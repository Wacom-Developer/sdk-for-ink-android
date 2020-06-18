[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.semantics](../index.md) / [SemanticTriple](./index.md)

# SemanticTriple

`data class SemanticTriple`

An RDF compliant semantic triple that represents a statement.

A statement states a single thing about its subject by linking it to an object via a predicate.
The subject and the predicate are resources and are identified by an URI,
whereas the object can be either a resource or a literal value.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Initializes a new instance of the SemanticTriple type with the specified parameters.`SemanticTriple(subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [obj](obj.md) | String The triple's object`val obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [pred](pred.md) | String? The triple's predicate`val pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [subj](subj.md) | String The triple's subject`val subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
