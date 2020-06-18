[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [SensorChannelsContext](index.md) / [createDefault](./create-default.md)

# createDefault

`fun createDefault(inkInputProviderID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, inputDeviceID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, latency: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, samplingRateHint: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SensorChannelsContext`](index.md)

Creates a SensorChannelsContext that contains a predefined combination of channel types that is suitable for a default SensorChannelsContext.

### Parameters

`inkInputProviderID` - String The ID of an ink input provider that produces the ink

`inputDeviceID` - String The ID of a device that captures the ink

`latency` - Int? Optional latency measurement in milliseconds

`samplingRateHint` - Int? Optional hint for the intended sampling rate of the sensor

**Return**
SensorChannelsContext A newly created SensorChannelsContext

