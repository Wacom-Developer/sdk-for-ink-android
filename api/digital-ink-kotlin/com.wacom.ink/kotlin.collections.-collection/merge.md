[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [kotlin.collections.Collection](index.md) / [merge](./merge.md)

# merge

`fun `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>.merge(polygonBounds: `[`Bounds`](../../com.wacom.ink.utils/-bounds/index.md)`? = null): `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../-d-i-polygon.md)`>`

Computes the union of a list of polygons. The result is a polygon with zero or more internal contours (holes).

### Parameters

`polygonBounds` - Optional. The bounds of the polygons to be merged. If not set the bounds will be computed automatically.

**Return**
The merged polygons.

