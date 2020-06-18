[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format](../index.md) / [ViewsRepository](./index.md)

# ViewsRepository

`class ViewsRepository : `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`>`

Represents a data repository, which holds a collection of ViewTree objects.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Represents a data repository, which holds a collection of ViewTree objects.`ViewsRepository(inkModel: `[`InkModel`](../-ink-model/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [size](size.md) | The size of the ViewsRepository.`val size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Adds a ViewTree object to the repository. Overwrites existing ViewTree object with the same id if it exists.`fun add(view: `[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addAll](add-all.md) | Adds multiple ViewTree objects to the repository.`fun addAll(views: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [contains](contains.md) | Gets a Boolean value indicating whether the repository contains the specified object.`fun contains(view: `[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](contains-all.md) | Gets a Boolean value indicating whether the repository contains the specified objects.`fun containsAll(views: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEmpty](is-empty.md) | Whether the ViewsRepository is empty.`fun isEmpty(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | Get an iterator for the repository.`fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`>` |
| [remove](remove.md) | Removes the specified view from the model.`fun remove(item: `[`ViewTree`](../../com.wacom.ink.format.tree/-view-tree/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
