[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PointerData](./index.md)

# PointerData

`data class PointerData`

Contains the pointer input data associated with a pointer event.

### Types

| Name | Summary |
|---|---|
| [Property](-property/index.md) | `enum class Property` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Contains the pointer input data associated with a pointer event.`PointerData(x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, phase: `[`Phase`](../-phase/index.md)`, timestamp: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, z: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, force: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, radiusX: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, radiusY: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, altitudeAngle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, azimuthAngle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [altitudeAngle](altitude-angle.md) | Pen altitude angle.`var altitudeAngle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [azimuthAngle](azimuth-angle.md) | Pen azimuth angle.`var azimuthAngle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [computedAzimuthAngle](computed-azimuth-angle.md) | A placeholder for azimuth angle computed in ComputeNearestAzimuthAngle.`var computedAzimuthAngle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [force](force.md) | Pointer Pressure / Force.`var force: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [phase](phase.md) | Stroke phase.`var phase: `[`Phase`](../-phase/index.md) |
| [radiusX](radius-x.md) | Touch X radius.`var radiusX: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [radiusY](radius-y.md) | Touch Y radius.`var radiusY: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [timestamp](timestamp.md) | Timestamp of the pointer event. Measured in milliseconds.`var timestamp: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [x](x.md) | Pointer X coordinate`var x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [y](y.md) | Pointer Y coordinate`var y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [z](z.md) | Pointer Z coordinate`var z: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [computeNearestAzimuthAngle](compute-nearest-azimuth-angle.md) | Computes an azimuth angle that is at the smallest absolute distance from the previous point's azimuth angle.`fun computeNearestAzimuthAngle(previous: `[`PointerData`](./index.md)`?): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?` |
| [computeValueBasedOnSpeed](compute-value-based-on-speed.md) | A helper method that calculates a normalized value based on the velocity of the pointer.`fun computeValueBasedOnSpeed(previous: `[`PointerData`](./index.md)`?, next: `[`PointerData`](./index.md)`?, minValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, maxValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, initialValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, finalValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, minSpeed: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 100f, maxSpeed: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 4000f, remap: ((`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`) -> `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`)? = null): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [equals](equals.md) | Check if two [PointerData](./index.md) objects are equal.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Get the hash code.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [speed](speed.md) | Calculates the speed of the pointer movement based on positions and timestamps of the current pointer data and its neighbours.`fun speed(previous: `[`PointerData`](./index.md)`?, next: `[`PointerData`](./index.md)`?): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
