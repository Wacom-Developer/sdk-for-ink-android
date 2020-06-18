[digital-ink-kotlin](../../index.md) / [com.wacom.ink.utils](../index.md) / [PointUtils](index.md) / [getPointValue](./get-point-value.md)

# getPointValue

`@JvmStatic fun getPointValue(property: Property, point: `[`PathPoint`](../../com.wacom.ink/-path-point/index.md)`, defaults: `[`StrokeConstants`](../../com.wacom.ink/-stroke-constants/index.md)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Gets a value from the point. If a value isn't present in the point (isn't part of the layout), it will be
extracted from the defaults ([StrokeConstants](../../com.wacom.ink/-stroke-constants/index.md)).

### Parameters

`property` - The property that you want to get.

`point` - The point to get the property from.

`defaults` - The default property values. Used if property is not available in the point.

**Return**
The property.

