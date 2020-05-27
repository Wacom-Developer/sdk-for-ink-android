package com.wacom.ink.samples.drawwithtouch;

import java.nio.FloatBuffer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.wacom.ink.rasterization.SolidColorBrush;
import com.wacom.ink.rasterization.StrokePaint;
import com.wacom.ink.rasterization.StrokeRenderer;
import com.wacom.ink.rendering.EGLRenderingContext.EGLConfiguration;

public class DrawWithTouchPart02 extends Activity {
	private InkCanvas inkCanvas;
	private Layer viewLayer;
	private SpeedPathBuilder pathBuilder;
	private StrokePaint paint;
	private SolidColorBrush brush;
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
		pathBuilder.setPropertyConfig(PropertyName.Width, 5f, 10f, 5f, 10f, PropertyFunction.Power, 1.0f, false);
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
				
				brush = new SolidColorBrush();
				
				paint = new StrokePaint();
				paint.setStrokeBrush(brush);	// Solid color brush.
				paint.setColor(Color.BLUE);		// Blue color.
				paint.setWidth(Float.NaN);		// Expected variable width.
				
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
		
		Phase phase = PathUtils.getPhaseFromMotionEvent(event);
		// Add the current input point to the path builder
		FloatBuffer part = pathBuilder.addPoint(phase, event.getX(), event.getY(), event.getEventTime());
		int partSize = pathBuilder.getPathPartSize();
		Log.d("XXXX", "xx(1): path size=" + pathBuilder.getPathSize() + " | pos=" + pathBuilder.getPathLastUpdatePosition() + " | added_size=" + pathBuilder.getAddedPointsSize());

		if (partSize>0){
			// Add the returned control points (aka path part) to the path builder. 
			pathBuilder.addPathPart(part, partSize);
		}
		
		Log.d("XXXX", "xx(2): path size=" + pathBuilder.getPathSize() + " | pos=" + pathBuilder.getPathLastUpdatePosition() + " | added_size=" + pathBuilder.getAddedPointsSize());

	}

	private void drawStroke(MotionEvent event){
		strokeRenderer.drawPoints(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), event.getAction()==MotionEvent.ACTION_UP);
		
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
		strokeRenderer.dispose();
		inkCanvas.dispose();
	}
}
