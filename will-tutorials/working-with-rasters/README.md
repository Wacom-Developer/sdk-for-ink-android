# Tutorial 5: Working with rasters

In this tutorial, we demonstrate how to load an Android bitmap into WILL, and how to mask it with a path created by touch. 
The tutorial is divided into the following parts:

* [Part 1: Displaying raster images](#part-1-displaying-raster-images)
* [Part 2: Creating image masks](#part-2-creating-image-masks)

Each part builds on the previous one, extending and improving its functionality.

## Prerequisites

This tutorial continues on from Part 1 of Tutorial 1: Drawing with touch.

## Source code

You can find the tutorial source code in the following location:
```Android Studio: /sdk/will-tutorials```

---
---
## Part 1: Displaying raster images

Although the WILL SDK *Rasterizer* module is not a general-purpose 2D drawing engine, it can draw raster images. In this tutorial, you will display and mask images using the functionality provided in the SDK. 

In Part 1 of this tutorial, you will load and display a raster image. Part 1 of this tutorial continues on from Part 1 of Tutorial 1: Drawing with touch.

### Step 1: Create a layer to hold the raster image

Create a *Layer* called imageLayer:

```java
imageLayer = inkCanvas.createLayer(width, height);
```

**Note:** This layer must be created on the GL thread (the thread the WILL engine has been initialized from). 
In this case, this is the UI thread.


### Step 2: Load a bitmap

Load a bitmap and apply it on the imageLayer:

```java
BitmapFactory.Options opts = new BitmapFactory.Options();
opts.inSampleSize = 1;
opts.inScaled = false;
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree_of_life, opts);
inkCanvas.loadBitmap(imageLayer, bitmap, GLES20.GL_LINEAR, GLES20.GL_CLAMP_TO_EDGE);
```

**Note:** The ```loadBitmap(...)``` method must be called on the GL thread. 
In this case, this is the UI thread. 
After calling ```loadBitmap(...)```, the bitmap instance can be safely recycled.

### Step 3: Display the bitmap

Update the ```renderView()``` method to draw the imageLayer on the screen:

```java
private void renderView() {
    inkCanvas.setTarget(viewLayer);
    inkCanvas.drawLayer(imageLayer, BlendMode.BLENDMODE_OVERWRITE);
    inkCanvas.invalidate();
}
```

---
---
## Part 2: Creating image masks

In Part 2 of this tutorial, you will create an image mask and apply it to an image. 
To do this, you will implement a selection tool that defines an area to use as an image mask. 
You can partially use the implementation from Part 1 of Tutorial 1: Drawing with touch.
 
### Step 1: Create a mask layer

Create a *Layer* called maskLayer:

```java
maskLayer = inkCanvas.createLayer(width, height);
```

### Step 2: Mask an area on the canvas

Update the touch input processing logic so that:

* When you start building the path, you draw the imageLayer over the currentFrameLayer. 
* While you build the path, you draw the selection tool to the currentFrameLayer (call the ```drawStroke(...)``` method).
* When the path is complete, you set the maskLayer as a target and clear it (fill it with zeros), then use the ```InkCanvas.fillPath(...)``` method to fill the area defined by the path with the color #FFFFFFFF. 
  The maskLayer acts as an image mask.
* You update the *currentFrameLayer* by copying the imageLayer to the *currentFrameLayer* using the ```BLENDMODE_OVERWRITE``` blend mode, and blending the *maskLayer* with the *currentFrameLayer* using the ```BLENDMODE_MULTIPLY_NO_ALPHA``` blend mode. 

The code looks like:

```java
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
```

**Note:** Use the ```BLENDMODE_MULTIPLY_NO_ALPHA``` blend mode to remove the area outside the path, leaving the area of the image enclosed by the path untouched. 
Alternatively, use the ```BLENDMODE_MULTIPLY_NO_ALPHA_INVERT``` blend mode to achieve the opposite effect.

### Step 3: Display the masked area

Update the ```renderView(...)``` method: 

```java
private void renderView() {
    inkCanvas.setTarget(viewLayer);
    inkCanvas.drawLayer(currentFrameLayer, BlendMode.BLENDMODE_OVERWRITE);
    inkCanvas.invalidate();
}
```

---
---
