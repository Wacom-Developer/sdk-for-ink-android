[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.data](../index.md) / [Stroke](./index.md)

# Stroke

`class Stroke : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)

A Stroke is defined as a combination of:
a Catmull-Rom spline,
optional rendering style and
optional reference to raw input data, which the stroke originates from.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new Stroke instance with the specified parameters`Stroke(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, spline: `[`Spline`](../../com.wacom.ink/-spline/index.md)`, style: `[`Style`](../../com.wacom.ink.format.rendering/-style/index.md)`? = null, sensorDataID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, sensorDataOffset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [sensorDataID](sensor-data-i-d.md) | String? The ID of the sensor data that is used as a source for the stroke's spline`var sensorDataID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [sensorDataOffset](sensor-data-offset.md) | Int? The index of the sensor data entry that is used as a source for the first spline point`var sensorDataOffset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [spline](spline.md) | Spline The stroke's Catmull-Rom spline`val spline: `[`Spline`](../../com.wacom.ink/-spline/index.md) |
| [style](style.md) | Style? The Style object associated with this stroke`val style: `[`Style`](../../com.wacom.ink.format.rendering/-style/index.md)`?` |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
