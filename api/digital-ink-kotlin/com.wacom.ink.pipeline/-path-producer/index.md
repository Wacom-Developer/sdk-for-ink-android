[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [PathProducer](./index.md)

# PathProducer

`class PathProducer : `[`DataProcessor`](../../com.wacom.ink.pipeline.base/-data-processor/index.md)`<`[`PointerData`](../../com.wacom.ink/-pointer-data/index.md)`, `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`>`

A data processor that converts pointer input data to path points.
Provided with [PointerData](../../com.wacom.ink/-pointer-data/index.md) objects (x, y, timestamp, pressure, tilt, etc.)
Produces [PathPoint](../../com.wacom.ink/-path-point/index.md): sequence of x, y, size, scaleX, scaleY, rotation etc.
Configured with layout definition (list of geometry properties) and a calculator function that returns the geometry based on the touch data.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates an instance of the PathProducer type.`PathProducer(layout: `[`PathPointLayout`](../../com.wacom.ink/-path-point-layout/index.md)`, pathPointCalculator: `[`Calculator`](../../com.wacom.ink/-calculator.md)`, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [layout](layout.md) | The path point layout object that specifies the available properties of a path point.`var layout: `[`PathPointLayout`](../../com.wacom.ink/-path-point-layout/index.md) |
| [pathPointCalculator](path-point-calculator.md) | The [Calculator](../../com.wacom.ink/-calculator.md) lambda assigned to this path producer. The [Calculator](../../com.wacom.ink/-calculator.md) lambda method calculates the path point properties based on pointer input.`var pathPointCalculator: `[`Calculator`](../../com.wacom.ink/-calculator.md) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`PointerData`](../../com.wacom.ink/-pointer-data/index.md)`?, prediction: `[`PointerData`](../../com.wacom.ink/-pointer-data/index.md)`?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`?>` |
| [reset](reset.md) | Resets the PathProducer to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
