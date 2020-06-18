[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [SensorChannelsContext](./index.md)

# SensorChannelsContext

`class SensorChannelsContext : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)

Represents a collection of sensor channels along with associated properties for the sensor.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new SensorChannelsContext instance with the specified parameters.`SensorChannelsContext(inputProviderID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, inputDeviceID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, sensorChannels: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SensorChannel`](../-sensor-channel/index.md)`>? = null, samplingRateHint: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, latency: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [inputDeviceID](input-device-i-d.md) | String The ID of the device that captures the ink.`val inputDeviceID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [inputProviderID](input-provider-i-d.md) | String The ID of the ink input provider that produces the ink.`val inputProviderID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [latency](latency.md) | Int? Gets the latency measurement in milliseconds.`var latency: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [samplingRateHint](sampling-rate-hint.md) | Int? Hint for the intended sampling rate of the sensor.`var samplingRateHint: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Add a sensor channel.`fun add(sensorChannel: `[`SensorChannel`](../-sensor-channel/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAll](get-all.md) | Get all sensor channels in the context.`fun getAll(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SensorChannel`](../-sensor-channel/index.md)`>` |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [createDefault](create-default.md) | Creates a SensorChannelsContext that contains a predefined combination of channel types that is suitable for a default SensorChannelsContext.`fun createDefault(inkInputProviderID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, inputDeviceID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, latency: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, samplingRateHint: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SensorChannelsContext`](./index.md) |
