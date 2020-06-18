[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.input](../index.md) / [InputContextRepository](./index.md)

# InputContextRepository

`class InputContextRepository`

A data repository which stores information about the origin of raw input data, which includes devices, environments
and sensors that produced the data.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A data repository which stores information about the origin of raw input data, which includes devices, environments and sensors that produced the data.`InputContextRepository()` |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Adds a new input context`fun add(inputContext: `[`InputContext`](../-input-context/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a new input provider`fun add(inkInputProvider: `[`InkInputProvider`](../-ink-input-provider/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a new input device`fun add(inputDevice: `[`InputDevice`](../-input-device/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a new environment`fun add(environment: `[`Environment`](../-environment/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a new sensor context`fun add(sensorContext: `[`SensorContext`](../-sensor-context/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAllEnvironments](get-all-environments.md) | Gets a map of all environments and their IDs.`fun getAllEnvironments(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Environment`](../-environment/index.md)`>` |
| [getAllInkInputProviders](get-all-ink-input-providers.md) | Gets a map of all input providers and their IDs.`fun getAllInkInputProviders(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`InkInputProvider`](../-ink-input-provider/index.md)`>` |
| [getAllInputContexts](get-all-input-contexts.md) | Gets a map of all input contexts and their IDs.`fun getAllInputContexts(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`InputContext`](../-input-context/index.md)`>` |
| [getAllInputDevices](get-all-input-devices.md) | Gets a map of all input devices and their IDs.`fun getAllInputDevices(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`InputDevice`](../-input-device/index.md)`>` |
| [getAllSensorContexts](get-all-sensor-contexts.md) | Gets a map of all sensor contexts and their IDs.`fun getAllSensorContexts(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`SensorContext`](../-sensor-context/index.md)`>` |
| [getEnvironment](get-environment.md) | Gets an environment by ID.`fun getEnvironment(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Environment`](../-environment/index.md)`?` |
| [getInputContext](get-input-context.md) | Gets an input context by ID.`fun getInputContext(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`InputContext`](../-input-context/index.md)`?` |
| [getInputDevice](get-input-device.md) | Gets an input device by ID.`fun getInputDevice(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`InputDevice`](../-input-device/index.md)`?` |
| [getInputProvider](get-input-provider.md) | Gets an input provider by ID.`fun getInputProvider(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`InkInputProvider`](../-ink-input-provider/index.md)`?` |
| [getSensorContext](get-sensor-context.md) | Gets an sensor context by ID.`fun getSensorContext(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SensorContext`](../-sensor-context/index.md)`?` |
