[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.rendering](../index.md) / [BrushRepository](./index.md)

# BrushRepository

`class BrushRepository`

A repository for brush objects.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A repository for brush objects.`BrushRepository()` |

### Functions

| Name | Summary |
|---|---|
| [addRasterBrush](add-raster-brush.md) | Adds a raster brush to the repository.`fun addRasterBrush(rasterBrush: `[`RasterBrush`](../-raster-brush/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addVectorBrush](add-vector-brush.md) | Adds a vector brush to the repository.`fun addVectorBrush(vectorBrush: `[`VectorBrush`](../../com.wacom.ink.rendering/-vector-brush/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAllRasterBrushes](get-all-raster-brushes.md) | Gets all raster brushes as a list.`fun getAllRasterBrushes(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`RasterBrush`](../-raster-brush/index.md)`>` |
| [getAllVectorBrushes](get-all-vector-brushes.md) | Gets all vector brushes as a list.`fun getAllVectorBrushes(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`VectorBrush`](../../com.wacom.ink.rendering/-vector-brush/index.md)`>` |
| [getBrush](get-brush.md) | Gets a brush by its name.`fun getBrush(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Brush`](../../com.wacom.ink.rendering/-brush/index.md)`?` |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
