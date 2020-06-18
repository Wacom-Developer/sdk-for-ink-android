[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline.base](../index.md) / [DataProcessor](index.md) / [add](./add.md)

# add

`fun add(phase: `[`Phase`](../../com.wacom.ink/-phase/index.md)`, addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>`

Inputs stroke data in the pipeline stage and processes it.

### Parameters

`phase` - Specifies the phase of the stroke.

`addition` - Newly added stroke data that must be processed and accumulated.

`prediction` - Predicted continuation of the stroke data. Can be used temporarily before the next data addition occurs.

**Return**
A tuple containing added and predicted data of the output type.

`protected abstract fun add(addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>`