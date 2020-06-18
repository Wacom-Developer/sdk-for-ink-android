[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.semantics](../index.md) / [TripleStore](index.md) / [add](./add.md)

# add

`fun add(triple: `[`SemanticTriple`](../-semantic-triple/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add a semantic triple to the store. The triple is not added if it already exists.

### Parameters

`triple` - SemanticTriple The semantic triple`fun add(subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add a semantic triple to the store.

### Parameters

`subj` - String The subject

`pred` - String? The predicate

`obj` - String The object