[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format](../index.md) / [StrokeRepository](./index.md)

# StrokeRepository

`class StrokeRepository : `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`Stroke`](../../com.wacom.ink.format.tree.data/-stroke/index.md)`>`

Represents a data repository, which holds a collection of Stroke objects.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Represents a data repository, which holds a collection of Stroke objects.`StrokeRepository()` |

### Properties

| Name | Summary |
|---|---|
| [size](size.md) | The size of the StrokeRepository.`val size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Adds a Stroke object to the repository. Overwrites existing Stroke object with the same id if it exists.`fun add(stroke: `[`Stroke`](../../com.wacom.ink.format.tree.data/-stroke/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addAll](add-all.md) | Adds multiple Stroke objects to the repository.`fun addAll(strokes: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`Stroke`](../../com.wacom.ink.format.tree.data/-stroke/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [contains](contains.md) | Gets a Boolean value indicating whether the repository contains the specified object.`fun contains(stroke: `[`Stroke`](../../com.wacom.ink.format.tree.data/-stroke/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](contains-all.md) | Gets a Boolean value indicating whether the repository contains the specified objects.`fun containsAll(strokes: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`Stroke`](../../com.wacom.ink.format.tree.data/-stroke/index.md)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEmpty](is-empty.md) | Whether the StrokeRepository is empty.`fun isEmpty(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | Get an iterator for the repository.`fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`Stroke`](../../com.wacom.ink.format.tree.data/-stroke/index.md)`>` |
