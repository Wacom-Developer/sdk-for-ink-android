[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree.groups](../index.md) / [GroupNode](./index.md)

# GroupNode

`abstract class GroupNode : `[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`, `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>`

An abstract class that represents a group node.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new instance of the GroupNode type with the specified identifier and bounding box.`GroupNode(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", boundingBox: `[`Rect`](../../com.wacom.ink.math/-rect/index.md)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [boundingBox](bounding-box.md) | Rect? The bounding rectangle of the elements contained in the group`var boundingBox: `[`Rect`](../../com.wacom.ink.math/-rect/index.md)`?` |
| [children](children.md) | List Gets an immutable list of the children of this group`val children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>` |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`open fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`open fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [iterator](iterator.md) | Gets an Pre-order depth-first iterator starting from this node.`open fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>` |

### Inheritors

| Name | Summary |
|---|---|
| [SensorDataGroupNode](../-sensor-data-group-node/index.md) | Represents a group node that contains SensorData objects.`class SensorDataGroupNode : `[`GroupNode`](./index.md) |
| [StrokeGroupNode](../-stroke-group-node/index.md) | Represents a group node that contains Stroke objects.`class StrokeGroupNode : `[`GroupNode`](./index.md) |
