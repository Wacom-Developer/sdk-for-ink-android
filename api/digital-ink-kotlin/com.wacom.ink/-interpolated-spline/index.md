[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [InterpolatedSpline](./index.md)

# InterpolatedSpline

`data class InterpolatedSpline`

The result of the SplineInterpolator add operation. Interpolated Catmull-Rom spline with additional properties, described in the
layout.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The result of the SplineInterpolator add operation. Interpolated Catmull-Rom spline with additional properties, described in the layout.`InterpolatedSpline(path: `[`FloatArrayList`](../-float-array-list/index.md)`, layout: `[`PathPointLayout`](../-path-point-layout/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [layout](layout.md) | The path point layout.`var layout: `[`PathPointLayout`](../-path-point-layout/index.md) |
| [path](path.md) | The interpolated path.`var path: `[`FloatArrayList`](../-float-array-list/index.md) |

### Functions

| Name | Summary |
|---|---|
| [getPathPoint](get-path-point.md) | Get a point on the interpolated spline by index.`fun getPathPoint(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`PathPoint`](../-path-point/index.md) |
