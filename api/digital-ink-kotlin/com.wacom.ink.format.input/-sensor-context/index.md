[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [SensorContext](./index.md)

# SensorContext

`class SensorContext : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)

Represents a collection of sensor channel contexts, used for capturing digital ink input.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Represents a collection of sensor channel contexts, used for capturing digital ink input.`SensorContext()` |

### Properties

| Name | Summary |
|---|---|
| [defaultSensorChannelsContext](default-sensor-channels-context.md) | SensorChannelsContext? The default SensorChannelsContext`var defaultSensorChannelsContext: `[`SensorChannelsContext`](../-sensor-channels-context/index.md)`?` |
| [hasDefaultSensorChannelsContext](has-default-sensor-channels-context.md) | Boolean Whether a default SensorChannelsContext exists in the collection`val hasDefaultSensorChannelsContext: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [addSensorChannelsContext](add-sensor-channels-context.md) | Adds the specified SensorChannelsContext instance to the collection. The first SensorChannelsContext that contains all mandatory channel types becomes the default sensor channels context.`fun addSensorChannelsContext(sensorChannelsContext: `[`SensorChannelsContext`](../-sensor-channels-context/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [containsSensorChannelsContext](contains-sensor-channels-context.md) | Checks whether the specified sensor channel context already exists in the collection.`fun containsSensorChannelsContext(sensorChannelsContext: `[`SensorChannelsContext`](../-sensor-channels-context/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getSensorChannelsContexts](get-sensor-channels-contexts.md) | Gets a list of all SensorChannelsContexts.`fun getSensorChannelsContexts(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SensorChannelsContext`](../-sensor-channels-context/index.md)`>` |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
