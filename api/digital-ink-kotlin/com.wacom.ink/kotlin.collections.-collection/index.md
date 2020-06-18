[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [kotlin.collections.Collection](./index.md)

### Extensions for kotlin.collections.Collection

| Name | Summary |
|---|---|
| [merge](merge.md) | Computes the union of a list of polygons. The result is a polygon with zero or more internal contours (holes).`fun `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>.merge(polygonBounds: `[`Bounds`](../../com.wacom.ink.utils/-bounds/index.md)`? = null): `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>` |
| [simplified](simplified.md) | Simplifies a collection of [DIPolygon](../-d-i-polygon.md)s by reducing the number of their vertices.`fun `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>.simplified(epsilon: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>` |
