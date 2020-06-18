[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format](../index.md) / [SensorDataRepository](./index.md)

# SensorDataRepository

`class SensorDataRepository : `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`>`

Represents a data repository, which holds a collection of SensorData objects.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Represents a data repository, which holds a collection of SensorData objects.`SensorDataRepository()` |

### Properties

| Name | Summary |
|---|---|
| [size](size.md) | The size of the SensorDataRepository.`val size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Adds a SensorData object to the repository. Overwrites existing SensorData object with the same id if it exists`fun add(sensorData: `[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addAll](add-all.md) | Adds multiple SensorData objects to the repository.`fun addAll(elements: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [contains](contains.md) | Gets a Boolean value indicating whether the repository contains the object with the provided ID.`fun contains(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Gets a Boolean value indicating whether the repository contains the object.`fun contains(sensorData: `[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](contains-all.md) | Gets a Boolean value indicating whether the repository contains the objects.`fun containsAll(data: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEmpty](is-empty.md) | Whether the SensorDataRepository is empty.`fun isEmpty(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | Get an iterator for the repository.`fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`>` |
