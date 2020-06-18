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

class FeltTool : VectorTool() {

    companion object {
        val uri = URIBuilder.getToolURI("vector", "felt")
    }

    override var brush = BrushPalette.circle()
    var previousSize = 2f

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
            initialValue = 3f,
            finalValue = 3f,
            minValue = 7f,
            maxValue = 3f,
            minSpeed = 80f,
            maxSpeed = 1400f,
            remap = { v: Float -> MathUtils.power(v, 0.65f) })

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        PathPoint(current.x, current.y, size = size)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            initialValue = 2f,
            finalValue = 2f,
            minValue = 6f,
            maxValue = 2f,
            minSpeed = 80f,
            maxSpeed = 1400f,
            remap = { v: Float -> MathUtils.power(v, 0.65f) })

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        //we add some values on pressure
        size += (current.force!! * 3)


        PathPoint(current.x, current.y, size = size)
    }

}