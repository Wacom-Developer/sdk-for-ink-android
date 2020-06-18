[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.semantics](../index.md) / [TripleStore](./index.md)

# TripleStore

`class TripleStore`

Contains a list of semantic triples to encode relationships between subject, predicate and object as defined in the RDF specification.

nodes of the logical trees, contained within the InkModel, are identified by URIs in the triple store.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Contains a list of semantic triples to encode relationships between subject, predicate and object as defined in the RDF specification.`TripleStore()` |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | Add a semantic triple to the store. The triple is not added if it already exists.`fun add(triple: `[`SemanticTriple`](../-semantic-triple/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add a semantic triple to the store.`fun add(subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [equals](equals.md) | Determines whether the specified object is equal to the current object.`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [find](find.md) | Find a triple.`fun find(subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SemanticTriple`](../-semantic-triple/index.md)`?` |
| [findByObject](find-by-object.md) | Finds all semantic triples that match the specified object.`fun findByObject(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SemanticTriple`](../-semantic-triple/index.md)`>?` |
| [findByPredicate](find-by-predicate.md) | Finds all semantic triples that match the specified predicate.`fun findByPredicate(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SemanticTriple`](../-semantic-triple/index.md)`>?` |
| [findBySubject](find-by-subject.md) | Finds all semantic triples that match the specified subject.`fun findBySubject(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SemanticTriple`](../-semantic-triple/index.md)`>?` |
| [getTriples](get-triples.md) | Get a list of all semantic triples.`fun getTriples(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SemanticTriple`](../-semantic-triple/index.md)`>` |
| [hashCode](hash-code.md) | Generates the hash code of the current object.`fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [remove](remove.md) | Remove a semantic triple from the store.`fun remove(subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove semantic triples from the store via providing a tree iterator.`fun remove(iterator: `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
