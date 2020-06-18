[digital-ink-kotlin](../../index.md) / [com.wacom.ink.math](../index.md) / [DIPoint4](./index.md)

# DIPoint4

`data class DIPoint4`

A point in 4D space.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Create instance of a point with coordinates (0,0,0,0).`DIPoint4()`<br>A point in 4D space.`DIPoint4(x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, z: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, w: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [w](w.md) | The w coordinate.`var w: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [x](x.md) | The x coordinate.`var x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [y](y.md) | The y coordinate.`var y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [z](z.md) | The z coordinate.`var z: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [dot](dot.md) | Dot product of two points.`infix fun dot(other: `[`DIPoint4`](./index.md)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [times](times.md) | Multiplication operator to transform a point using matrix.`operator fun times(matrix: `[`Matrix3D`](../-matrix3-d/index.md)`): `[`DIPoint4`](./index.md) |
