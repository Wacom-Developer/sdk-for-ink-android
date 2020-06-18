[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.tree](../index.md) / [ViewTree](./index.md)

# ViewTree

`class ViewTree`

Represents a named ink tree, which is used to store paths or sensor data objects in a particular hierarchical order.

### Properties

| Name | Summary |
|---|---|
| [name](name.md) | String An Uri that identifies the view.`var name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [tree](tree.md) | InkTree The InkTree of the view`val tree: `[`InkTree`](../-ink-tree/index.md) |

### Functions

| Name | Summary |
|---|---|
| [deepEquals](deep-equals.md) | `fun deepEquals(other: `[`ViewTree`](./index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [createCustomView](create-custom-view.md) | Create a custom view.`fun createCustomView(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tree: `[`InkTree`](../-ink-tree/index.md)`): `[`ViewTree`](./index.md) |
| [createSemanticsView](create-semantics-view.md) | Creates a Semantics View.`fun createSemanticsView(tree: `[`InkTree`](../-ink-tree/index.md)`): `[`ViewTree`](./index.md) |
| [createTextSegmentationView](create-text-segmentation-view.md) | Creates a Text Segmentation View.`fun createTextSegmentationView(tree: `[`InkTree`](../-ink-tree/index.md)`): `[`ViewTree`](./index.md) |
