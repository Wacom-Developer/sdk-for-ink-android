[digital-ink-kotlin](../../index.md) / [com.wacom.ink.manipulation](../index.md) / [SpatialModel](index.md) / [getStrokes](./get-strokes.md)

# getStrokes

`fun getStrokes(): `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`InkStroke`](../../com.wacom.ink.model/-ink-stroke/index.md)`>`

Get all the strokes that are currently available in the model.

**Return**
HashMap&lt;String, InkStroke&gt; a map of all strokes with their ids

`fun getStrokes(rect: `[`Rect`](../../com.wacom.ink.math/-rect/index.md)`): `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Get all stroke in a given rect.

### Parameters

`rect` - Rect The rect

**Return**
ArrayList The ids of the strokes

