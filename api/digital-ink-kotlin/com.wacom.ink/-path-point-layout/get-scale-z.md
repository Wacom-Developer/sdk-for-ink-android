[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PathPointLayout](index.md) / [getScaleZ](./get-scale-z.md)

# getScaleZ

`fun getScaleZ(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Gets the ScaleZ value of a path point.

### Parameters

`path` - The source path.

`pointStartIndex` - Int The index of the first element of the source point.

`default` - Float Default value that is returned in case there is no ScaleZ value in the path.

**Return**
Float ScaleZ value of the point.

