[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PathPointLayout](index.md) / [getTangent](./get-tangent.md)

# getTangent

`fun getTangent(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>`

Gets the tangent vector of a path point.

### Parameters

`path` - The source path.

`pointStartIndex` - Int The index of the first element of the source point.

**Return**
Pair&lt;Float?, Float?&gt; The tangent vector to a point. Returns zero vector if the path does not contain tangent values.

