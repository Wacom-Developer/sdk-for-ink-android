[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PointerData](index.md) / [speed](./speed.md)

# speed

`fun speed(previous: `[`PointerData`](index.md)`?, next: `[`PointerData`](index.md)`?): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Calculates the speed of the pointer movement based on positions and timestamps of the current pointer data and its neighbours.

### Parameters

`previous` - Previous pointer data. Can be null.

`next` - Next pointer data. Can be null.

**Return**
The speed.

