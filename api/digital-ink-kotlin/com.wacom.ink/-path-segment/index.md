[digital-ink-kotlin](../../index.md) / [com.wacom.ink](../index.md) / [PathSegment](./index.md)

# PathSegment

`class PathSegment`

Represents a path segment that accumulates path points.
This class can be used to accumulate added points from the path producer and pass them to the subsequent pipeline stages as a batch.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Represents a path segment that accumulates path points. This class can be used to accumulate added points from the path producer and pass them to the subsequent pipeline stages as a batch.`PathSegment()` |

### Properties

| Name | Summary |
|---|---|
| [accumulatedAddition](accumulated-addition.md) | Gets the added points accumulated in the path segment since the last call of Reset.`var accumulatedAddition: `[`FloatArrayList`](../-float-array-list/index.md) |
| [isFirst](is-first.md) | Gets a value indicating whether this is the first segment of a path. Returns true when the segment contains the first point of a path.`var isFirst: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isLast](is-last.md) | Gets a value indicating whether this is the last segment of a path. Returns true when the segment contains the last point of a path.`var isLast: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [lastPrediction](last-prediction.md) | Gets the last prediction stored in the path segment.`var lastPrediction: `[`FloatArrayList`](../-float-array-list/index.md) |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Adds a point to the path segment.`fun add(phase: `[`Phase`](../-phase/index.md)`, added: `[`FloatArrayList`](../-float-array-list/index.md)`?, predicted: `[`FloatArrayList`](../-float-array-list/index.md)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [reset](reset.md) | Resets the path segment to its initial state. IsFirst and IsLast properties are set to false. AccumulatedAddition and LastPrediction are cleared.`fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
