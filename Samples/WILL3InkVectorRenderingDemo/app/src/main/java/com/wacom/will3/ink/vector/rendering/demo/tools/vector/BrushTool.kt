/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.tools.vector

import com.wacom.ink.Calculator
import com.wacom.ink.PathPoint
import com.wacom.ink.PathPointLayout
import com.wacom.will3.ink.vector.rendering.demo.brush.BrushPalette
import com.wacom.will3.ink.vector.rendering.demo.brush.URIBuilder
import com.wacom.will3.ink.vector.rendering.demo.computeValueBasedOnPressure
import com.wacom.will3.ink.vector.rendering.demo.tools.MathUtils
import com.wacom.will3.ink.vector.rendering.demo.tools.Range
import kotlin.math.cos

class BrushTool : VectorTool {
    constructor() : super()

    companion object {
        val uri = URIBuilder.getToolURI("vector", "brush")
    }

    override var brush = BrushPalette.circle()
    override var alphaValue = 0.5f

    var previousSize = 10f

    override fun getLayout(): PathPointLayout {
        if (isStylus) {
            /**
             * The currently layout will use:
             * - Coordinate values (X and Y)
             * - Size - the size of the brush at any point of the stroke
             */
            return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE
            )
        } else {
            /**
             * The currently layout will use:
             * - Coordinate values (X and Y)
             * - Size - the size of the brush at any point of the stroke
             */
            return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE
            )
        }
    }

    override val touchCalculator: Calculator = { previous, current, next ->
        // Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            initialValue = 10f,
            finalValue = 10f,
            minValue = 17.2f,
            maxValue = 10f,
            minSpeed = 182f,
            maxSpeed = 3547f,
            remap = { v: Float -> MathUtils.power(v, 1.19f) })

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        PathPoint(current.x, current.y, size = size)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        // Use the following to compute size based on pressure:
        var size = current.computeValueBasedOnPressure(
            maxValue = 17.2f,
            minValue = 10f,
            minPressure = 182f,
            maxPressure = 3547f,
            remap = { v: Float -> MathUtils.power(v, 1.19f) })

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        PathPoint(current.x, current.y, size = size)
    }

}