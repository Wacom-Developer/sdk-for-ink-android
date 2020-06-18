[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [PolygonSimplifier](./index.md)

# PolygonSimplifier

`class PolygonSimplifier : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>>`

Simplifies a polygon by reducing the number of its vertices.
Uses the Ramer-Douglas-Peucker algorithm: https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a new instance of the PolygonSimplifier type.`PolygonSimplifier(epsilon: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [epsilon](epsilon.md) | Threshold value that determines the level of simplification. Greater values result into polygons with less segments.`var epsilon: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?, prediction: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?>` |
| [reset](reset.md) | Resets the polygon simplifier to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
