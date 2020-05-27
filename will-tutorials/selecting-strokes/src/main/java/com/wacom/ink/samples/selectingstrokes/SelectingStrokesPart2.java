package com.wacom.ink.samples.selectingstrokes;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.wacom.ink.manipulation.Intersector;
import com.wacom.ink.manipulation.Intersector.IntersectionResult;
import com.wacom.ink.manipulation.Intersector.Interval;
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
import com.wacom.ink.samples.selectingstrokes.model.Stroke;
import com.wacom.ink.samples.selectingstrokes.model.StrokeSerializer;
import com.wacom.ink.smooth.MultiChannelSmoothener;
import com.wacom.ink.smooth.MultiChannelSmoothener.SmoothingResult;

public class SelectingStrokesPart2 extends Activity {
	private InkCanvas inkCanvas;
	private Layer viewLayer;
	private SpeedPathBuilder pathBuilder;
	private StrokePaint paint;
	private StrokePaint selectionPaint;
	private SolidColorBrush brush;
	private Layer strokesLayer;
	private Layer currentFrameLayer;
	private StrokeRenderer strokeRenderer;
	private StrokeRenderer selectionStrokeRenderer;
	private MultiChannelSmoothener smoothener;
	private int pathStride;

	private StrokeSerializer serializer;
	private LinkedList<Stroke> strokesList = new LinkedList<Stroke>();
	private Intersector<Stroke> intersector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		pathBuilder = new SpeedPathBuilder();
		pathBuilder.setNormalizationConfig(100.0f, 4000.0f);
		pathBuilder.setMovementThreshold(2.0f);
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
				paint.setStrokeBrush(brush);				// Solid color brush.

				selectionPaint = new StrokePaint();
				selectionPaint.setStrokeBrush(brush);		// Solid color brush.
				selectionPaint.setColor(Color.RED);			// Red color.
				selectionPaint.setWidth(2.0f);				// Use fixed width.
				
				smoothener = new MultiChannelSmoothener(pathStride);
				
				strokeRenderer = new StrokeRenderer(inkCanvas, paint, 3, width, height); 
				selectionStrokeRenderer = new StrokeRenderer(inkCanvas, selectionPaint, pathStride, width, height);
				
				serializer = new StrokeSerializer();
				
				intersector = new Intersector<Stroke>();
						
				loadStrokes();
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

				if (bFinished){

					if (strokesList.size()>0) {
						intersector.setTargetAsClosedPath(pathBuilder.getPathBuffer(), 0, pathBuilder.getPathSize(), pathStride);
						LinkedList<Stroke> removedStrokes = new LinkedList<Stroke>();
						LinkedList<Stroke> newStrokes = new LinkedList<Stroke>();
						for (Stroke stroke: strokesList){
							IntersectionResult intersection = intersector.intersectWithTarget(stroke);
							if (intersection.getCount()==1){
								Intersector.IntervalIterator iterator = intersection.getIterator();
								Interval interval = iterator.next();
								if (interval.inside){
									stroke.setColor(Color.RED);
								}
							} else if (intersection.getCount()>1){
								removedStrokes.add(stroke);
								Intersector.IntervalIterator iterator = intersection.getIterator();
								while (iterator.hasNext()){
									Interval interval = iterator.next();
									int size = interval.toIndex - interval.fromIndex + stroke.getStride();

									Stroke newStroke = new Stroke(size);

									newStroke.copyPoints(stroke.getPoints(), interval.fromIndex, size);
									newStroke.setStride(stroke.getStride());
									newStroke.setColor(stroke.getColor());
									newStroke.setWidth(stroke.getWidth());
									newStroke.setBlendMode(stroke.getBlendMode());
									newStroke.setInterval(interval.fromValue, interval.toValue);
									newStroke.calculateBounds();

									if (interval.inside){
										newStroke.setColor(Color.RED);
									} else {
										newStroke.setColor(stroke.getColor());
									}

									newStrokes.add(newStroke);
								}
							}
						}
						strokesList.removeAll(removedStrokes);
						strokesList.addAll(newStrokes);
						drawStrokes();
					}
				}
				drawStroke(event);
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
		selectionStrokeRenderer.drawPoints(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), event.getAction()==MotionEvent.ACTION_UP);
		selectionStrokeRenderer.drawPrelimPoints(pathBuilder.getPreliminaryPathBuffer(), 0, pathBuilder.getFinishedPreliminaryPathSize());

		if (event.getAction()!=MotionEvent.ACTION_UP){
			inkCanvas.setTarget(currentFrameLayer, selectionStrokeRenderer.getStrokeUpdatedArea());
			inkCanvas.clearColor(Color.WHITE);
			inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
			selectionStrokeRenderer.blendStrokeUpdatedArea(currentFrameLayer, BlendMode.BLENDMODE_NORMAL);
		}
	}

	private void drawStrokes() {
		inkCanvas.setTarget(strokesLayer);
		inkCanvas.clearColor();

		for (Stroke stroke: strokesList){
			paint.setColor(stroke.getColor());
			paint.setWidth(stroke.getWidth());
			strokeRenderer.setStrokePaint(paint);
			strokeRenderer.drawPoints(stroke.getPoints(), 0, stroke.getSize(), stroke.getStartValue(), stroke.getEndValue(), true);
			strokeRenderer.blendStroke(strokesLayer, BlendMode.BLENDMODE_NORMAL);
		}

		inkCanvas.setTarget(currentFrameLayer);
		inkCanvas.clearColor(Color.WHITE);
		inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
	}

	protected void loadStrokes(){
		try {
			strokesList = serializer.deserialize(getResources().getAssets().open("will.bin"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void releaseResources(){
		strokeRenderer.dispose();
		inkCanvas.dispose();
	}
}
