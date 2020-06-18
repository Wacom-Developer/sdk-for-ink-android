[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.nodes](../index.md) / [InkNode](./index.md)

# InkNode

`abstract class InkNode : `[`Node`](../-node/index.md)

An abstract base class for ink tree nodes.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new InkNode instance with the specified ID.`InkNode(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Inheritors

| Name | Summary |
|---|---|
| [SensorDataNode](../-sensor-data-node/index.md) | Represents an ink tree node that refers to a SensorData object.`class SensorDataNode : `[`InkNode`](./index.md) |
| [StrokeNode](../-stroke-node/index.md) | Represents an ink tree node that refers to a Stroke object.`class StrokeNode : `[`InkNode`](./index.md) |
