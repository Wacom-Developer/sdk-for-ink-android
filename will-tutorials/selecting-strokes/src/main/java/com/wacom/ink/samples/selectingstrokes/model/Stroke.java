package com.wacom.ink.samples.selectingstrokes.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.RectF;

import com.wacom.ink.manipulation.Intersectable;
import com.wacom.ink.path.PathBuilder;
import com.wacom.ink.rasterization.BlendMode;
import com.wacom.ink.utils.Utils;

public class Stroke implements Intersectable{
	
	private FloatBuffer points;
	private int color;
	private int stride;
	private int size;
	private float width;
	private float startT;
	private float endT;
	private BlendMode blendMode;
	
	private RectF bounds;
	private FloatBuffer segmentsBounds;
	
	public Stroke(){
		bounds = new RectF();
	}
	
	public Stroke(int size) {
		this();
		setPoints(Utils.createNativeFloatBufferBySize(size), size);
		startT = 0.0f;
		endT = 1.0f;
	}

	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public FloatBuffer getPoints() {
		return points;
	}

	public int getSize() {
		return size;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getStartValue() {
		return startT;
	}

	public float getEndValue() {
		return endT;
	}

	public void setInterval(float startT, float endT) {
		this.startT = startT;
		this.endT = endT;
	}

	public void setPoints(FloatBuffer points, int pointsSize) {
		size = pointsSize;  
		this.points = points;
	}

	public void copyPoints(FloatBuffer source, int sourcePosition, int size) {
		this.size = size;
		points = ByteBuffer.allocateDirect(size * Float.SIZE/Byte.SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
		Utils.copyFloatBuffer(source, points, sourcePosition, 0, size);
	}

	@Override
	public FloatBuffer getSegmentsBounds() {
		return segmentsBounds;
	}

	@Override
	public RectF getBounds() {
		return bounds;
	}

	public void setBlendMode(BlendMode blendMode){
		this.blendMode = blendMode;
	}
	
	public BlendMode getBlendMode() {
		return blendMode;
	}
	
	public void calculateBounds(){
		RectF segmentBounds = new RectF();
		Utils.invalidateRectF(bounds);
		//Allocate a float buffer to hold the segments' bounds. 
		FloatBuffer segmentsBounds = Utils.createNativeFloatBuffer(PathBuilder.calculateSegmentsCount(size, stride) * 4);
		segmentsBounds.position(0);
		for (int index=0;index<PathBuilder.calculateSegmentsCount(size, stride);index++){
			PathBuilder.calculateSegmentBounds(getPoints(), getStride(), getWidth(), index, 0.0f, segmentBounds);
			segmentsBounds.put(segmentBounds.left);
			segmentsBounds.put(segmentBounds.top);
			segmentsBounds.put(segmentBounds.width());
			segmentsBounds.put(segmentBounds.height());
			Utils.uniteWith(bounds, segmentBounds);
		}
		this.segmentsBounds = segmentsBounds;
	}
}
