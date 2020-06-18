[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline.base](../index.md) / [DataProcessor](./index.md)

# DataProcessor

`abstract class DataProcessor<Input, Output>`

An abstract base class for pipeline stages that process data based on a single input item.

### Parameters

`Input` - The type of the input data.

`Output` - The type of the output data.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Default constructor.`DataProcessor(allData: Output? = null, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, phase: `[`Phase`](../../com.wacom.ink/-phase/index.md)` = Phase.END)` |

### Properties

| Name | Summary |
|---|---|
| [allData](all-data.md) | All data accumulated in the pipeline stage.`open var allData: Output?` |
| [keepAllData](keep-all-data.md) | `var keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [phase](phase.md) | The phase of the input item.`open var phase: `[`Phase`](../../com.wacom.ink/-phase/index.md) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Inputs stroke data in the pipeline stage and processes it.`fun add(phase: `[`Phase`](../../com.wacom.ink/-phase/index.md)`, addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>``abstract fun add(addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>` |
| [reset](reset.md) | Resets the processor to its initial state.`abstract fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [PathProducer](../../com.wacom.ink.pipeline/-path-producer/index.md) | A data processor that converts pointer input data to path points. Provided with [PointerData](../../com.wacom.ink/-pointer-data/index.md) objects (x, y, timestamp, pressure, tilt, etc.) Produces [PathPoint](../../com.wacom.ink/-path-point/index.md): sequence of x, y, size, scaleX, scaleY, rotation etc. Configured with layout definition (list of geometry properties) and a calculator function that returns the geometry based on the touch data.`class PathProducer : `[`DataProcessor`](./index.md)`<`[`PointerData`](../../com.wacom.ink/-pointer-data/index.md)`, `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`>` |
