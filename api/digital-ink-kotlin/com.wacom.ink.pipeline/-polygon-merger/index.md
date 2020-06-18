[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [PolygonMerger](./index.md)

# PolygonMerger

`class PolygonMerger : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>>`

Computes the union of a list of polygons. The result is a polygon with zero or more internal contours (holes).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a new instance of the PolygonMerger type.`PolygonMerger(keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [polygonBounds](polygon-bounds.md) | The bounds of the polygons to be merged. If not set the bounds will be computed automatically.`var polygonBounds: `[`Bounds`](../../com.wacom.ink.utils/-bounds/index.md)`?` |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?, prediction: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?>` |
| [reset](reset.md) | Resets the polygon merger to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
