[digital-ink-kotlin](../../index.md) / [com.wacom.ink.pipeline](../index.md) / [SmoothingFilter](index.md) / [movingAverageWindowSize](./moving-average-window-size.md)

# movingAverageWindowSize

`var movingAverageWindowSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Window size of the smoothing filter.

he default value is 15, which is tuned for hand writing data (sequence of xy positions) at 60Hz.
If the input is for example 120Hz, set movingAverageWindowSize to 30.

