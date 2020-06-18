[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [Spline](./index.md)

# Spline

`data class Spline`

Represents a Catmull-Rom spline with start/end parameters for the first/last segment respectively.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates an instance of the Spline type.`Spline(path: `[`FloatArrayList`](../-float-array-list/index.md)`, layout: `[`PathPointLayout`](../-path-point-layout/index.md)`, ts: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f, tf: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f)` |

### Properties

| Name | Summary |
|---|---|
| [layout](layout.md) | The spline point layout.`var layout: `[`PathPointLayout`](../-path-point-layout/index.md) |
| [path](path.md) | List of spline values.`var path: `[`FloatArrayList`](../-float-array-list/index.md) |
| [tf](tf.md) | Final parameter for the last Catmull-Rom segment.`var tf: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [ts](ts.md) | Start parameter for the first Catmull-Rom segment.`var ts: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | `fun copy(): `[`Spline`](./index.md) |
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getChannel](get-channel.md) | `fun getChannel(propertyIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ChannelIterable`](../-channel-iterable/index.md) |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [transform](transform.md) | `fun transform(scaleX: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f, scaleY: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f, sizeScale: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f, translateX: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f, translateY: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f, angle: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f, pivotX: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f, pivotY: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
