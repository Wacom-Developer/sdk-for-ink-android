[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [SmoothingFilter](./index.md)

# SmoothingFilter

`class SmoothingFilter : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`, `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`>`

Smoothening filter for path values.
Each point is a liner combination of the last several points. Coefficients are produced using multi-pass double exponential smoothing.
The default value of movingAverageWindowSize is 15, which is tuned for hand writing data (sequence of xy positions) at 60Hz. If the input is for example 120Hz, set movingAverageWindowSize to 30.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates an instance of the Smoother type.`SmoothingFilter(dimsCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, movingAverageWindowSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 15, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [dimsCount](dims-count.md) | The number of dimensions (properties) of the processed path points.`var dimsCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [movingAverageWindowSize](moving-average-window-size.md) | Window size of the smoothing filter.`var movingAverageWindowSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`?, prediction: `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`?>` |
| [reset](reset.md) | Resets the Smoother to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
