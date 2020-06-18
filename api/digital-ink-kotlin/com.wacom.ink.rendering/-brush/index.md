[digital-ink-kotlin](../../index.md) / [com.wacom.ink.rendering](../index.md) / [Brush](./index.md)

# Brush

`abstract class Brush`

An abstract class containing the base brush data for serialization / deserialization.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a brush with the specified name`Brush(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [name](name.md) | String A string that identifies the brush`val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [RasterBrush](../../com.wacom.ink.format.rendering/-raster-brush/index.md) | Represents a raster brush for serialization / deserialization.`class RasterBrush : `[`Brush`](./index.md) |
| [VectorBrush](../-vector-brush/index.md) | Contains vector brush data.`class VectorBrush : `[`Brush`](./index.md) |
