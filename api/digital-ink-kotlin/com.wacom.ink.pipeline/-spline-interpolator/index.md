[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [SplineInterpolator](./index.md)

# SplineInterpolator

`class SplineInterpolator : `[`DataSequenceProcessor`](../../com.wacom.ink.pipeline.base/-data-sequence-processor/index.md)`<`[`Spline`](../../com.wacom.ink/-spline/index.md)`, `[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`>`

Catmull-Rom spline interpolator.
Provided with Catmull-Rom spline control points, produces multiple sample points of the same kind along the spline trajectory.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Creates an instance of the SplineInterpolator type.`SplineInterpolator(spacing: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, splitCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, calculateDerivatives: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, interpolateByLength: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [calculateDerivatives](calculate-derivatives.md) | If set to true - the derivatives of the curve are calculated for each sample point. This is needed for some modes of the raster rendering.`var calculateDerivatives: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [defaultSize](default-size.md) | A constant size value that is used when the layout does not include variable size property.`var defaultSize: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [interpolateByLength](interpolate-by-length.md) | Scale factor for the distance between control points.`var interpolateByLength: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sizeScaleFactor](size-scale-factor.md) | Scale factor for the path point size.`var sizeScaleFactor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [spacing](spacing.md) | Specifies the spacing between two successive samples.`var spacing: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [splitCount](split-count.md) | Determines the number of iterations for the discretization.`var splitCount: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `fun add(addition: `[`Spline`](../../com.wacom.ink/-spline/index.md)`?, prediction: `[`Spline`](../../com.wacom.ink/-spline/index.md)`?): `[`ProcessorResult`](../../com.wacom.ink.pipeline.base/-processor-result/index.md)`<`[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`?>` |
| [initPrivateState](init-private-state.md) | `fun initPrivateState(layout: `[`PathPointLayout`](../../com.wacom.ink/-path-point-layout/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [reset](reset.md) | Resets the spline interpolator to its initial state.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
