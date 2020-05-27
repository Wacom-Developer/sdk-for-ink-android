# Tutorial 4: Selecting strokes

In this tutorial, we demonstrate how to make changes in a collection of strokes. 
First, we implement a selection tool to select whole strokes; next, we use the WILL *Manipulation* module to remove and redraw parts of strokes. 

The tutorial is divided into the following parts: 

* [Part 1 :Selecting whole strokes](#part-1-selecting-whole-strokes)
* [Part 2: Selecting parts of strokes](#part-2-selecting-parts-of-strokes)

Each part builds on the previous one, extending and improving its functionality.

## Prerequisites

This tutorial continues on from Part 1 of Tutorial 3: Erasing strokes.

## Source code

You can find the tutorial source code in the following location:
```Android Studio: /sdk/will-tutorials```

---
---
## Part 1: Selecting whole strokes

In this tutorial, you will modify the path builder to select whole strokes and parts of strokes. This tutorial builds on Part 1 or Part 2 of Tutorial 3: Erasing strokes.

In Part 1 of this tutorial, you will select whole strokes and change their colors.

### Step 1: Modify the path builder to draw a stroke of constant width

To define a path builder configuration for a stroke with a constant width, remove the following line:

```java
pathBuilder.setPropertyConfig(PropertyName.Width, 30f, 40f, Float.NaN, Float.NaN, PropertyFunction.Power, 1.0f, false);
```

This allows you to create a stroke with constant width. The pathBuilder works with stride 2 (only x and y coordinates).

### Step 2: Modify the paint instance to set a constant width

Modify the paint instance:

```java
paint.setWidth(2.0f);
```

### Step 3: Select a stroke

Create an instance of the *Intersector* class, as described in Part 2 of Tutorial 3: Erasing strokes. 
This time, use the ```setTargetAsClosedPath(...)``` method of the *Intersector* to set the target of the intersection to be the area enclosed by the whole path calculated by the pathBuilder.

**Note:** The width of the path is ignored.

### Step 4: Change the color of the selected stroke

When the selection is complete (the path is finished), call the ```isIntersectingTarget(...)``` method, check each stroke for intersection with the target, and set the stroke color as follows:

* If the current stroke color is not RED, change the color to RED.
* If the current stroke color is RED, change the color to BLUE.
* If the stroke does not intersect the target, leave the stroke color unchanged.

The code for this looks like:

```java
public boolean onTouch(View v, MotionEvent event) {
    boolean bFinished = buildPath(event);

    if (bFinished){
        if (strokesList.size()>0) {
            intersector.setTargetAsClosedPath(pathBuilder.getPathBuffer(), 0, pathBuilder.getPathSize(), pathStride);
            for (Stroke stroke: strokesList){
                if (intersector.isIntersectingTarget(stroke)){
                    if (stroke.getColor()==Color.RED){
                        stroke.setColor(Color.BLUE);
                    } else {
                        stroke.setColor(Color.RED);
                    }
                }
            }
            drawStrokes();
        }
    } else {
        drawStroke(event);
    }
    renderView();
    return true;
}
```

---
---
## Part 2: Selecting parts of strokes

In Part 1 of this tutorial, you selected whole strokes; in Part 2, you will select parts of strokes inside a selection area.

### Step 1: Find intersections with strokes

Use the ```intersectWithTarget(...)``` method to find intersections with drawn paths. 
This method returns an *IntersectionResult* instance (intersection) that holds the number of the intervals produced by the intersection and an *Intersector.IntervalIterator* containing the actual intervals. 
Each interval of the path is either totally inside or totally outside the intersection target. 
(For comparison, see Part 3 of Tutorial 3: Erasing strokes.)

### Step 2: Remove the original strokes and create new strokes for every interval

If intervals are inside the target, change their color to RED. If intervals are outside the target, do not change their color.

The code for this looks like:

```java
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
    } else {
        drawStroke(event);
    }
    renderView();
    return true;
}      
```

---


