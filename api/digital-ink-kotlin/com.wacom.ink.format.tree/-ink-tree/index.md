[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree](../index.md) / [InkTree](./index.md)

# InkTree

`class InkTree : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>`

Represent a tree of ink nodes.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `InkTree(root: `[`GroupNode`](../../com.wacom.ink.format.tree.groups/-group-node/index.md)`, inkModel: `[`InkModel`](../../com.wacom.ink.format/-ink-model/index.md)`)`<br>Creates an instance of InkTree`InkTree(inkModel: `[`InkModel`](../../com.wacom.ink.format/-ink-model/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [inkModel](ink-model.md) | InkModel The ink model containing the tree`val inkModel: `[`InkModel`](../../com.wacom.ink.format/-ink-model/index.md) |
| [primaryLocale](primary-locale.md) | String The primary locale associated with this ink tree. This is a helper property that looks for a statement with CommonRDF.Locale predicate in the knowledge graph.`val primaryLocale: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [root](root.md) | GroupNode The root node of this ink tree`var root: `[`GroupNode`](../../com.wacom.ink.format.tree.groups/-group-node/index.md)`?` |

### Functions

| Name | Summary |
|---|---|
| [deepEquals](deep-equals.md) | `fun deepEquals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [iterator](iterator.md) | `fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>` |
