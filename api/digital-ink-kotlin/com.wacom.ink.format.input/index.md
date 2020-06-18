[digital-ink-kotlin](../index.md) / [com.wacom.ink.format.input](./index.md)

## Package com.wacom.ink.format.input

### Types

| Name | Summary |
|---|---|
| [Environment](-environment/index.md) | Describes the environment in which the ink was captured.`class Environment : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](../com.wacom.ink.format.util/-sealable/index.md) |
| [InkInputProvider](-ink-input-provider/index.md) | Represents the device/object that is used for producing ink data - touch, mouse, stylus, controller, etc.`class InkInputProvider : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](../com.wacom.ink.format.util/-sealable/index.md) |
| [InputContext](-input-context/index.md) | A combination of an Environment instance and a SensorContext instance.`data class InputContext : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md) |
| [InputContextRepository](-input-context-repository/index.md) | A data repository which stores information about the origin of raw input data, which includes devices, environments and sensors that produced the data.`class InputContextRepository` |
| [InputDevice](-input-device/index.md) | Represents the hardware device, on which the sensor data has been produced (touch enabled mobile device, touch capable monitor, digitizer, etc).`class InputDevice : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md)`, `[`Sealable`](../com.wacom.ink.format.util/-sealable/index.md) |
| [SensorChannel](-sensor-channel/index.md) | Represents a generic sensor channel definition.`data class SensorChannel : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md) |
| [SensorChannelsContext](-sensor-channels-context/index.md) | Represents a collection of sensor channels along with associated properties for the sensor.`class SensorChannelsContext : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md) |
| [SensorContext](-sensor-context/index.md) | Represents a collection of sensor channel contexts, used for capturing digital ink input.`class SensorContext : `[`IdentifiableImpl`](../com.wacom.ink.model/-identifiable-impl/index.md) |
