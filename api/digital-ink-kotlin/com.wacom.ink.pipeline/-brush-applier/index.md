[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [BrushApplier](./index.md)

# BrushApplier

`class BrushApplier : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>>`

For each sample of the interpolated spline the BrushApplier creates a transformation based on the sample data and transforms the brush prototype.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a new instance of the BrushApplier type.`BrushApplier(brush: `[`VectorBrush`](../../com.wacom.ink.rendering/-vector-brush/index.md)`, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [brush](brush.md) | The vector brush`var brush: `[`VectorBrush`](../../com.wacom.ink.rendering/-vector-brush/index.md) |
| [defaultOffset](default-offset.md) | Constant offset values that are used when the layout does not include variable offset properties.`var defaultOffset: `[`Triple`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-triple/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>` |
| [defaultRotation](default-rotation.md) | A constant rotation value that is used when the layout does not include variable rotation property.`var defaultRotation: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [defaultScale](default-scale.md) | Constant scale values that are used when the layout does not include variable scale properties.`var defaultScale: `[`Triple`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-triple/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>` |
| [defaultSize](default-size.md) | A constant size value that is used when the layout does not include variable size property.`var defaultSize: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`?, prediction: `[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>?>` |
| [reset](reset.md) | Resets the brush applier to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [centerAndFit](center-and-fit.md) | `fun centerAndFit(poly: `[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`): `[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md) |
| [createCirclePolygon](create-circle-polygon.md) | `fun createCirclePolygon(vertexCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md) |
| [createPolygonFromPointsClound](create-polygon-from-points-clound.md) | `fun createPolygonFromPointsClound(point: `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`): `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md) |
