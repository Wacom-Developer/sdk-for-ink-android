[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [Environment](./index.md)

# Environment

`class Environment : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](../../com.wacom.ink.format.util/-sealable/index.md)

Describes the environment in which the ink was captured.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Describes the environment in which the ink was captured.`Environment()` |

### Functions

| Name | Summary |
|---|---|
| [containsProperty](contains-property.md) | Checks if a property key exists.`fun containsProperty(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAll](get-all.md) | Gets all property key-value pairs as a map.`fun getAll(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getProperty](get-property.md) | Gets a property value by key.`fun getProperty(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isSealed](is-sealed.md) | Gets a Boolean value indicating whether the object is sealed for modification.`fun isSealed(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [putAll](put-all.md) | Sets the values of the properties specified via their keys.`fun putAll(properties: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [putProperty](put-property.md) | Sets the value of the property with the specified key.`fun putProperty(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [seal](seal.md) | Seals the object for modification. Once this method is called, the object cannot be further modified.`fun seal(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
