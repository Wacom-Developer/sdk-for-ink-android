[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.nodes](../index.md) / [Node](./index.md)

# Node

`abstract class Node : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)

The base node class, extended by each node from the InkTree.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new Node instance with the specified ID.`Node(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [parent](parent.md) | GroupNode? Gets the parent of this node. Returns null if this is the root node.`var parent: `[`GroupNode`](../../com.wacom.ink.format.tree.groups/-group-node/index.md)`?` |

### Functions

| Name | Summary |
|---|---|
| [hasParent](has-parent.md) | Checks if this node has a parent.`fun hasParent(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isRoot](is-root.md) | Checks if this is a root node.`fun isRoot(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [GroupNode](../../com.wacom.ink.format.tree.groups/-group-node/index.md) | An abstract class that represents a group node.`abstract class GroupNode : `[`Node`](./index.md)`, `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`Node`](./index.md)`>` |
| [InkNode](../-ink-node/index.md) | An abstract base class for ink tree nodes.`abstract class InkNode : `[`Node`](./index.md) |
