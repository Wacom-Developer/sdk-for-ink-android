[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [ConvexHullChainProducer](./index.md)

# ConvexHullChainProducer

`class ConvexHullChainProducer : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>>`

Computes the convex hull of each pair of successive brush samples.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a new instance of the ConvexHullChainProducer type.`ConvexHullChainProducer(keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>?, prediction: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>?>` |
| [reset](reset.md) | Resets the ConvexHullChainProducer to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
