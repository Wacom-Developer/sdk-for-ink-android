package com.wacom.ink.samples.workingwithrasters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wacom.ink.rasterization.BlendMode;
import com.wacom.ink.rasterization.InkCanvas;
import com.wacom.ink.rasterization.Layer;
import com.wacom.ink.rendering.EGLRenderingContext.EGLConfiguration;

public class WorkingWithRastersPart1 extends Activity {
	private InkCanvas inkCanvas;
	private Layer viewLayer;
	private Layer imageLayer;
	
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
				inkCanvas.glInit();

				viewLayer = inkCanvas.createViewLayer(width, height);
				imageLayer = inkCanvas.createLayer(width, height);
				
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 1;
				opts.inScaled = false;
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree_of_life, opts);
				inkCanvas.loadBitmap(imageLayer, bitmap, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE);
				
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
	}

	private void renderView() {
		inkCanvas.setTarget(viewLayer);
		inkCanvas.drawLayer(imageLayer, BlendMode.BLENDMODE_OVERWRITE);
		inkCanvas.invalidate();
	}
	
	private void releaseResources(){
		inkCanvas.dispose();
	}
}
