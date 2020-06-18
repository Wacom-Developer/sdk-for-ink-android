[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [InkInputProvider](index.md) / [putAll](./put-all.md)

# putAll

`fun putAll(properties: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Sets the values of the properties specified via their keys.

Overwrites property value if key exists.
The operation cannot be performed if the object is sealed.

### Parameters

`properties` - Map&lt;String, String&gt; The key-value property map.