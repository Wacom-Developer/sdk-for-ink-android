[digital-ink-kotlin](../../index.md) / [com.wacom.ink.rendering](../index.md) / [VectorBrush](./index.md)

# VectorBrush

`class VectorBrush : `[`Brush`](../-brush/index.md)

Contains vector brush data.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `VectorBrush(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, brushPrototypes: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`BrushPrototype`](../-brush-prototype/index.md)`>, spacing: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f)` |

### Properties

| Name | Summary |
|---|---|
| [brushPrototypes](brush-prototypes.md) | Array The prototypes of the brush`var brushPrototypes: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`BrushPrototype`](../-brush-prototype/index.md)`>` |
| [spacing](spacing.md) | Float Specifies the spacing between particles`val spacing: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getPolygonForScale](get-polygon-for-scale.md) | Get the brush polygon corresponding to the specified scale`fun getPolygonForScale(scale: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
