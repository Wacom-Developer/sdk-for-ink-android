[digital-ink-kotlin](../../index.md) / [com.wacom.ink.utils](../index.md) / [PointUtils](./index.md)

# PointUtils

`class PointUtils`

Utility functions help you work easily with the digital ink points.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Utility functions help you work easily with the digital ink points.`PointUtils()` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [getPointValue](get-point-value.md) | Gets a value from the point. If a value isn't present in the point (isn't part of the layout), it will be extracted from the defaults ([StrokeConstants](../../com.wacom.ink/-stroke-constants/index.md)).`fun getPointValue(property: Property, point: `[`PathPoint`](../../com.wacom.ink/-path-point/index.md)`, defaults: `[`StrokeConstants`](../../com.wacom.ink/-stroke-constants/index.md)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [isFiniteFloat](is-finite-float.md) | Checks if a float value is finite. This means that is not null, not NaN and not Infinity.`fun isFiniteFloat(value: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
