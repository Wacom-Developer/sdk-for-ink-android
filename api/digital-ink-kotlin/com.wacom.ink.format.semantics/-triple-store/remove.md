[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.semantics](../index.md) / [TripleStore](index.md) / [remove](./remove.md)

# remove

`fun remove(subj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, pred: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, obj: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Remove a semantic triple from the store.

### Parameters

`subj` - String The subject

`pred` - String? The predicate

`obj` - String? The object`fun remove(iterator: `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`Node`](../../com.wacom.ink.format.tree.nodes/-node/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Remove semantic triples from the store via providing a tree iterator.

### Parameters

`iterator` - Iterator The iterator