[digital-ink-kotlin](../index.md) / [com.wacom.ink.format.rendering](./index.md)

## Package com.wacom.ink.format.rendering

### Types

| Name | Summary |
|---|---|
| [BrushRepository](-brush-repository/index.md) | A repository for brush objects.`class BrushRepository` |
| [PathPointProperties](-path-point-properties/index.md) | A data structure that contains size, color components and brush transform components. Used to specify stroke attributes that are constant along the stroke length.`data class PathPointProperties` |
| [RasterBrush](-raster-brush/index.md) | Represents a raster brush for serialization / deserialization.`class RasterBrush : `[`Brush`](../com.wacom.ink.rendering/-brush/index.md) |
| [Style](-style/index.md) | The Style is defined as a combination of a PathPointProperties configuration, reference to a Brush, a random number generator seed value and rendering method type.`data class Style` |
