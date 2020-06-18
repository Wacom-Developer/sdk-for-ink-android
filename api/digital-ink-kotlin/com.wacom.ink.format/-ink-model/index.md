[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format](../index.md) / [InkModel](./index.md)

# InkModel

`class InkModel`

A data model for representing and manipulating ink data captured using an electronic pen or stylus, or using touch input.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | A data model for representing and manipulating ink data captured using an electronic pen or stylus, or using touch input.`InkModel()` |

### Properties

| Name | Summary |
|---|---|
| [brushRepository](brush-repository.md) | BrushRepository The brush repository`var brushRepository: `[`BrushRepository`](../../com.wacom.ink.format.rendering/-brush-repository/index.md) |
| [inkTree](ink-tree.md) | InkTree? The main tree`var inkTree: `[`InkTree`](../../com.wacom.ink.format.tree/-ink-tree/index.md) |
| [inputConfiguration](input-configuration.md) | InputContextRepository The input configuration repository`var inputConfiguration: `[`InputContextRepository`](../../com.wacom.ink.format.input/-input-context-repository/index.md) |
| [knowledgeGraph](knowledge-graph.md) | TripleStore The knowledge graph associated with this ink model`var knowledgeGraph: `[`TripleStore`](../../com.wacom.ink.format.semantics/-triple-store/index.md) |
| [properties](properties.md) | HashMap&lt;String, String&gt; The map of properties`var properties: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [sensorDataRepository](sensor-data-repository.md) | SensorDataRepository The sensor data repository`var sensorDataRepository: `[`SensorDataRepository`](../-sensor-data-repository/index.md) |
| [strokeRepository](stroke-repository.md) | StrokeRepository The stroke repository`var strokeRepository: `[`StrokeRepository`](../-stroke-repository/index.md) |
| [transform](transform.md) | Matrix3D The global transformation matrix`var transform: `[`Matrix3D`](../../com.wacom.ink.math/-matrix3-d/index.md) |
| [views](views.md) | List List of the views added to this ink model`var views: `[`ViewsRepository`](../-views-repository/index.md) |
