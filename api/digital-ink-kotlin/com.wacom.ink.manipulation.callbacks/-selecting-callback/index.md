[digital-ink-kotlin](../../index.md) / [com.wacom.ink.manipulation.callbacks](../index.md) / [SelectingCallback](./index.md)

# SelectingCallback

`interface SelectingCallback`

The callback for events while selecting.

### Functions

| Name | Summary |
|---|---|
| [onStrokeAdded](on-stroke-added.md) | Fired when a new stroke is created.`abstract fun onStrokeAdded(stroke: `[`InkStroke`](../../com.wacom.ink.model/-ink-stroke/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStrokeRemoved](on-stroke-removed.md) | Fired when a stroke is removed.`abstract fun onStrokeRemoved(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onStrokeSelected](on-stroke-selected.md) | Fired when a stroke is selected.`abstract fun onStrokeSelected(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
