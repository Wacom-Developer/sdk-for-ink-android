[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.nodes](../index.md) / [SensorDataNode](./index.md)

# SensorDataNode

`class SensorDataNode : `[`InkNode`](../-ink-node/index.md)

Represents an ink tree node that refers to a SensorData object.

A SensorDataNode can represent either a whole sensor data sequence or part of a sequence within the ink model.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new instance of the SensorDataNode type with the specified parameters`SensorDataNode(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, data: `[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md)`, fromIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, toIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0)` |

### Properties

| Name | Summary |
|---|---|
| [data](data.md) | SensorData The SensorData associated with this node`val data: `[`SensorData`](../../com.wacom.ink.format.tree.data/-sensor-data/index.md) |
| [fromIndex](from-index.md) | Int The index of the first sensor data element, which is relevant for this node`val fromIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [toIndex](to-index.md) | Int The index of the last sensor data element, which is relevant for this node`val toIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
