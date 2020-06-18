[digital-ink-kotlin](../index.md) / [com.wacom.ink](./index.md)

## Package com.wacom.ink

### Types

| Name | Summary |
|---|---|
| [Calculator](-calculator.md) | A delegate that calculates the properties of a single path point based on three sequential points from the pointer input.`typealias Calculator = (`[`PointerData`](-pointer-data/index.md)`?, `[`PointerData`](-pointer-data/index.md)`, `[`PointerData`](-pointer-data/index.md)`?) -> `[`PathPoint`](-path-point/index.md)`?` |
| [ChannelIterable](-channel-iterable/index.md) | `class ChannelIterable : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`>` |
| [DIPointList2D](-d-i-point-list2-d.md) | A list of 2D points.`typealias DIPointList2D = `[`FloatArrayList`](-float-array-list/index.md) |
| [DIPointList3D](-d-i-point-list3-d.md) | A list of 2D points.`typealias DIPointList3D = `[`FloatArrayList`](-float-array-list/index.md) |
| [DIPolygon](-d-i-polygon.md) | Polygon - a flat list of vertices.`typealias DIPolygon = `[`FloatArrayList`](-float-array-list/index.md) |
| [DIPolyline](-d-i-polyline.md) | Polyline - a flat list of vertices`typealias DIPolyline = `[`FloatArrayList`](-float-array-list/index.md) |
| [FloatArrayList](-float-array-list/index.md) | TODO document TODO make it sealable`class FloatArrayList` |
| [IntArrayList](-int-array-list/index.md) | TODO document`class IntArrayList` |
| [InterpolatedSpline](-interpolated-spline/index.md) | The result of the SplineInterpolator add operation. Interpolated Catmull-Rom spline with additional properties, described in the layout.`data class InterpolatedSpline` |
| [LongArrayList](-long-array-list/index.md) | TODO document`class LongArrayList` |
| [PathPoint](-path-point/index.md) | Represents an ink path point containing geometric and appearance properties.`data class PathPoint` |
| [PathPointLayout](-path-point-layout/index.md) | Represents a layout specification for path points.`class PathPointLayout : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<Property>` |
| [PathSegment](-path-segment/index.md) | Represents a path segment that accumulates path points. This class can be used to accumulate added points from the path producer and pass them to the subsequent pipeline stages as a batch.`class PathSegment` |
| [Phase](-phase/index.md) | Specifies stroke phases.`enum class Phase` |
| [PointerData](-pointer-data/index.md) | Contains the pointer input data associated with a pointer event.`data class PointerData` |
| [Spline](-spline/index.md) | Represents a Catmull-Rom spline with start/end parameters for the first/last segment respectively.`data class Spline` |
| [StrokeConstants](-stroke-constants/index.md) | Contains constant (default) values for the path properties. If they are not variable - not present in the the layout, the default value will be used for rendering.`data class StrokeConstants` |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlin.collections.Collection](kotlin.collections.-collection/index.md) |  |

### Properties

| Name | Summary |
|---|---|
| [fPI](f-p-i.md) | `const val fPI: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [distanceSq](distance-sq.md) | Computes the square distance between two points.`fun distanceSq(a: `[`FloatArrayList`](-float-array-list/index.md)`, b: `[`FloatArrayList`](-float-array-list/index.md)`, layout: `[`PathPointLayout`](-path-point-layout/index.md)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [intersects](intersects.md) | `fun `[`DIPolygon`](-d-i-polygon.md)`.intersects(other: `[`DIPolygon`](-d-i-polygon.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [intersects2](intersects2.md) | `fun `[`DIPolygon`](-d-i-polygon.md)`.intersects2(other: `[`DIPolygon`](-d-i-polygon.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [linesIntersect](lines-intersect.md) | `fun linesIntersect(x1: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y1: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, x2: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y2: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, x3: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y3: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, x4: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y4: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [searchPoint](search-point.md) | `fun `[`DIPolygon`](-d-i-polygon.md)`.searchPoint(x: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, y: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [transform](transform.md) | `fun `[`DIPolygon`](-d-i-polygon.md)`.transform(mx: `[`Matrix3D`](../com.wacom.ink.math/-matrix3-d/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
