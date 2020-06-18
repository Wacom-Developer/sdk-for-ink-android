[digital-ink-kotlin](../../index.md) / [com.wacom.ink.utils](../index.md) / [Bounds](./index.md)

# Bounds

`data class Bounds`

Bounding rect.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates instance of Bounds.`Bounds(top: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, left: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, bottom: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, right: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [bottom](bottom.md) | The bottom coordinate of the rect.`var bottom: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [left](left.md) | The left coordinate of the rect.`var left: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [right](right.md) | The right coordinate of the rect.`var right: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [top](top.md) | The top coordinate of the rect.`var top: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [intersect](intersect.md) | Intersects the current bounds with another bounds.`fun intersect(other: `[`Bounds`](./index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [roundOut](round-out.md) | `fun roundOut(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [scale](scale.md) | Scale the bounds.`fun scale(factor: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [transform](transform.md) | `fun transform(matrix: `[`Matrix3D`](../../com.wacom.ink.math/-matrix3-d/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [union](union.md) | Unite the current bounds with another bounds.`fun union(other: `[`Bounds`](./index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [of](of.md) | Computes the bounds of a [DIPolygon](../../com.wacom.ink/-d-i-polygon.md).`fun of(polygon: `[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`): `[`Bounds`](./index.md)<br>Computes the bounds of a list of [DIPolygon](../../com.wacom.ink/-d-i-polygon.md)s.`fun of(polygons: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>): `[`Bounds`](./index.md) |
