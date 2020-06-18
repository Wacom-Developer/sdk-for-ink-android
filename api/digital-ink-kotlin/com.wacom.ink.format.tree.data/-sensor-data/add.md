[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.data](../index.md) / [SensorData](index.md) / [add](./add.md)

# add

`fun add(sensorChannel: `[`SensorChannel`](../../com.wacom.ink.format.input/-sensor-channel/index.md)`, value: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add an integer value to a sensor channel.

### Parameters

`sensorChannel` - SensorChannel The sensor channel

`value` - Int The value`fun add(sensorChannel: `[`SensorChannel`](../../com.wacom.ink.format.input/-sensor-channel/index.md)`, value: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add a float value to a sensor channel.

### Parameters

`sensorChannel` - SensorChannel The sensor channel

`value` - Float The value`@JvmName("addInts") fun add(sensorChannel: `[`SensorChannel`](../../com.wacom.ink.format.input/-sensor-channel/index.md)`, values: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add a list of integer values to a sensor channel.

### Parameters

`sensorChannel` - SensorChannel The sensor channel

`values` - List The values`@JvmName("addFloats") fun add(sensorChannel: `[`SensorChannel`](../../com.wacom.ink.format.input/-sensor-channel/index.md)`, values: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add a list of float values to a sensor channel.

### Parameters

`sensorChannel` - SensorChannel The sensor channel

`values` - List The values