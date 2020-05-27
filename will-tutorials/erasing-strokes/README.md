# Tutorial 3: Erasing strokes

In this tutorial, we demonstrate how to detect strokes based on user input and how to erase strokes, or parts of strokes, using the WILL Manipulation module. 
The tutorial is divided into the following parts:

* [Part 1: Extending the stroke model](#part-1-extending-the-stroke-model)
* [Part 2: Creating an eraser](#part-2-creating-an-eraser)
* [Part 3: Erasing parts of strokes](#part-3-erasing-parts-of-strokes)

Each part builds on the previous one, extending and improving its functionality.

## Prerequisites
This tutorial continues on from Part 2 of Tutorial 2: Encoding and decoding strokes.

## Source code
You can find the tutorial source code in the following location:
```Android Studio: /sdk/will-tutorials```

---
---
## Part 1: Extending the stroke model

In Tutorial 2: Encoding and decoding strokes, you defined a stroke model. 
In this tutorial, you will extend the stroke model to facilitate stroke manipulation.

To manipulate strokes, WILL SDK requires data on the bounds of each individual stroke segment. 
A segment is defined as the curve between two successive control points. 
WILL SDK provides the Intersector class to calculate intersections of one stroke with another stroke, and intersections of a stroke with an area defined by a closed path. 
The Intersector works with stroke models that implement the Intersectable interface, so you will upgrade the stroke model to implement this interface.

The *Intersectable* interface requires you to implement the following methods: 
* ```getPoints()```
* ```getSize()```
* ```getStride()```
* ```getWidth()```
* ```getStartValue()```
* ```getEndValue()```
* ```getSegmentsBounds()```
* ```getBounds()```

The stroke model you defined in Tutorial 2: Encoding and decoding strokes implements all of these except ```getSegmentsBounds()``` and ```getBounds()```. 
In Part 1 of this tutorial, you will extend the stroke model to calculate and store stroke and segment bounds.

### Step 1: Update the stroke model to store bounds data

Add ```getSegmentsBounds()``` and ```getBounds()``` to the stroke model:

```java
    @Override
    public FloatBuffer getSegmentsBounds() {
        return segmentsBounds;
    }

    @Override
    public RectF getBounds() {
        return bounds;
    }
```

This allows you to check the stroke for intersections using the *Intersector* class.

### Step 2: Calculate stroke and segment bounds

Define a ```calculateBounds()``` method to calculate the total bounds of the stroke and the bounds of each segment. 

Store the bounds of each segment in a ```FloatBuffer``` called segmentsBounds. 
The *Intersector* class expects the data in this buffer to be represented in a series of top, left, width, and height float values for each segment.

To determine the exact number of segments, use the ```calculateSegmentsCount(...)``` utility method, provided by PathBuilder.

The code looks like:

```java
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
```
    
### Step 3: Calculate the bounds of deserialized strokes

Update the ```StrokeSerializer``` class to call the stroke's method ```calculateBounds()``` after deserialization.

---
---
## Part 2: Creating an eraser

In Part 2 of this tutorial, you will create an eraser path to erase strokes.

### Step 1: Add an eraser path to the path builder

Define a path-building configuration that produces a thicker path:

```java
pathBuilder.setPropertyConfig(PropertyName.Width, 30f, 40f, Float.NaN, Float.NaN, PropertyFunction.Power, 1.0f, false);
```

This will be used as an eraser tool.
Remove the ```drawStroke(...)``` method for this path, as you do not want to draw this path as a stroke.

### Step 2: Delete strokes that intersect with the eraser

Create an *Intersector* instance named intersector and set the target of the intersection to be the partial path calculated by the pathBuilder. 

For each stroke, check if it intersects the target. If it does, remove the stroke from the strokes list.

### Step 3: Redraw the remaining strokes

After checking all intersections, redraw the remaining strokes and present them to the screen.

The touch input processing logic now looks like:

```java
public boolean onTouch(View v, MotionEvent event) {
    buildPath(event);
    if (strokesList.size()>0) {
        intersector.setTargetAsStroke(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), pathStride);
        LinkedList<Stroke> removedStrokes = new LinkedList<Stroke>();
        for (Stroke stroke: strokesList){
            if (intersector.isIntersectingTarget(stroke)){
                removedStrokes.add(stroke);
            }
        }
        strokesList.removeAll(removedStrokes);
        drawStrokes();
        renderView();
    }
    return true;
}
```

---
---
## Part 3: Erasing parts of strokes

In Part 2 of this tutorial, you removed full strokes from the strokes list; in Part 3, you will remove parts of strokes.

### Step 1: Find intersections between the eraser and strokes

Use the ```intersectWithTarget(...)``` method to find intersections between the eraser and drawn paths. 
This method returns an *IntersectionResult* instance (intersection) that holds the number of intervals produced by the intersection, and an *Intersector.IntervalIterator* containing the actual intervals. 
Each interval of the path is either totally inside or totally outside the intersection target. 

**Note:** For performance reasons, each *Intersector* instance returns the same *IntersectionResult* each time the ```intersectWithTarget(...)``` method is called.

### Step 2: Redraw the strokes with erased parts

Remove the original strokes and create new strokes for every interval outside the target.

The touch input processing logic now looks like:

```java
public boolean onTouch(View v, MotionEvent event) {
    buildPath(event);

    if (strokesList.size()>0) {
        intersector.setTargetAsStroke(pathBuilder.getPathBuffer(), pathBuilder.getPathLastUpdatePosition(), pathBuilder.getAddedPointsSize(), pathStride);
        LinkedList<Stroke> removedStrokes = new LinkedList<Stroke>();
        LinkedList<Stroke> newStrokes = new LinkedList<Stroke>();

        for (Stroke stroke: strokesList){
            IntersectionResult intersection = intersector.intersectWithTarget(stroke);

            if (intersection.getCount()==1){
                Intersector.IntervalIterator iterator = intersection.getIterator();
                if (iterator.next().inside){
                    removedStrokes.add(stroke);
                }
            } else if (intersection.getCount()>1){
                removedStrokes.add(stroke);
                Intersector.IntervalIterator iterator = intersection.getIterator();
                while (iterator.hasNext()){
                    Intersector.Interval interval = iterator.next();
                    if (!interval.inside){
                        int size = interval.toIndex - interval.fromIndex + stroke.getStride();

                        Stroke newStroke = new Stroke(size);

                        newStroke.copyPoints(stroke.getPoints(), interval.fromIndex, size);
                        newStroke.setStride(stroke.getStride());
                        newStroke.setColor(stroke.getColor());
                        newStroke.setBlendMode(stroke.getBlendMode());
                        newStroke.setWidth(stroke.getWidth());
                        newStroke.setInterval(interval.fromValue, interval.toValue);
                        newStroke.calculateBounds();

                        newStrokes.add(newStroke);
                    }
                }
            }
        }

        strokesList.removeAll(removedStrokes);
        strokesList.addAll(newStrokes);

        drawStrokes();
        renderView();
    }
    return true;
}
```

---
---




