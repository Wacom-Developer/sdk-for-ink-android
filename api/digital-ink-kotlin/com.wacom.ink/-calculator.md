[digital-ink-kotlin](../index.md) / [com.wacom.ink](index.md) / [Calculator](./-calculator.md)

# Calculator

`typealias Calculator = (`[`PointerData`](-pointer-data/index.md)`?, `[`PointerData`](-pointer-data/index.md)`, `[`PointerData`](-pointer-data/index.md)`?) -> `[`PathPoint`](-path-point/index.md)`?`

A delegate that calculates the properties of a single path point based on three sequential points from the pointer input.

Param 1 - A reference to the first (oldest) point in a sequence of three input points.
Param 2 - A reference to the second point in a sequence of three input points.
Param 3 - A reference to the third (latest) point in a sequence of three input points.
Returns a path point object.

