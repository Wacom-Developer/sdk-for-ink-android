[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PointerData](index.md) / [computeValueBasedOnSpeed](./compute-value-based-on-speed.md)

# computeValueBasedOnSpeed

`fun computeValueBasedOnSpeed(previous: `[`PointerData`](index.md)`?, next: `[`PointerData`](index.md)`?, minValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, maxValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, initialValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, finalValue: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`? = null, minSpeed: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 100f, maxSpeed: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 4000f, remap: ((`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`) -> `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`)? = null): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

A helper method that calculates a normalized value based on the velocity of the pointer.

### Parameters

`previous` - A reference to the first (oldest) point in a sequence of three input points.

`next` - A reference to the third (latest) point in a sequence of three input points.

`minValue` - Min result value.

`maxValue` - Max result value.

`initialValue` - Initial value. The initial value is returned as a result of the function when p1 is null (beginning of stroke).

`finalValue` - Final value. The final value is returned as a result of the function when p3 is null (end of stroke).

`minSpeed` - Speed is clamped to this value if speed is below the value.

`maxSpeed` - Speed is clamped to this value if speed is above the value.

`remap` - A lambda that that defines a custom transformation on the normalized speed value.

**Return**

