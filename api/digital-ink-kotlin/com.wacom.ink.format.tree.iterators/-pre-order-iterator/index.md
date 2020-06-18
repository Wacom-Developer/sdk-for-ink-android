[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.iterators](../index.md) / [PreOrderIterator](./index.md)

# PreOrderIterator

`class PreOrderIterator : `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>`

Depth first pre-order traversal of the DOM tree.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates an instance of PreOrderIterator`PreOrderIterator(root: `[`GroupNode`](../../com.wacom.ink.format.tree.groups/-group-node/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [currentDepth](current-depth.md) | Gets the depth of the current node.`val currentDepth: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [hasNext](has-next.md) | Returns a Boolean indicating whether there is a next available node.`fun hasNext(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [next](next.md) | Gets the next node.`fun next(): `[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md) |
| [reset](reset.md) | Reset the iterator.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
