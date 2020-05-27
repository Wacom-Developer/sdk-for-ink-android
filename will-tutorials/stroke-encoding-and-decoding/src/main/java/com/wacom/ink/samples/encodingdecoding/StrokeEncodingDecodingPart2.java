package com.wacom.ink.samples.encodingdecoding;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

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
import com.wacom.ink.samples.encodingdecoding.model.Stroke;
import com.wacom.ink.samples.encodingdecoding.model.StrokeSerializer;
import com.wacom.ink.smooth.MultiChannelSmoothener;
import com.wacom.ink.smooth.MultiChannelSmoothener.SmoothingResult;
import com.wacom.ink.samples.encodingdecoding.R;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.LinkedList;

public class StrokeEncodingDecodingPart2 extends Activity {
	private InkCanvas inkCanvas;
	private Layer viewLayer;
	private SpeedPathBuilder pathBuilder;
	private StrokePaint paint;
	private SolidColorBrush brush;
	private MultiChannelSmoothener smoothener;
	private int pathStride;
	private StrokeRenderer strokeRenderer;
	private Layer strokesLayer;
	private Layer currentFrameLayer;
	private StrokeSerializer serializer;
	private LinkedList<Stroke> strokesList = new LinkedList<Stroke>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		pathBuilder = new SpeedPathBuilder();
		pathBuilder.setNormalizationConfig(100.0f, 4000.0f);
		pathBuilder.setMovementThreshold(2.0f);
		pathBuilder.setPropertyConfig(PropertyName.Width, 5f, 10f, Float.NaN, Float.NaN, PropertyFunction.Power, 1.0f, false);
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

				smoothener = new MultiChannelSmoothener(pathStride);
				smoothener.enableChannel(2);
				
				strokeRenderer = new StrokeRenderer(inkCanvas, paint, pathStride, width, height);
				
				serializer = new StrokeSerializer();
				
				loadStrokes();
//				strokesList.clear();
				drawStrokes();
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
				boolean bFinished = buildPath(event);
				drawStroke(event); 
				renderView();
				
				if (bFinished){
					Stroke stroke = new Stroke();
					stroke.copyPoints(pathBuilder.getPathBuffer(), 0, pathBuilder.getPathSize());
					stroke.setStride(pathBuilder.getStride());
					stroke.setWidth(Float.NaN);
					stroke.setColor(paint.getColor());
					stroke.setInterval(0.0f, 1.0f);
					stroke.setBlendMode(BlendMode.BLENDMODE_NORMAL);
					
					strokesList.add(stroke);
					
					Toast.makeText(StrokeEncodingDecodingPart2.this, "We have " + strokesList.size() + " strokes in the list.", Toast.LENGTH_SHORT).show();
				}
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

	private boolean buildPath(MotionEvent event){
		if (event.getAction()!=MotionEvent.ACTION_DOWN 
				&& event.getAction()!=MotionEvent.ACTION_MOVE 
				&& event.getAction()!=MotionEvent.ACTION_UP){
			return false;
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
		
		return (event.getAction()==MotionEvent.ACTION_UP && pathBuilder.hasFinished());
	}

	private void drawStroke(MotionEvent event){
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
	
	protected void loadStrokes(){
		strokesList = serializer.deserialize(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/will.bin")));
	}
	
	protected void saveStrokes(){
		serializer.serialize(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/will.bin")), strokesList);
	}
	
	private void drawStrokes() {
		inkCanvas.setTarget(strokesLayer);
		inkCanvas.clearColor();
		
		for (Stroke stroke: strokesList){
			paint.setColor(stroke.getColor());
			strokeRenderer.setStrokePaint(paint);
			strokeRenderer.drawPoints(stroke.getPoints(), 0, stroke.getSize(), stroke.getStartValue(), stroke.getEndValue(), true);
			strokeRenderer.blendStroke(strokesLayer, stroke.getBlendMode());
		}
		
		inkCanvas.setTarget(currentFrameLayer);
		inkCanvas.clearColor(Color.WHITE);
		inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
	}
	
	private void releaseResources(){
		strokeRenderer.dispose();
		inkCanvas.dispose();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveStrokes();
		super.onSaveInstanceState(outState);
	}
}
