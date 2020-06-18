[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PathPointLayout](./index.md)

# PathPointLayout

`class PathPointLayout : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<Property>`

Represents a layout specification for path points.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates a layout with the specified properties.`PathPointLayout(vararg args: Property)` |

### Properties

| Name | Summary |
|---|---|
| [channelMask](channel-mask.md) | Gets a bitmask representation of the layout.`var channelMask: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [size](size.md) | The number of the properties in the [PathPointLayout](./index.md)`val size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [createExtendedLayout](create-extended-layout.md) | Creates a layout based on the current one and extending it with the specified properties.`fun createExtendedLayout(vararg addedProperties: Property): `[`PathPointLayout`](./index.md) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAlpha](get-alpha.md) | Gets the Alpha value of a path point.`fun getAlpha(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getAt](get-at.md) | Gets the layout property at the specified index.`fun getAt(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): Property?` |
| [getBlue](get-blue.md) | Gets the Blue value of a path point.`fun getBlue(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getGreen](get-green.md) | Gets the Green value of a path point.`fun getGreen(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getOffsetX](get-offset-x.md) | Gets the OffsetX value of a path point.`fun getOffsetX(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getOffsetY](get-offset-y.md) | Gets the OffsetY value of a path point.`fun getOffsetY(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getOffsetZ](get-offset-z.md) | Gets the OffsetZ value of a path point.`fun getOffsetZ(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getRed](get-red.md) | Gets the Red value of a path point.`fun getRed(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getRotation](get-rotation.md) | Gets the Rotation value of a path point.`fun getRotation(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getScaleX](get-scale-x.md) | Gets the ScaleX value of a path point.`fun getScaleX(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getScaleY](get-scale-y.md) | Gets the ScaleY value of a path point.`fun getScaleY(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getScaleZ](get-scale-z.md) | Gets the ScaleZ value of a path point.`fun getScaleZ(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getSize](get-size.md) | Gets the Size value of a path point.`fun getSize(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, default: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 1.0f): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getTangent](get-tangent.md) | Gets the tangent vector of a path point.`fun getTangent(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>` |
| [getX](get-x.md) | Gets the X value of a path point.`fun getX(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getY](get-y.md) | Gets the Y value of a path point.`fun getY(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getZ](get-z.md) | Gets the Z value of a path point.`fun getZ(path: `[`FloatArrayList`](../-float-array-list/index.md)`, pointStartIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [hasProperty](has-property.md) | Checks if the specified property is supported by the layout.`fun hasProperty(property: Property): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | Gets an iterator for the properties of the layout.`fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<Property>` |
