[digital-ink-kotlin](../../index.md) / [com.wacom.ink.rendering](../index.md) / [BlendMode](./index.md)

# BlendMode

`enum class BlendMode`

Specifies the supported blend modes.

### Enum Values

| Name | Summary |
|---|---|
| [SOURCE_OVER](-s-o-u-r-c-e_-o-v-e-r.md) | Standard alpha composition - draws new shapes on top of the existing canvas content. Also known as "Normal". |
| [DESTINATION_OVER](-d-e-s-t-i-n-a-t-i-o-n_-o-v-e-r.md) | New shapes are drawn behind the existing canvas content. Also known as "Normal Reverse". |
| [DESTINATION_OUT](-d-e-s-t-i-n-a-t-i-o-n_-o-u-t.md) | The existing content is kept where it doesn't overlap the new shape. Also known as "Erase". |
| [LIGHTER](-l-i-g-h-t-e-r.md) | Where both shapes overlap the color is determined by adding color components. Also known as "Add". |
| [COPY](-c-o-p-y.md) | No color blending, only the new layer is shown. Also known as "None". |
| [MIN](-m-i-n.md) | The result is the minimum of both color. The result is a darker color. |
| [MAX](-m-a-x.md) | The result is the maximum of both color. The result is a lighter color. |
