# Tutorial 2: Encoding and decoding strokes

In this tutorial, we demonstrate how to use WILL SDK to encode strokes into compressed binary data, and how to reconstruct strokes from encoded data. 
The tutorial is divided into the following parts:

* [Part 1: Creating a stroke model](#part-1-creating-a-stroke-model)
* [Part 2: Serializing and deserializing strokes](#part-2-serializing-and-deserializing-strokes)
* [Part 3: Working with the WILL file format](#part-3-working-with-the-will-file-format)

Each part builds on the previous one, extending and improving its functionality.

## Prerequisites
This tutorial continues on from Part 4 of Tutorial 1: Drawing with touch.

## Source code
You can find the tutorial source code in the following location:
```Android Studio: /sdk/will-tutorials```

---
---
## Part 1: Creating a stroke model

In Tutorial 1: Drawing with touch, you built paths and drew them as strokes, but you did not define a stroke model - you didn't actually need one. 

In this tutorial, you will define a stroke model so that you can serialize and deserialize strokes.

In Part 1 of this tutorial, you will create a model to hold stroke data.

### Step 1: Define a stroke model

Define a class called ```Stroke``` to represent the model:

```java
public class Stroke{

    private FloatBuffer points;
    private int color;
    private int stride;
    private int size;
    private float width;
    private float startT;
    private float endT;
    private BlendMode blendMode;
...

}
```

Using this class, a stroke has the following properties: 
* **_points_**: A native *FloatBuffer*, holding the control points of the path. Depending on the stroke type, you can define each control point using one of the following value sets: 
  * x and y
  * x,y, and Width
  * x, y, Width, and Alpha 

* **_stride_**: The offset from one control point to the next. Possible values are:
  * 2 for control points defined by x and y coordinates
  * 3 for control points defined by x and y coordinates and a Width value
  * 4 for control points defined by x and y coordinates, a Width value, and an Alpha value

* **_width_**: The width of the stroke. If the control points include a Width property value (stride=3 or stride=4), this property should be NaN - this indicates that the width values are being stored in the points buffer itself.

* **_startT_**: The starting value for the Catmull-Rom spline parameter of the path (the default value is 0). 

* **_endT_**: The final value for the Catmull-Rom spline parameter of the path (the default value is 1).

* **_blendMode_**: When the path is being drawn as a stroke, this property specifies how to blend this path with already-drawn strokes.

**Note:** The ```Stroke``` class has a number of getter and setter methods. See the sample code for full details.

### Step 2: Create a list to store strokes

Create a list to store strokes:

```java
LinkedList<Stroke> strokesList = new LinkedList<Stroke>();
```

### Step 3: Store finished strokes in the strokes list

Modify the ```buildPath(...)``` method to return ```true``` when a stroke is finished, and update the touch input processing to add finished strokes to the ```strokesList```:

```java
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
        Toast.makeText(StrokeEncodingDecodingPart1.this, "We have " + strokesList.size() + " strokes in the list.", Toast.LENGTH_SHORT).show();
    }
    return true;
}
```

---
---
## Part 2: Serializing and deserializing strokes

The WILL SDK *Serialization* module provides the classes *InkEncoder* and *InkDecoder*:

* The *InkEncoder* class creates a compressed binary representation of a single stroke.
* The *InkDecoder* class reads this binary data and recreates the stroke.

For both classes, coordinates, width values, and other stroke properties are encoded in a binary form using Google protocol buffers. 
This allows rapid encoding and decoding, and the resulting binary code is highly portable and compact because the techniques used for representing repeated stroke values greatly improve the efficiency of the varint technique used.

In Part 2 of this tutorial, you will create a ```StrokeSerializer``` class to serialize and deserialize lists of strokes using WILL SDK.

### Step 1: Define the encoding process

Create a ```StrokeSerializer``` class with a ```serialize(...)``` method for the encoding process. It should:

* Create an *InkEncoder* instance.
* Call the ```encodePath(...)``` method for each stroke in the list. 
* Call the ```getEncodedData()``` method to retrieve a ```ByteBuffer``` containing the compressed binary representation of all strokes. 
  The ```ByteBuffer``` is encoded with the ```encodePath(...)``` method.
* Call the ```getEncodedDataSizeInBytes()``` method to check how many bytes the encoded strokes occupy.
* Save the encoded strokes to a binary file using the utility method ```Utils.saveBinaryFile(...)```.

The code for the ```serialize(...)``` method looks like: 

```java
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
```

### Step 2: Serialize and save a list of strokes

Create a ```StrokeSerializer``` instance named serializer, and override the ```onSaveInstanceState(...)``` method of the activity to serialize and save a list of strokes:

```java
@Override
protected void onSaveInstanceState(Bundle outState) {
    serializer.serialize(Uri.fromFile(getFileStreamPath("will.bin")), strokesList);
    super.onSaveInstanceState(outState);
}
```

### Step 3: Define the decoding process

Update the ```StrokeSerializer``` class with a ```deserialize(...)``` method to:

* Create an *InkDecoder* instance.
* Load the encoded strokes from a binary file into a buffer using the ```loadBinaryFile(...)``` utility method.
* Call the ```decodeNextPath(...)``` method while it returns ```true```. 
  This method returns ```false``` when there are no more paths. 
* Recreate each stroke using the following methods: ```getDecodedPathSize(), getDecodedPathIntColor(), getDecodedPathStride(), getDecodedPathTs(), getDecodedPathTf(), getDecodedPathWidth()```.

The code looks like:

```java
public LinkedList<Stroke> deserialize(Uri uri) {
    ByteBuffer buffer = Utils.loadBinaryFile(uri);
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

        result.add(stroke);
    }
    return result;
}
```

### Step 4: Add deserialized strokes to the strokes list

To load the strokes and fill out the *strokesList* with ```Stroke``` instances, add the following lines to the end of the ```surfaceChanged()``` method:

```java
strokesList = serializer.deserialize(Uri.fromFile(getFileStreamPath("will.bin")));

drawStrokes(strokesList);
renderView();
```
Define the ```drawStrokes``` method to blend each stroke in the strokesLayer, then draw the strokesLayer to the currentFrameLayer:

```java
private void drawStrokes() {
    inkCanvas.setTarget(strokesLayer);
    inkCanvas.clearColor();

    for (Stroke stroke: strokesList){
        paint.setColor(stroke.getColor());
        strokeRenderer.setStrokePaint(paint);
        strokeRenderer.drawPoints(stroke.getPoints(), 0, stroke.getSize(), stroke.getStartValue(), stroke.getEndValue(), true);
        strokeRenderer.blendStroke(strokesLayer, BlendMode.BLENDMODE_NORMAL);
    }

    inkCanvas.setTarget(currentFrameLayer);
    inkCanvas.clearColor(Color.WHITE);
    inkCanvas.drawLayer(strokesLayer, BlendMode.BLENDMODE_NORMAL);
}
```

---
---
## Part 3: Working with the WILL file format

In Part 2, you serialized strokes using the *InkEncoder* and *InkDecoder* classes. 
In Part 3 of this tutorial, you will save to and read from files in the WILL format.

The WILL SDK Serialization module includes the *WILLWriter* and *WILLReader* classes, which provide implementations to read and write document encoded in WILL Format.

First, rename the ```saveStrokes()``` method to ```saveWillFile()``` and ```loadStrokes()``` to ```loadWillFile()```.

### Step 1: Write strokes to a file

Modify the ```saveWillFile()``` method to do the following:

* Store the data for each stroke in an *InkPathData* instance and add the instance to a list.
* Create a ```WillDocumentFactory``` instance and use it to create a ```WillDocument``` instance. 
  Provide the application's cache directory as working directory in the factory's constuctor.
* Provide core and extended properties.
* Create a ```Section``` instance.
* Create a ```Paths``` element and add it to the section.
* Add the list of the digital ink paths (the list from point 1) to the ```Paths``` element.
* Add the section to the document.
* Pass the document instance to the ```WILLWriter.write()``` method, which stores the document in WILL Format, encoding the strokes using the *InkEncoder* class, and then creates a file called sample.will.
* Recycle the document to release allocated resources.

```java
private void saveWillFile(){
    File willFile = new File(Environment.getExternalStorageDirectory() + "/sample.will"); 

    LinkedList<InkPathData> inkPathsDataList = new LinkedList<InkPathData>();       
    for (Stroke stroke: strokesList){
        InkPathData inkPathData = new InkPathData(
                stroke.getPoints(), 
                stroke.getSize(), 
                stroke.getStride(), 
                stroke.getWidth(), 
                stroke.getColor(), 
                stroke.getStartValue(), 
                stroke.getEndValue(), 
                stroke.getBlendMode(),
                stroke.getPaintIndex(),
                stroke.getSeed(),
                stroke.hasRandomSeed());
        inkPathsDataList.add(inkPathData);
    }

    WillDocumentFactory factory = new WillDocumentFactory(this, getCacheDir());
    try{
        WillDocument willDoc = factory.newDocument();

        willDoc.setCoreProperties(new CorePropertiesBuilder()
            .category("category")
            .created(new Date())
            .build());

        willDoc.setExtendedProperties(new ExtendedPropertiesBuilder()
            .template("light")
            .application("demo")
            .appVersion("0.0.1")
            .build());

        Section section = willDoc.createSection()
            .width(sceneWidth)
            .height(sceneHeight)
            .addChild(
                willDoc.createPaths(inkPathsDataList, 2));

        willDoc.addSection(section);

        new WILLWriter(willFile).write(willDoc);
        willDoc.recycle();
    } catch (WILLFormatException e){ 
        throw new WILLException("Can't write the sample.will file. Reason: " + e.getLocalizedMessage() + " / Check stacktrace in the console."); 
    }
}
```

### Step 2: Read strokes from a file

Update the ```loadWillFile()``` method to do the following:

* Load the sample.will file by creating a *WILLReader* instance and calling its ```read()``` method. 
  The ```read()``` method returns a ```WillDocument``` instance.
* Iterate over the sections of the document. 
* For each seaction find the Paths elements. Iterate over the digital ink paths, stored in each ```Paths``` element - use the ```pathsElement.getInkPaths()``` method.
* Create a stroke instance from each digital ink path data instance and add it to the strokesList of the activity.
* Recycle the document to release allocated resources.

```java
private void loadWillFile(){
    File willFile = new File(Environment.getExternalStorageDirectory() + "/sample.will"); 
    try {
        WILLReader reader = new WILLReader(new WillDocumentFactory(this, this.getCacheDir()), willFile);
        WillDocument doc = reader.read();
        for (Section section: doc.getSections()){
            ArrayList<BaseNode> pathsElements = section.findChildren(BaseNode.TYPE_PATHS);
            for (BaseNode node: pathsElements){
                Paths pathsElement = (Paths)node;
                for (InkPathData inkPath: pathsElement.getInkPaths()){
                    Stroke stroke = new Stroke();
                    stroke.copyPoints(inkPath.getPoints(), 0, inkPath.getSize());
                    stroke.setStride(inkPath.getStride());
                    stroke.setWidth(inkPath.getWidth());
                    stroke.setBlendMode(inkPath.getBlendMode());
                    stroke.setInterval(inkPath.getTs(), inkPath.getTf());
                    stroke.setColor(inkPath.getColor());
                    stroke.setPaintIndex(inkPath.getPaintIndex());
                    stroke.setSeed(inkPath.getRandomSeed());
                    stroke.setHasRandomSeed(inkPath.hasRandomSeed());
                    strokesList.add(stroke);
                }
            }
        }
        doc.recycle();
    } catch (WILLFormatException e) {
        throw new WILLException("Can't read the sample.will file. Reason: " + e.getLocalizedMessage() + " / Check stacktrace in the console."); 
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}
```
---
---

