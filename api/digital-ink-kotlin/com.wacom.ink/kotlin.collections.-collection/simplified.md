[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [kotlin.collections.Collection](index.md) / [simplified](./simplified.md)

# simplified

`fun `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>.simplified(epsilon: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>`

Simplifies a collection of [DIPolygon](../-d-i-polygon.md)s by reducing the number of their vertices.

### Parameters

`epsilon` - Threshold value that determines the level of simplification. Greater values result into polygons with less segments.

**Return**
The simplified [DIPolygon](../-d-i-polygon.md)s.

