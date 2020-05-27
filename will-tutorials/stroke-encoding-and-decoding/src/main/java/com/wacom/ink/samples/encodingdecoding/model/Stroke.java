package com.wacom.ink.samples.encodingdecoding.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.wacom.ink.rasterization.BlendMode;
import com.wacom.ink.utils.Utils;

public class Stroke{
	
	private FloatBuffer points;
	private int color;
	private int stride;
	private int size;
	private float width;
	private float startT;
	private float endT;
	private BlendMode blendMode;
	private int paintIndex;
	private int seed;
	private boolean hasRandomSeed;
	
	public Stroke(){
		
	}
	
	public Stroke(int size) {
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

	public void setBlendMode(BlendMode blendMode){
		this.blendMode = blendMode;
	}
	
	public BlendMode getBlendMode() {
		return blendMode;
	}

	public void setPaintIndex(int paintIndex) {
		this.paintIndex = paintIndex;
	}

	public int getPaintIndex() {
		return paintIndex;
	}

	public int getSeed() {
		return seed;
	}
	
	public void setSeed(int seed){
		this.seed = seed;
	}

	public void setHasRandomSeed(boolean hasRandomSeed) {
		this.hasRandomSeed = hasRandomSeed;
	}
	
	public boolean hasRandomSeed() {
		return hasRandomSeed;
	}
	
}
