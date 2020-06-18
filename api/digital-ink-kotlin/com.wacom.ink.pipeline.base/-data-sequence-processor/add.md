[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline.base](../index.md) / [DataSequenceProcessor](index.md) / [add](./add.md)

# add

`fun add(isFirst: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, isLast: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>`

Inputs stroke data to the pipeline stage and processes it.

### Parameters

`isFirst` - True if the data contains the start of the stroke.

`isLast` - True if the data contains the end of the stroke.

`addition` - Newly added stroke data that must be processed and accumulated.

`prediction` - Predicted continuation of the stroke data. Can be used temporarily before the next data addition occurs.

**Return**
A tuple containing added and predicted data of the output type.

`protected abstract fun add(addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>`