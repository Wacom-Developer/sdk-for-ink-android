[digital-ink-kotlin](../../index.md) / [com.wacom.ink.manipulation](../index.md) / [SpatialModel](index.md) / [erase](./erase.md)

# erase

`fun erase(eraser: `[`Spline`](../../com.wacom.ink/-spline/index.md)`, brush: `[`VectorBrush`](../../com.wacom.ink.rendering/-vector-brush/index.md)`, manipulationMode: `[`ManipulationMode`](../-manipulation-mode/index.md)`, callback: `[`ErasingCallback`](../../com.wacom.ink.manipulation.callbacks/-erasing-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Erase strokes. The erasing may be one of two kinds - whole stroke or partial.
The eraser is provided in the form of a spline and the brush with which it is drawn.
In the case of the whole stroke erasing - if the eraser passes through a stroke it will be removed.
In the case of the partial erasing, the eraser will cut through the the splines, removing some of
them and producing new ones.

Via the callback you will be notified for these events:

* Added spline - The event will contain a spline and its ID.
* Removed splines - The event will contain the ID of the removed spline.

### Parameters

`eraser` - Spline The spline of the eraser

`brush` - VectorBrush The brush with which the spline is drawn

`wholeStrokes` - Boolean Whether to do a whole stroke erasing

`callback` - ErasingCallback The erasing events callback`fun erase(eraser: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>, manipulationMode: `[`ManipulationMode`](../-manipulation-mode/index.md)`, callback: `[`ErasingCallback`](../../com.wacom.ink.manipulation.callbacks/-erasing-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Erase strokes. The erasing may be one of two kinds - whole stroke or partial.
The eraser is provided in the form of a list of polygons. This method should be used with the result of the
ConvexHullChainProducer's add(...) method.
In the case of the whole stroke erasing - if the eraser passes through a stroke it will be removed.
In the case of the partial erasing, the eraser will cut through the the splines, removing some of
them and producing new ones.

Via the callback you will be notified for these events:

* Added spline - The event will contain a spline and its ID.
* Removed splines - The event will contain the ID of the removed spline.

### Parameters

`eraser` - ArrayList The convex hull chain of the eraser

`manipulationMode` - Boolean The manipulation mode

`callback` - ErasingCallback The erasing events callback