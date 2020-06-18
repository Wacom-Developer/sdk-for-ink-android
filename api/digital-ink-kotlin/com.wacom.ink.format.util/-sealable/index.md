[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.util](../index.md) / [Sealable](./index.md)

# Sealable

`interface Sealable`

Implemented by objects that can be sealed for modification.

### Functions

| Name | Summary |
|---|---|
| [isSealed](is-sealed.md) | Gets a Boolean value indicating whether the object is sealed.`abstract fun isSealed(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [seal](seal.md) | Seals an object to prevent further modification of its state.`abstract fun seal(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [Environment](../../com.wacom.ink.format.input/-environment/index.md) | Describes the environment in which the ink was captured.`class Environment : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](./index.md) |
| [InkInputProvider](../../com.wacom.ink.format.input/-ink-input-provider/index.md) | Represents the device/object that is used for producing ink data - touch, mouse, stylus, controller, etc.`class InkInputProvider : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](./index.md) |
| [InputDevice](../../com.wacom.ink.format.input/-input-device/index.md) | Represents the hardware device, on which the sensor data has been produced (touch enabled mobile device, touch capable monitor, digitizer, etc).`class InputDevice : `[`IdentifiableImpl`](../../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](./index.md) |
