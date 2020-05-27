package com.wacom.ink.samples.workingwithrasters;

import java.nio.FloatBuffer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.wacom.ink.path.PathUtils;
import com.wacom.ink.path.PathUtils.Phase;
import com.wacom.ink.path.SpeedPathBuilder;
import com.wacom.ink.rasterization.BlendMode;
import com.wacom.ink.rasterization.InkCanvas;
import com.wacom.ink.rasterization.Layer;
import com.wacom.ink.rasterization.SolidColorBrush;
import com.wacom.ink.rasterization.StrokePaint;
import com.wacom.ink.rasterization.StrokeRenderer;
import com.wacom.ink.rendering.EGLRenderingContext.EGLConfiguration;
import com.wacom.ink.smooth.MultiChannelSmoothener;
import com.wacom.ink.smooth.MultiChannelSmoothener.SmoothingResult;

public class WorkingWithRastersPart2 extends Activity {
	private InkCanvas inkCanvas;
	private Layer viewLayer;
	private Layer currentFrameLayer;
	private Layer imageLayer;
	private Layer maskLayer;
	private SpeedPathBuilder pathBuilder;
	private StrokePaint paint;
	private SolidColorBrush brush;
	private MultiChannelSmoothener smoothener;
	private int pathStride;
	private StrokeRenderer strokeRenderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				if (inkCanvas!=null && !inkCanvas.isDisposed()){
					releaseResources();
				}
				inkCanvas = InkCanvas.create(holder, new EGLConfiguration());

				viewLayer = inkCanvas.createViewLayer(width, height);
				
				imageLayer = inkCanvas.createLayer(width, height);
				maskLayer = inkCanvas.createLayer(width, height);
				currentFrameLayer = inkCanvas.createLayer(width, height);
				
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 1;
				opts.inScaled = false;
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree_of_life, opts);
				inkCanvas.loadBitmap(imageLayer, bitmap, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE);
				
				inkCanvas.setTarget(currentFrameLayer);
				inkCanvas.drawLayer(imageLayer, BlendMode.BLENDMODE_OVERWRITE);
				
				pathBuilder = new SpeedPathBuilder();
				pathBuilder.setNormalizationConfig(100.0f, 4000.0f);
				pathBuilder.setMovementThreshold(2.0f);
				pathStride = pathBuilder.getStride();

				brush = new SolidColorBrush();

				paint = new StrokePaint();
				paint.setStrokeBrush(brush);	// Solid color brush.
				paint.setColor(Color.BLUE);		// Blue color.
				paint.setWidth(5.0f);			// Use fixed width.

				smoothener = new MultiChannelSmoothener(pathStride);
				
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
				if (event.getAction()==MotionEvent.ACTION_DOWN){
					inkCanvas.setTarget(currentFrameLayer);
					inkCanvas.drawLayer(imageLayer, BlendMode.BLENDMODE_OVERWRITE);
				}
				boolean bFinished = buildPath(event);
				drawStroke(event);
				if (bFinished){
					inkCanvas.setTarget(maskLayer);
					inkCanvas.clearColor();
					inkCanvas.fillPath(pathBuilder.getPathBuffer(), pathBuilder.getPathSize(), pathBuilder.getStride(), 0xFFFFFFFF, true);
					
					inkCanvas.setTarget(currentFrameLayer);
					inkCanvas.drawLayer(imageLayer, BlendMode.BLENDMODE_OVERWRITE);
					inkCanvas.drawLayer(maskLayer, BlendMode.BLENDMODE_MULTIPLY_NO_ALPHA);
				}
				renderView();
				return true;
			}
		});
	}

	private void renderView() {
		inkCanvas.setTarget(viewLayer);
		inkCanvas.drawLayer(currentFrameLayer, BlendMode.BLENDMODE_OVERWRITE);
		inkCanvas.invalidate();
	}

	private boolean buildPath(MotionEvent event){
		if (event.getAction()!=MotionEvent.ACTION_DOWN 
				&& event.getAction()!=MotionEvent.ACTION_MOVE 
				&& event.getAction()!=MotionEvent.ACTION_UP){
			return false;
		}
		
		// Add the current input point to the path builder
		if (event.getAction()==MotionEvent.ACTION_DOWN){
			smoothener.reset();
		}
		
		Phase phase = PathUtils.getPhaseFromMotionEvent(event);
		FloatBuffer part = pathBuilder.addPoint(phase, event.getX(), event.getY(), event.getEventTime());
		SmoothingResult smoothingResult;
		int partSize = pathBuilder.getPathPartSize();
		
		if (partSize>0){
			// Smooth the returned control points (aka path part).
			smoothingResult = smoothener.smooth(part, partSize, (phase==Phase.END));
			// Add the smoothed control points to the path builder.
			pathBuilder.addPathPart(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
		}
		
		return (event.getAction()==MotionEvent.ACTION_UP && pathBuilder.hasFinished());
	}

	private void drawStroke(MotionEvent event){
		strokeRenderer.drawPoints(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), event.getAction()==MotionEvent.ACTION_UP);
		
		if (event.getAction()!=MotionEvent.ACTION_UP){
			inkCanvas.setTarget(currentFrameLayer, strokeRenderer.getStrokeUpdatedArea());
			inkCanvas.drawLayer(imageLayer, BlendMode.BLENDMODE_OVERWRITE);
			strokeRenderer.blendStrokeUpdatedArea(currentFrameLayer, BlendMode.BLENDMODE_NORMAL);
		}
	}
	
	private void releaseResources(){
		strokeRenderer.dispose();
		inkCanvas.dispose();
	}
}
