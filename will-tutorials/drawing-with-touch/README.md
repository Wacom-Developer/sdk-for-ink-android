# Tutorial 1: Drawing with touch

In this tutorial, we demonstrate how to use WILL SDK to draw strokes in response to a user's touch input.
The tutorial is divided into the following parts:

* [Part 1: Setting up the ink engine](#part-1-setting-up-the-ink-engine)
* [Part 2: Building paths from touch input](#part-2-building-paths-from-touch-input)
* [Part 3: Smoothing paths](#part-3-smoothing-paths)
* [Part 4: Drawing preliminary paths](#part-4-drawing-preliminary-paths)
* [Part 5: Using a particle brush](#part-5-using-a-particle-brush)
* [Part 6: Drawing semi-transparent strokes](#part-6-drawing-semi-transparent-strokes)
* [Part 7: Calculating stroke boundary](#part-7-calculating-stroke-boundary)

Each part builds on the previous one, extending and improving its functionality.

## Prerequisites

You will need the WILL SDK to complete this tutorial.
Basic experience with OpenGL programming would be useful, but it is not required.
For more information, see http://developer.android.com/guide/topics/graphics/opengl.html and https://www.khronos.org/opengles/.

## Source code

You can find the tutorial source code in the following location:
```/sdk/will-tutorials```

---
---
## Part 1: Setting up the ink engine

WILL SDK provides a 2D drawing engine which focusses primarily on inking.
It uses OpenGL 2.0 (or newer) and is compatible with the Android OS, which uses EGL/OpenGL for 2D/3D graphics rendering.

A WILL *InkCanvas* can be bound to either an Android ```SurfaceView``` or an Android ```TextureView```.
In this tutorial, you will use a ```SurfaceView```, as this option offers superior performance during compositing.
For more information on the Android Graphics architecture, see the [official Android documentation](https://source.android.com/devices/graphics/architecture.html). 

In Part 1 of this tutorial, you will set up a rendering environment. 


### Step 1: Create an Android project

Create a project with a simple activity and override the ```onCreate``` method to provide a drawing surface.
Use the Android ```SurfaceView``` class, which provides a dedicated drawing surface embedded inside the View hierarchy, add a ```SurfaceHolder.Callback``` interface to its SurfaceHolder instance, and implement its methods:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {

    ....

    surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            ...
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            ....
        }
    });
```
    
### Step 2: Create a canvas to draw on

The *InkCanvas* class is the core of the WILL SDK *Rendering* module.
It is responsible for the management of the allocated OpenGL resources and can only be used on an OpenGL thread. 

Create an *InkCanvas* instance when a surface is available.
Release the canvas's allocated OpenGL resources when the EGL context is lost, or when the rendering surface provided by the Android framework is destroyed or recreated. 

An *InkCanvas* instance can be created with one of the following factory methods: 
* ```InkCanvas.create()```
* ```InkCanvas.create(SurfaceHolder, EGLRenderingContext.EGLConfiguration) ```

In this case, use ```InkCanvas.create(SurfaceHolder, EGLRenderingContext.EGLConfiguration)``` so that an EGL rendering context will be created, applied, and dedicated to this canvas only: 

```java
@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (inkCanvas!=null && !inkCanvas.isDisposed()){
        releaseResources();
    }

    inkCanvas = InkCanvas.create(holder, new EGLConfiguration());

    ...
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
    releaseResources();
}
```

The thread that this method is called from (the UI thread) becomes the OpenGL thread.

### Step 3: Create a method to dispose of the canvas

Since an InkCavas instance can only be used on the thread from which it was initialized, 
you must release the InkCanvas instance's allocated OpenGL resources if the EGL context is going to be lost, 
or the rendering surface provided by the Android framework is going to be destroyed or recreated.

Define the ```releaseResources()``` method to dispose of the canvas, finalizing it properly and making it safe for garbage collection:

```java
private void releaseResources(){
    inkCanvas.dispose();
}
```

### Step 4: Create a drawing layer

A *Layer* represents storage for graphics (pixels) that can be updated, drawn to other layers, or presented on screen. 
To draw a stroke onto an InkCanvas, you must first draw it into a layer.

Use the ```inkCanvas.createViewLayer()``` method to create and initialize a layer (viewLayer), binding it to the default OpenGL framebuffer (the framebuffer provided by the window system):

```java
viewLayer = inkCanvas.createViewLayer(width, height);
```

Everything drawn into this layer is presented to the screen.

**Note:** The ```inkCanvas.createViewLayer()``` method must be called on the GL thread (the thread the ink engine has been initialized from). In this case, this is the UI thread.

### Step 5: Present the drawing layer to the screen

Implement the ```renderView()``` method to clear the view layer (filling it with a red color), then present it on the screen using ```inkCanvas.invalidate()```.

```java
private void renderView() {
    inkCanvas.setTarget(viewLayer);
    inkCanvas.clearColor(Color.RED);
    inkCanvas.invalidate();
}
```

Update the ```surfaceChanged()``` method to use ```renderView()```:

```java
@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (inkCanvas!=null && !inkCanvas.isDisposed()){
        releaseResources();
    }

    inkCanvas = InkCanvas.create(holder, new EGLConfiguration());
    viewLayer = inkCanvas.createViewLayer(width, height);   
    renderView();
}
```

---
---
## Part 2: Building paths from touch input

In Part 2 of this tutorial, you will build a path from the user's touch input and draw it into the drawing layer (viewLayer) as a stroke.

Path building is performed in two phases:

* A pointer event is converted into a partial path. This partial path can be manipulated (for example, to smooth it) before proceeding.
* The partial path is added to the currently built path. 

The WILL SDK supports velocity-based or pressure-based path building.
In velocity-based path building, the **Path** module calculates stroke geometry based on the speed of the input. 
In pressure-based path building, the **Path** module calculates stroke geometry based on the input pressure. In this tutorial, you will use velocity-based path building.

### Step 1: Create paths based on user input

Create an instance called *pathBuilder* using the ```SpeedPathBuilder``` class, which produces paths with variable width depending on the speed of the user's input:

```java
pathBuilder = new SpeedPathBuilder(getResources().getDisplayMetrics().density);
pathBuilder.setNormalizationConfig(100.0f, 4000.0f);
pathBuilder.setPropertyConfig(PropertyName.Width, 5f, 10f, 5f, 10f, PropertyFunction.Power, 1.0f, false);
pathStride = pathBuilder.getStride();
```

In the code above:

* ```pathBuilder.setNormalizationConfig(...)``` sets the minimum and maximum velocities of the touch input. 
Velocity values outside this range are clamped to these min/max values.

* ```pathBuilder.setPropertyConfig(PropertyName.Width, ....)``` instructs the path builder to produce a width property for each control point. It sets:
  * The min/max width values
  * The initial/final width values
  * The property dynamics function and its parameter
  * A flag that defines whether the generated value should be inverted
  
* ```pathBuilder.getStride()``` retrieves the stride of the paths generated by the path builder. 
It defines the offset from one control point to the next. 
In this case, because a variable-width path builder is configured, the stride will be 3. 
This means that the path builder will work with sets of points, returned as a series of x, y, and width values. 
The stride can also be treated as the count of values defining a single path point.

**Note:** *PathBuilder* is unitless. However, we recommend that you use pixels as a unit when working with Android, as the Android application framework promotes the use of pixels when working with graphics, and touch input is in pixels for Android.

### Step 2: Create a brush to use when drawing strokes

Use a *SolidColorBrush* instance to draw strokes with a solid color:

```java
brush = new SolidColorBrush();
```

The *SolidColorBrush* is more accurate for paths that vary sharply in width than the faster *directBrush*.

### Step 3: Create and initialize layers to render the stroke

Create and initialize three WILL layers to render a stroke and present it to the screen:

```java
viewLayer = inkCanvas.createViewLayer(width, height);
strokesLayer = inkCanvas.createLayer(width, height);
currentFrameLayer = inkCanvas.createLayer(width, height);

inkCanvas.clearLayer(currentFrameLayer, Color.WHITE);
```

* *viewLayer* is bound to the default framebuffer (the default OpenGL framebuffer provided by the window system). Everything drawn into this layer is presented to the screen.
* *strokesLayer* contains all drawn strokes.
* *currentFrameLayer* contains the currently rendered frame, which should be copied to viewLayer when the rendering is complete.

**Note:** All layer creation calls should be made on the GL thread (the thread from which the WILL engine has been initialized). In this case, this is the UI thread.

### Step 4: Create an instance to store transient stroke information between successive draw calls

Create a *StrokePaint* instance to hold information about how the strokes should be drawn:

```java
paint = new StrokePaint();
paint.setStrokeBrush(brush);    // Solid color brush.
paint.setColor(Color.BLUE);     // Blue color.
paint.setWidth(Float.NaN);      // Expected variable width.
```
Each *StrokePaint* instance can be treated as a stroke rendering configuration: 
it holds all the information required by the WILL SDK Rasterizer module to draw a path, 
in the form of a stroke, into a specified *InkCanvas*: 
it holds the previously created *SolidColorBrush* instance (called brush), a color, the stroke width, and the stroke start cap and end cap flags. 
You will set the beginning and ending caps later, when you draw the parts of the stroke.

In the code above, ```paint.setWidth(Float.NaN)``` indicates that the stroke will be drawn with variable width.

Finally create an instance of the *StrokeRenderer* class, which encapsulates dirty area clean-up and rendering optimization techniques, allowing fast and smooth stroke drawing.

```
strokeRenderer = new StrokeRenderer(inkCanvas, paint, pathStride, width, height);
```

### Step 5: Set an ```OnTouchListener``` to handle user input

Set an ```OnTouchListener``` to *surfaceView*:

```java
surfaceView.setOnTouchListener(new OnTouchListener()) {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
            buildPath(event);
            drawStroke(event); 
            renderView();
            return true;
    }
});
```

### Step 6: Build a path from the user input

Define a ```buildPath(...)``` method to build a path with a variable width, based on the received Android ```MotionEvents```:

```java
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

    if (partSize>0){
        // Add the returned control points (aka partial path) to the path builder. 
        pathBuilder.addPathPart(part, partSize);
    }
}
```

For each phase of the path lifecycle, the path builder calculates a partial path. 
This is a set of points with variable width (as a ```FloatBuffer```), based on the current touch coordinates and timestamp. 
Additional processing can be applied to these points before adding them to the currently built path, but in this case they are simply fed back to the path buffer.

*PathUtils.getPhaseFromMotionEvent(event)* is a utility method that translates Android touch input into path building phases. 
Possible phases are: ```Phase.BEGIN, Phase.MOVE, Phase.END, Phase.UNKNOWN```.

For more information on ```MotionEvents```, see [http://developer.android.com/reference/android/view/MotionEvent.html](http://developer.android.com/reference/android/view/MotionEvent.html ).

### Step 7: Draw the strokes

Define a ```drawStroke(...)``` method to be responsible for the stroke composition: 

```java
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
```

This method is called on every touch event.

The most simplified stroke lifecycle can be described in three steps: 
a stroke beginning, stroke intermediate points rendering, and stroke finalization. 
In this case, stroke rendering begins on a ```MotionEvent.ACTION_MOVE``` event and ends on a ```MotionEvent.ACTION_UP``` event. 
The stroke's intermediate points are rendered on ```MotionEvent.ACTION_MOVE``` events.

The ```drawStroke(...)``` method above shows an example usage of the *StrokeRenderer* class, which provides a simplified mechanism to draw strokes on an *InkCanvas*. 
It draws the last built partial path points into an internally maintained layer of the *StrokeRenderer* instance using the ```strokeRenderer.drawPoints(...)``` method. 
Then the newly generated stroke part is blended into ```currentFrameLayer```.

### Step 8: Present the strokes on screen

Modify the ```renderView()``` method to overwrite *viewLayer* with the contents of *currentFrameLayer*, presenting the contents on screen:

```java
private void renderView() {
    inkCanvas.setTarget(viewLayer);
    inkCanvas.drawLayer(currentFrameLayer, BlendMode.BLENDMODE_OVERWRITE);
    inkCanvas.invalidate();
}
```

### Step 9: Dispose of the ```StrokeRenderer``` instance when no longer needed

Update the ```releaseResources()``` method to dispose of the *StrokeRenderer* instance when the OpenGL resources are released:

```java
private void releaseResources(){
    strokeRenderer.dispose();
    inkCanvas.dispose();
}
```

---
---
## Part 3: Smoothing paths

In Part 3 of this tutorial, you will create and configure a smoothener to smooth the path built in Part 2.

The WILL SDK Smoothing module smoothes data sequences stored in one or more channels using a technique based on double exponential smoothing. The result of the smoothing operation depends on the most recent sequence values. 

### Step 1: Create a '''MultiChannelSmoothener''' instance with three channels

A *MultiChannelSmoothener* instance can have one or more channels, with each channel responsible for smoothing a set of float values. 
The channel with index 0 smoothes the x-coordinate values and the channel with index 1 smoothes the y values. 
To cater for variable-width path generation, you must enable an additional channel (index 2) for the width values: 

```java
smoothener = new MultiChannelSmoothener(pathStride);
smoothener.enableChannel(2);
```

The ```pathStride``` parameter specifies the number of channels the *smoothener* instance will work with. A disabled channel must still contain data, but that data will not be smoothed.

### Step 2: Modify the path builder to smooth partial paths

Modify the ```buildPath(...)``` method from Part 2 to smooth the built partial paths before adding them to the path builder:

```java
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
    // Add the current input point to the path builder.
    FloatBuffer part = pathBuilder.addPoint(phase, event.getX(), event.getY(), event.getEventTime());
    SmoothingResult smoothingResult;
    int partSize = pathBuilder.getPathPartSize();

    if (partSize>0){
        // Smooth the returned control points (aka partial path).
        smoothingResult = smoothener.smooth(part, partSize, (phase==Phase.END));
        // Add the smoothed control points to the path builder.
        pathBuilder.addPathPart(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
    }
}
```

---
---
## Part 4: Drawing preliminary paths

There is a lag in stroke generation while the smoothing algorithm smoothes a stroke's partial paths. 
The *PathBuilder* class allows you to obtain a preliminary path which can be used to reduce the perceived latency introduced by the interpolation method or any additional processing applied by the client. 

In Part 4 of this tutorial, you will draw preliminary stroke curves, neutralizing any stroke generation lag.

### Step 1: Modify the path builder to build a preliminary path

Modify the ```buildPath(...)``` method to build and smooth a preliminary path during path composition:

```java
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
    // Add the current input point to the path builder.
    FloatBuffer part = pathBuilder.addPoint(phase, event.getX(), event.getY(), event.getEventTime());
    SmoothingResult smoothingResult;
    int partSize = pathBuilder.getPathPartSize();

    if (partSize>0){
        // Smooth the returned control points (aka partial path).
        smoothingResult = smoothener.smooth(part, partSize, (phase==Phase.END));
        // Add the smoothed control points to the path builder.
        pathBuilder.addPathPart(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
    }

    // Create a preliminary path.
    FloatBuffer preliminaryPath = pathBuilder.createPreliminaryPath();
    // Smoothen the preliminary path's control points (return inform of a partial path).
    smoothingResult = smoothener.smooth(preliminaryPath, pathBuilder.getPreliminaryPathSize(), true);
    // Add the smoothed preliminary path to the path builder.
    pathBuilder.finishPreliminaryPath(smoothingResult.getSmoothedPoints(), smoothingResult.getSize());
}
```

You can work with the preliminary path as you would with a normal path. 
The preliminary path is used to address any lag in rendering. It is a partial path that is created, smoothed, and displayed during the rendering, but never persisted.

### Step 2: Draw the preliminary curve

To draw the preliminary curve calculated in ```buildPath(...)```, modify the ```drawStroke(...)``` method to call the ```strokeRenderer.drawPrelimPoints(...)``` method:

```java
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
```

The ```strokeRenderer.drawPrelimPoints(...)``` method draws a set of points in the form of a preliminary curve into an internal layer, maintained by the strokeRenderer instance. 
This layer contains a copy of the unblended dirty area from the last-drawn stroke and the current preliminary points.

---
---
## Part 5: Using a particle brush

Up to this point in the tutorial, you have created solid color strokes. 
In Part 5, you will use an alternative rendering method in WILL SDK to create a textured effect. 
This method draws a large number of small textures scattered along the stroke trajectory. 
It is much more computationally expensive than generating a solid color stroke.
 
### Step 1: Configure the path builder to support variable stroke opacity

Configure *pathBuilder* to produce alpha values for the opacity at each control point. 
To do this, add a property configuration for stroke opacity in the same way you added a property configuration for stroke width in Part 2.

```java
pathBuilder.setPropertyConfig(PropertyName.Alpha, 0.05f, 0.4f, 0.05f, 0.4f, PropertyFunction.Power, 1.0f, false);
```

*pathBuilder* now produces an alpha property for each control point. 
The value is based on the min/max and initial/final alpha values, and the property dynamics function and its parameter.

**Note:** *pathBuilder* is now configured to produce both width and alpha values. This means that the stride of paths is 4, and the following properties are generated for each point: x, y, width, and alpha.

### Step 2: Create a particle brush
Create a *ParticleBrush* instance to draw a large number of small images along the path:

```java
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
```

This *ParticleBrush* instance draws the shapeTexture bitmap along the path, then fills the stroke by repeating the fillTexture bitmap. 
Images are separated by 15% (0.15) of their width and spread out randomly (set by the ```setSpacing(...)``` method). 
The shapeTexture bitmap undergoes a random rotation each time it is drawn.

**Note:** The ```allocateTextures(...)``` method must be called from the GL thread (in this case, the UI thread), and each *ParticleBrush* instance is responsible for managing its own OpenGL resources. 
The instance should be disposed of on finalization so that it can be safely garbage collected.

### Step 3: Replace the solid color brush with the particle brush

Replace the solid color brush from Part 4 with the new particle brush, and configure the *StrokePaint* instance to support strokes with variable alpha:

```java
paint.setStrokeBrush(brush);    // Particle brush.
paint.setAlpha(Float.NaN);      // Expected variable alpha.
```

### Step 4: Modify the path smoothener to support opacity

Enable the third channel of the *MultiChannelSmoothener* instance (called smoothener):


```java
smoothener.enableChannel(3);
```
The third channel will be used for the smoothed alpha values.

### Step 5: Update the drawStroke() method to support the new stroke

Configure the *StrokeRenderer* instance for the new stroke before it starts drawing a new stroke on a ```MotionEvent.ACTION_DOWN``` event:

```java
private void drawStroke(MotionEvent event){
    if (event.getAction()==MotionEvent.ACTION_DOWN){
        strokeRenderer.setUseSeed(true);
        strokeRenderer.setSeed((int) System.currentTimeMillis());
    }
    ....
}
```

### Step 6: Dispose of the brush

Update the ```releaseResources()``` method to dispose of the brush when the OpenGL resources are released:

```java
private void releaseResources(){
    brush.dispose();
    strokeRenderer.dispose();
    inkCanvas.dispose();
}
```

---
---
## Part 6: Drawing semi-transparent strokes

Up to this point in the tutorial, you have only drawn completely opaque strokes that are a single color. 
In Part 6, you will use the *StrokeRenderer* class to draw semi-transparent strokes and blend them together.
 
**Note:** Part 6 continues on from Part 4 of Tutorial 1: Drawing with touch.

### Step 1: Define an array with three colours

You will use an array with three colours to draw the generated strokes. Define this array:

```java
private int colors[] = {
        0x77ff0000, //red with 0x77 alpha
        0x8800ff00, //green with 0x88 alpha
        0x990000ff  //blue with 0x99 alpha
        };
```

### Step 2: Modify the path builder to support wider and narrower paths

Change the configuration of *pathBuilder* to increase the minimum and maximum width values of the generated paths:

```java
pathBuilder.setPropertyConfig(PropertyName.Width, 10f, 40f, Float.NaN, Float.NaN, PropertyFunction.Power, 1.0f, false);
```

### Step 3: Update the stroke color at the start of a new stroke

To update the color of the *StrokeRenderer* at the start of a new stroke, add the following code snippet at the beginning of the ```drawStroke(...)``` method:

```java
    private void drawStroke(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            paint.setColor(colors[step]);
            strokeRenderer.setStrokePaint(paint);
            step++;
            if (step==colors.length){
                step = 0;
            }
        }
        ......
    }
```

---
---
## Part 7: Calculating stroke boundary

In this part we will show how to translate the stroke geometry from Catmull-Rom spline with variable width to a collection of cubic Bezier curves that fit the stroke outline. 
Calculating stroke boundary is very useful when you want to render a stroke using traditional 2D graphics APIs or you want to export a stroke to PDF / SVG file.
 
This part continues from Part4 of the Tutorial 01 - Drawing with touch.

First, we define the necessary building elements and initialize the ```onCreate``` method:

```java
BoundaryBuilder instance is used to calculate the boundary of the stroke;
boundaryPaths is ArrayList<Path> instance that holds the boundary of all drawn strokes. Each stroke boundary is expressed as Path instance;
BoundaryView instance is utility class used to visualize stroke boundary using Android Canvas.
    private BoundaryBuilder boundaryBuilder;
    private ArrayList<Path> boundaryPaths;
    private BoundaryView boundaryView;
    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        boundaryBuilder = new BoundaryBuilder();
        boundaryPaths = new ArrayList<>();
        boundaryView = new BoundaryView(this);
        ((RelativeLayout)findViewById(R.id.contentview)).addView(boundaryView,
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
    }
```

The next step is to calculate the stroke boundary after a stroke is created. 
This is done by adding the following code to ```buildPath``` method:

```java
    private void buildPath(MotionEvent event){
        ...

        if(event.getAction() == MotionEvent.ACTION_UP) {
            boundaryBuilder.addPath(pathBuilder.getPathBuffer(), pathBuilder.getPathSize(), pathBuilder.getStride());
            Boundary boundary = boundaryBuilder.getBoundary();
            boundaryPaths.add(boundary.createPath());
            boundaryView.invalidate();
        }
    }
```
```Boundary``` class is used to encapsulate stroke boundary. 
As it is illustrated in the example, the main usage of ```Boundary``` class is to create a ```Path``` instance from it. 
However, ```Boundary``` class can be also used to iterate through the control points of the ```Boundary```, which can not be done via ```Path``` instance. 
This is why ```Boundary``` class implements ```Iterable<>``` interface. 
Below you can find a sample code that shows how to iterate through ```Boundary``` instance:

```java
    for(Iterable<PointF[]> subPath : boundary) {
        for(PointF[] curve : subPath) {
            // curve[0] -> first control point of cubic Bezier segment
            // curve[1] -> second control point of cubic Bezier segment
            // curve[2] -> third control point of cubic Bezier segment
            // curve[3] -> fourth control point of cubic Bezier segment
        }
    }
```

Finally we need to visualize stroke boundary. 
We will do this by creating a custom view as inner class. 
```BoundaryView``` overrides ```onDraw``` method and draws the boundaries of all strokes created by the user:

```java
    private class BoundaryView extends View {
        private Paint paint;

        public BoundaryView(Context context) {
            super(context);

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for(Path path : boundaryPaths) {
                canvas.drawPath(path, paint);
            }
        }
    }
```

The strokes will be rendered in red color, on the top of OpenGL surface used for the real-time WILL rendering.

---
---
