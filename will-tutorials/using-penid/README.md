# Tutorial 6: Using Pen Ids

This tutorial demonstrates how to obtain a unique identifier for a pen and use it in an application. The tutorial is divided into the following parts: 

* [Part 1: Obtaining Pen Identifiers](#part-1-obtaining-pen-identifiers)

## Prerequisites

This tutorial continues on from Part 4 of Tutorial 1: Drawing with touch.

## Source code

You can find the tutorial source code in the following location:
```Android Studio: /sdk/will-tutorials```

---
---
## Part 1: Obtaining Pen Identifiers

Pen Id is a 64 bit number that uniqely identifies a pen device. 
Knowing which pen is used in the generation of a particular stroke allows for various features to be added to an ink-enabled application. 
For example you can assign different settings for different pen devices, thus letting the users change the style of a stroke just by switching the pen they use.

In this tutorial we will assign a random color to each pen device that is used using the ```PenRecognizer``` class.

### Step 1: Create a hash map to store the ids

Create a ```HashMap``` that maps pen identifiers to the corresponding randomly generated colors.

```java
    private HashMap<Long,Integer> penIdsMap;
    ...
    penIdsMap = new HashMap<Long, Integer>();
```
    
### Step 2: Detect the pen identifier

Using the ```PenRecognizer.getPenId(...)``` method of the WILL SDK modify the ```drawStroke(...)``` method to detect the pen id of the current pen. 
When a new stroke is started, obtain the pen id of the pen and retrieve the color from the hash map if such is already available for this pen id, or otherwise add it to the map. 
Use this color to update the paint instance of the *strokeRenderer*.

```java
private void drawStroke(MotionEvent event){
    if (event.getAction()==MotionEvent.ACTION_DOWN){
        //the first touch of the new stroke
        long penId = PenRecognizer.getPenId(event);
        if (!penIdsMap.containsKey(penId)){
            int color = Color.argb(155 + (int)(Math.random()*100), (int)(Math.random()*150), (int)(Math.random()*150), (int)(Math.random()*150));
            penIdsMap.put(penId, color);
        }
        strokeRenderer.getStrokePaint().setColor(penIdsMap.get(penId));
    }
    ...
```
    
Note that the ```PenRecognizer.getPenId(...)``` method will return ```PenRecognizer.NO_ID``` in cases when the Id cannot be obtained.

---
---
