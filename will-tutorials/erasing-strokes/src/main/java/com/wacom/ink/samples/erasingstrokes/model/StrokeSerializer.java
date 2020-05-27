package com.wacom.ink.samples.erasingstrokes.model;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;

import android.net.Uri;

import com.wacom.ink.serialization.InkDecoder;
import com.wacom.ink.serialization.InkEncoder;
import com.wacom.ink.utils.Utils;

public class StrokeSerializer {
	private final static int DEFAULT_DECIMAL_PRECISION = 2;
	
	private int decimalPrecision;
	public StrokeSerializer(int decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public StrokeSerializer() {
		this(DEFAULT_DECIMAL_PRECISION);
	}

	public boolean serialize(Uri uri, LinkedList<Stroke> strokes) {
		byte[] bytes = null;
		int encSize = 0;

		InkEncoder encoder = new InkEncoder();

		for (Stroke stroke : strokes) {
			encoder.encodePath(decimalPrecision, stroke.getPoints(), stroke.getSize(), stroke.getStride(), stroke.getWidth(), stroke.getColor(), stroke.getStartValue(), stroke.getEndValue(), stroke.getBlendMode());
		}

		ByteBuffer encData = encoder.getEncodedData();
		encSize = encoder.getEncodedDataSizeInBytes();

		bytes = new byte[encSize];
		if (encSize > 0) {
			encData.position(0);
			encData.get(bytes);
		}
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(bytes);

		return Utils.saveBinaryFile(uri, buffer, 0, encSize);
	}

	public LinkedList<Stroke> deserialize(InputStream inputStream) {
		ByteBuffer buffer = Utils.readInputStream(inputStream);
		if (buffer==null){
			return new LinkedList<Stroke>();
		}
		
		buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		InkDecoder decoder = new InkDecoder(buffer);
		LinkedList<Stroke> result = new LinkedList<Stroke>();
		
		while (decoder.decodeNextPath()){
			Stroke stroke = new Stroke(decoder.getDecodedPathSize());
			
			stroke.setColor(decoder.getDecodedPathIntColor());
			stroke.setStride(decoder.getDecodedPathStride());
			stroke.setInterval(decoder.getDecodedPathTs(), decoder.getDecodedPathTf());
			stroke.setWidth(decoder.getDecodedPathWidth());
			stroke.setBlendMode(decoder.getDecodedBlendMode());
			Utils.copyFloatBuffer(decoder.getDecodedPathData(), stroke.getPoints(), 0, 0, decoder.getDecodedPathSize());
			
			stroke.calculateBounds();
			
			result.add(stroke);
		}
		return result;
	}
}
