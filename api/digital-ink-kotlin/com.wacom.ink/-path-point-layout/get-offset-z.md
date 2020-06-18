[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PathPointLayout](index.md) / [getOffsetZ](./get-offset-z.md)

# getOffsetZ

`fun getOffsetZ(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Gets the OffsetZ value of a path point.

### Parameters

`path` - The source path.

`pointStartIndex` - Int The index of the first element of the source point.

`default` - Float Default value that is returned in case there is no OffsetZ value in the path.

**Return**
Float OffsetZ value of the point.

