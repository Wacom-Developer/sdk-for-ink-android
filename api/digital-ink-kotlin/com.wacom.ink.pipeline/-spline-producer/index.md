[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [SplineProducer](./index.md)

# SplineProducer

`class SplineProducer : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`, `[`Spline`](../../com.wacom.ink/-spline/index.md)`>`

Catmull Rom Spline data processor.
Provided with a float vectors : for example a sequence of x and y.
Produces same kind of float vectors, representing a Catmull-Rom spline.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates an instance of the SplineProducer type.`SplineProducer(layout: `[`PathPointLayout`](../../com.wacom.ink/-path-point-layout/index.md)`, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [layout](layout.md) | The path point layout object that specifies the available properties of a path point.`var layout: `[`PathPointLayout`](../../com.wacom.ink/-path-point-layout/index.md) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`?, prediction: `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`Spline`](../../com.wacom.ink/-spline/index.md)`?>` |
| [reset](reset.md) | Resets the spline producer to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
