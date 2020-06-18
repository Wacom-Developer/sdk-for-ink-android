[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.rendering](../index.md) / [Style](./index.md)

# Style

`data class Style`

The Style is defined as a combination of a PathPointProperties configuration, reference to a Brush, a random number
generator seed value and rendering method type.

Setting the Style property allows overriding of specific path point properties, color components and/or brush transform components.
A Style with PathPointProperties configuration should be normally used to define constant path components.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new Style instance`Style(brushURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, particlesRandomSeed: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, props: `[`PathPointProperties`](../-path-point-properties/index.md)`? = null, renderModeUri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [brushURI](brush-u-r-i.md) | String A string that identifies the brush associated with this style. This property holds the brush name, which is normally a uri string`val brushURI: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [particlesRandomSeed](particles-random-seed.md) | Int A random seed used for randomly generated attributes of a stroke`val particlesRandomSeed: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [props](props.md) | PathPointProperties? The rendering properties of the style override the rendering properties of the brush`val props: `[`PathPointProperties`](../-path-point-properties/index.md)`?` |
| [renderModeUri](render-mode-uri.md) | String? A user defined identifier of the rendering mode. The render mode is normally identified with a uri string, for example: "will3://rendering//eraser"`val renderModeUri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
