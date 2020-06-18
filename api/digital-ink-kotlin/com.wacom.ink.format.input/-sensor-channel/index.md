[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [SensorChannel](./index.md)

# SensorChannel

`data class SensorChannel : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)

Represents a generic sensor channel definition.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SensorChannel(type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, metric: `[`InkSensorMetricType`](../../com.wacom.ink.format.enums/-ink-sensor-metric-type/index.md)`, unit: `[`ScalarUnit`](../../com.wacom.ink.format.util/-scalar-unit/index.md)`, minValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, maxValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, precision: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [maxValue](max-value.md) | Float Gets the upper bound of the reported values range.`val maxValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [metric](metric.md) | InkSensorMetricType Gets the type of the data according to the SI metric system.`val metric: `[`InkSensorMetricType`](../../com.wacom.ink.format.enums/-ink-sensor-metric-type/index.md) |
| [minValue](min-value.md) | Float Gets the lower bound of the reported values range.`val minValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [precision](precision.md) | Int Gets the precision of the sensor when reporting floating-point values. Defined as an int value, used as a power of 10 during the serialization/deserialization phase.`val precision: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [resolution](resolution.md) | Float A factor multiplication value used to convert the stored data values to the specified SI metric.`val resolution: `[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [type](type.md) | String Gets a uri string that identifies the sensor channel type`val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
