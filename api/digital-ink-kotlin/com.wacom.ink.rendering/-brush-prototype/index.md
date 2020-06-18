[digital-ink-kotlin](../../index.md) / [com.wacom.ink.rendering](../index.md) / [BrushPrototype](./index.md)

# BrushPrototype

`class BrushPrototype`

A polygon that describes the shape of a 2D or 3D brush.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `BrushPrototype(brushPolygon2d: `[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`, minScale: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, shapeURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)`<br>`BrushPrototype(brushPolygon3d: `[`DIPointList3D`](../../com.wacom.ink/-d-i-point-list3-d.md)`, indices: `[`IntArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)`, minScale: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, shapeURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [brushPolygon](brush-polygon.md) | `var brushPolygon: `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md) |
| [indices](indices.md) | `lateinit var indices: `[`IntArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [is2D](is2-d.md) | Boolean Whether this is the brush prototype of a 2D brush`val is2D: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [minScale](min-scale.md) | Minimum scale of the brush sample, after which this shape is applied`val minScale: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [shapeURI](shape-u-r-i.md) | String? Uri string that identifies the shape`val shapeURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
