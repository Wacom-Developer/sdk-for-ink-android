[digital-ink-kotlin](../../index.md) / [com.wacom.ink.format.serialization](../index.md) / [Will3Codec](./index.md)

# Will3Codec

`class Will3Codec`

Encodes and decodes the ink model to Universal Ink Model format (UIM).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Encodes and decodes the ink model to Universal Ink Model format (UIM).`Will3Codec()` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [decode](decode.md) | Decodes a UIM formatted byte array to an InkModel.`fun decode(byteArray: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`): `[`InkModel`](../../com.wacom.ink.format/-ink-model/index.md) |
| [encode](encode.md) | Encodes an ink model to a UIM formatted byte array.`fun encode(inkModel: `[`InkModel`](../../com.wacom.ink.format/-ink-model/index.md)`, outputStream: `[`OutputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
