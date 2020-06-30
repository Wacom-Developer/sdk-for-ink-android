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
import java.lang.Math.abs
import kotlin.math.cos
import kotlin.math.pow

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
             * - For tilt effect the rotation, scale y and offset y
             */
            return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE,
                PathPoint.Property.ROTATION,
                PathPoint.Property.SCALE_X,
                PathPoint.Property.OFFSET_X
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
            minValue = 3f,
            maxValue = 7f,
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
        var size = if (current.force == -1f) {
            current.computeValueBasedOnSpeed(
                previous,
                next,
                minValue = 1f,
                maxValue = 5f,
                minSpeed = 0f,
                maxSpeed = 3500f,
                remap = { v: Float -> v.toDouble().pow(1.17).toFloat() }
            )

        } else {
            current.computeValueBasedOnPressure(
                minValue = 1f,
                maxValue = 5f,
                minPressure = 0.0f,
                maxPressure = 1.0f,
                remap = { v: Float -> v.toDouble().pow(1.17).toFloat() }
            )
        }
        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }
        val cosAltitudeAngle = abs(cos(current.altitudeAngle!!))

        val tiltScale = 1.5f * cosAltitudeAngle
        val scaleX = 1.0f + tiltScale
        val offsetX = size * tiltScale
        val rotation = current.computeNearestAzimuthAngle(previous)
        PathPoint(
            current.x, current.y,
            size = size, rotation = rotation,
            scaleX = scaleX, offsetX = offsetX)
    }

}