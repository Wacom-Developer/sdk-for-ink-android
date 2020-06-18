[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.rendering](../index.md) / [RasterBrush](./index.md)

# RasterBrush

`class RasterBrush : `[`Brush`](../../com.wacom.ink.rendering/-brush/index.md)

Represents a raster brush for serialization / deserialization.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a new raster brush instance with the specified parameters.`RasterBrush(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, spacing: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, scattering: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, rotationMode: `[`RotationMode`](../../com.wacom.ink.format.enums/-rotation-mode/index.md)`, shapeTextures: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`>, shapeTextureURIs: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, fillTexture: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`, fillTextureURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, fillWidth: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, fillHeight: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, randomizeFill: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, blendMode: `[`BlendMode`](../../com.wacom.ink.rendering/-blend-mode/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [blendMode](blend-mode.md) | BlendMode An enum value that specifies how the brush particles are blended during rendering`val blendMode: `[`BlendMode`](../../com.wacom.ink.rendering/-blend-mode/index.md) |
| [fillHeight](fill-height.md) | Float The height of the fill texture tiles`val fillHeight: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [fillTexture](fill-texture.md) | ByteArray The texture that defines the shading of the stroke's interior`val fillTexture: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [fillTextureURI](fill-texture-u-r-i.md) | String URI of the texture that defines the shading of the stroke's interior`val fillTextureURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [fillWidth](fill-width.md) | Float The width of the fill texture tiles`val fillWidth: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [randomizeFill](randomize-fill.md) | Boolean Whether the offset of the fill texture is set to random value for each new stroke`val randomizeFill: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [rotationMode](rotation-mode.md) | RotationMode The rotation mode for the brush particles`val rotationMode: `[`RotationMode`](../../com.wacom.ink.format.enums/-rotation-mode/index.md) |
| [scattering](scattering.md) | Float Value that controls the particle scattering along the stroke normal`val scattering: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [shapeTextures](shape-textures.md) | List A collection of textures that define the shape of the stroke particles`val shapeTextures: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`>` |
| [shapeTextureURIs](shape-texture-u-r-is.md) | List A collection of texture URIs that define the shape of the stroke particles`val shapeTextureURIs: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [spacing](spacing.md) | Float The spacing between brush particles`val spacing: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
