[digital-ink-kotlin](../index.md) / [com.wacom.ink.format.tree.nodes](./index.md)

## Package com.wacom.ink.format.tree.nodes

### Types

| Name | Summary |
|---|---|
| [InkNode](-ink-node/index.md) | An abstract base class for ink tree nodes.`abstract class InkNode : `[`Node`](-node/index.md) |
| [Node](-node/index.md) | The base node class, extended by each node from the InkTree.`abstract class Node : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md) |
| [SensorDataNode](-sensor-data-node/index.md) | Represents an ink tree node that refers to a SensorData object.`class SensorDataNode : `[`InkNode`](-ink-node/index.md) |
| [StrokeNode](-stroke-node/index.md) | Represents an ink tree node that refers to a Stroke object.`class StrokeNode : `[`InkNode`](-ink-node/index.md) |
