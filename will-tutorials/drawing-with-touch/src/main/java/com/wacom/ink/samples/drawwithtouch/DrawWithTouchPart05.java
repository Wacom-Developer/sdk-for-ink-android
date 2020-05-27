package com.wacom.ink.samples.drawwithtouch;

import java.nio.FloatBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.wacom.ink.path.PathBuilder.PropertyFunction;
import com.wacom.ink.path.PathBuilder.PropertyName;
import com.wacom.ink.path.PathUtils;
import com.wacom.ink.path.PathUtils.Phase;
import com.wacom.ink.path.SpeedPathBuilder;
import com.wacom.ink.rasterization.BlendMode;
import com.wacom.ink.rasterization.InkCanvas;
import com.wacom.ink.rasterization.Layer;
import com.wacom.ink.rasterization.ParticleBrush;
import com.wacom.ink.rasterization.RotationMode;
import com.wacom.ink.rasterization.StrokePaint;
import com.wacom.ink.rasterization.StrokeRenderer;
import com.wacom.ink.rendering.EGLRenderingContext.EGLConfiguration;
import com.wacom.ink.smooth.MultiChannelSmoothener;
import com.wacom.ink.smooth.MultiChannelSmoothener.SmoothingResult;

public class DrawWithTouchPart05 extends Activity {
	private InkCanvas inkCanvas;
	private Layer viewLayer;
	private SpeedPathBuilder pathBuilder;
	private StrokePaint paint;
	private ParticleBrush brush;
	private MultiChannelSmoothener smoothener;
	private int pathStride;
	private StrokeRenderer strokeRenderer;
	private Layer strokesLayer;
	private Layer currentFrameLayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_with_touch);

		pathBuilder = new SpeedPathBuilder();
		pathBuilder.setNormalizationConfig(100.0f, 4000.0f);
		pathBuilder.setMovementThreshold(2.0f);
		pathBuilder.setPropertyConfig(PropertyName.Width, 10f, 80f, Float.NaN, Float.NaN, PropertyFunction.Power, 1.0f, false);
		pathBuilder.setPropertyConfig(PropertyName.Alpha, 0.05f, 0.4f, Float.NaN, Float.NaN, PropertyFunction.Power, 1.0f, false);
		
		pathStride = pathBuilder.getStride();
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				if (inkCanvas!=null && !inkCanvas.isDisposed()){
					releaseResources();
				}
				
				inkCanvas = InkCanvas.create(holder, new EGLConfiguration());
				
				viewLayer = inkCanvas.createViewLayer(width, height);
				strokesLayer = inkCanvas.createLayer(width, height);
				currentFrameLayer = inkCanvas.createLayer(width, height);
				
				inkCanvas.clearLayer(currentFrameLayer, Color.WHITE);
				

				brush = new ParticleBrush();
				brush.setBlendingMode(BlendMode.BLENDMODE_MAX);
				brush.setScattering(0.15f);
				brush.setSpacing(0.15f);
				brush.setRotationMode(RotationMode.RANDOM);
				
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 1;
				opts.inScaled = false;
				
				Bitmap shapeTexture = BitmapFactory.decodeResource(getResources(), R.drawable.shape, opts);
				Bitmap fillTexture = BitmapFactory.decodeResource(getResources(), R.drawable.fill, opts);
				
				
				brush.allocateTextures(shapeTexture, fillTexture, fillTexture.getWidth(), fillTexture.getHeight());
				
				paint = new StrokePaint();
				paint.setStrokeBrush(brush);	// Particle brush.
				paint.setColor(Color.BLUE);		// Blue color.
//				paint.setWidth(Float.NaN);		// Expected variable width.
//				paint.setAlpha(Float.NaN);		// Expected variable alpha.
				
				paint.useVariableWidth();
				paint.useVariableAlpha();
				
				smoothener = new MultiChannelSmoothener(pathStride);
				smoothener.enableChannel(2);
				smoothener.enableChannel(3); 
				
				strokeRenderer = new StrokeRenderer(inkCanvas, paint, pathStride, width, height);
				
				renderView();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {

			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				releaseResources();
			}
		});

		surfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				buildPath(event);
				drawStroke(event); 
				renderView();
				return true;
			}
		});

	}

	private void renderView() {
		inkCanvas.setTarget(viewLayer);
		// Copy the current frame layer in the view layer to present it on the screen.
		inkCanvas.drawLayer(currentFrameLayer, BlendMode.BLENDMODE_OVERWRITE);
		inkCanvas.invalidate();
	}

	private void buildPath(MotionEvent event){
		if (event.getAction()!=MotionEvent.ACTION_DOWN 
				&& event.getAction()!=MotionEvent.ACTION_MOVE 
				&& event.getAction()!=MotionEvent.ACTION_UP){
			return;
		}
		
		if (event.getAction()==MotionEvent.ACTION_DOWN){
			// Reset the smoothener instance when starting to generate a new path.
			smoothener.reset();
		}
		
		Phase phase = PathUtils.getPhaseFromMotionEvent(event);
		// Add the current input point to the path builder
		FloatBuffer part = pathBuilder.addPoint(phase, event.getX(), event.getY(), event.getEventTime());
		SmoothingResult smoothingResult;
		int partSize = pathBuilder.getPathPartSize();
		
		if (partSize>0){
			// Smooth the returned control points (aka path part).
			smoothingResult = smoothener.smooth(part, partSize, (phase==Phase.END));
			// Add the smoothed control points to the path builder.
			pathBuilder.addPathPart(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
		}
		
		// Create a preliminary path.
		FloatBuffer preliminaryPath = pathBuilder.createPreliminaryPath();
		// Smoothen the preliminary path's control points (return inform of a path part).
		smoothingResult = smoothener.smooth(preliminaryPath, pathBuilder.getPreliminaryPathSize(), true);
		// Add the smoothed preliminary path to the path builder.
		pathBuilder.finishPreliminaryPath(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
	}

	private void drawStroke(MotionEvent event){
		if (event.getAction()==MotionEvent.ACTION_DOWN){
			strokeRenderer.setUseSeed(true);
			strokeRenderer.setSeed((int) System.currentTimeMillis());
		}
		
		strokeRenderer.drawPoints(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), event.getAction()==MotionEvent.ACTION_UP);
		strokeRenderer.drawPrelimPoints(pathBuilder.getPreliminaryPathBuffer(), 0, pathBuilder.getFinishedPreliminaryPathSize());
		
		if (event.getAction()!=MotionEvent.ACTION_UP){
			inkCanvas.setTarget(currentFrameLayer, strokeRenderer.getStrokeUpdatedArea());
			inkCanvas.clearColor(Color.WHITE);
			inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
			strokeRenderer.blendStrokeUpdatedArea(currentFrameLayer, BlendMode.BLENDMODE_NORMAL);
		} else {
			strokeRenderer.blendStroke(strokesLayer, BlendMode.BLENDMODE_NORMAL);
			inkCanvas.setTarget(currentFrameLayer);
			inkCanvas.clearColor(Color.WHITE);
			inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
		}
	}
	
	private void releaseResources(){
		brush.dispose();
		strokeRenderer.dispose();
		inkCanvas.dispose();
	}
}
