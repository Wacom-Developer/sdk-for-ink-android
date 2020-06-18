[digital-ink-kotlin](../../index.md) / [com.wacom.ink.manipulation](../index.md) / [SpatialModel](index.md) / [select](./select.md)

# select

`fun select(selector: `[`Spline`](../../com.wacom.ink/-spline/index.md)`, manipulationMode: `[`ManipulationMode`](../-manipulation-mode/index.md)`, callback: `[`SelectingCallback`](../../com.wacom.ink.manipulation.callbacks/-selecting-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Select strokes. The selecting may be one of two kinds - whole stroke or partial.
In both cases a closed polygon is produced from the selector spline.
In the case of the whole stroke selecting - if the selector polygon intersects a stroke it will be selected.
In the case of the partial selecting - the selector polygon will cut the splines, removing the old ones,
producing new and selecting those inside the selector.

Via the callback you will be notified for those events:

* Added spline - The event will contain a spline and its ID.
* Removed spline - The event will contain the ID of the removed spline.
* Selected spline - The event will contain the ID of the selected spline.

### Parameters

`selector` - Spline The selector spline

`manipulationMode` - Boolean The manipulation mode

`callback` - SelectingCallback The selecting events callback`fun select(selector: `[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`, manipulationMode: `[`ManipulationMode`](../-manipulation-mode/index.md)`, callback: `[`SelectingCallback`](../../com.wacom.ink.manipulation.callbacks/-selecting-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Select strokes. The selecting may be one of two kinds - whole stroke or partial.
In the case of the whole stroke selecting - if the selector polygon intersects a stroke it will be selected.
In the case of the partial selecting - the selector polygon will cut the splines, removing the old ones,
producing new and selecting those inside the selector.

Via the callback you will be notified for those events:

* Added spline - The event will contain a spline and its ID.
* Removed spline - The event will contain the ID of the removed spline.
* Selected spline - The event will contain the ID of the selected spline.

### Parameters

`selector` - Spline The selector polygon

`manipulationMode` - Boolean The manipulation mode

`callback` - SelectingCallback The selecting events callback