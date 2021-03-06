[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PathPointLayout](index.md) / [getSize](./get-size.md)

# getSize

`fun getSize(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Gets the Size value of a path point.

### Parameters

`path` - The source path.

`pointStartIndex` - Int The index of the first element of the source point.

`default` - Float Default value that is returned in case there is no Size value in the path.

**Return**
Float Size value of the point.

