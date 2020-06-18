[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline.base](../index.md) / [DataSequenceProcessor](./index.md)

# DataSequenceProcessor

`abstract class DataSequenceProcessor<Input, Output>`

An abstract base class for pipeline stages that process a sequence of input items.

### Parameters

`Input` - The type of the input data.

`Output` - The type of the output data.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Default constructor.`DataSequenceProcessor(allData: Output? = null, keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [allData](all-data.md) | All data accumulated in the pipeline stage.`open var allData: Output?` |
| [isFirstSegment](is-first-segment.md) | `var isFirstSegment: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isLastSegment](is-last-segment.md) | `var isLastSegment: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [keepAllData](keep-all-data.md) | `open var keepAllData: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Inputs stroke data to the pipeline stage and processes it.`fun add(isFirst: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, isLast: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>``abstract fun add(addition: Input?, prediction: Input?): `[`ProcessorResult`](../-processor-result/index.md)`<Output?>` |
| [reset](reset.md) | Resets the processor to its initial state.`abstract fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [BrushApplier](../../com.wacom.ink.pipeline/-brush-applier/index.md) | For each sample of the interpolated spline the BrushApplier creates a transformation based on the sample data and transforms the brush prototype.`class BrushApplier : `[`DataSequenceProcessor`](./index.md)`<`[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>>` |
| [ConvexHullChainProducer](../../com.wacom.ink.pipeline/-convex-hull-chain-producer/index.md) | Computes the convex hull of each pair of successive brush samples.`class ConvexHullChainProducer : `[`DataSequenceProcessor`](./index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPointList2D`](../../com.wacom.ink/-d-i-point-list2-d.md)`>, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>>` |
| [PolygonMerger](../../com.wacom.ink.pipeline/-polygon-merger/index.md) | Computes the union of a list of polygons. The result is a polygon with zero or more internal contours (holes).`class PolygonMerger : `[`DataSequenceProcessor`](./index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>>` |
| [PolygonSimplifier](../../com.wacom.ink.pipeline/-polygon-simplifier/index.md) | Simplifies a polygon by reducing the number of its vertices. Uses the Ramer-Douglas-Peucker algorithm: https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm`class PolygonSimplifier : `[`DataSequenceProcessor`](./index.md)`<`[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>, `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`DIPolygon`](../../com.wacom.ink/-d-i-polygon.md)`>>` |
| [SmoothingFilter](../../com.wacom.ink.pipeline/-smoothing-filter/index.md) | Smoothening filter for path values. Each point is a liner combination of the last several points. Coefficients are produced using multi-pass double exponential smoothing. The default value of movingAverageWindowSize is 15, which is tuned for hand writing data (sequence of xy positions) at 60Hz. If the input is for example 120Hz, set movingAverageWindowSize to 30.`class SmoothingFilter : `[`DataSequenceProcessor`](./index.md)`<`[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`, `[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`>` |
| [SplineInterpolator](../../com.wacom.ink.pipeline/-spline-interpolator/index.md) | Catmull-Rom spline interpolator. Provided with Catmull-Rom spline control points, produces multiple sample points of the same kind along the spline trajectory.`class SplineInterpolator : `[`DataSequenceProcessor`](./index.md)`<`[`Spline`](../../com.wacom.ink/-spline/index.md)`, `[`InterpolatedSpline`](../../com.wacom.ink/-interpolated-spline/index.md)`>` |
| [SplineProducer](../../com.wacom.ink.pipeline/-spline-producer/index.md) | Catmull Rom Spline data processor. Provided with a float vectors : for example a sequence of x and y. Produces same kind of float vectors, representing a Catmull-Rom spline.`class SplineProducer : `[`DataSequenceProcessor`](./index.md)`<`[`FloatArrayList`](../../com.wacom.ink/-float-array-list/index.md)`, `[`Spline`](../../com.wacom.ink/-spline/index.md)`>` |
