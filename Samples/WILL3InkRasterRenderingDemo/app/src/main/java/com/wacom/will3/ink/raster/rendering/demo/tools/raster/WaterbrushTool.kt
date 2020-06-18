/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.tools.raster

import android.content.Context
import com.wacom.ink.Calculator
import com.wacom.ink.PathPoint
import com.wacom.ink.PathPointLayout
import com.wacom.will3.ink.raster.rendering.demo.brush.BrushPalette
import com.wacom.will3.ink.raster.rendering.demo.brush.URIBuilder
import com.wacom.will3.ink.raster.rendering.demo.computeValueBasedOnPressure
import com.wacom.will3.ink.raster.rendering.demo.tools.MathUtils
import com.wacom.will3.ink.raster.rendering.demo.tools.Range
import kotlin.math.cos

class WaterbrushTool(context: Context) : RasterTool(context) {

    companion object {
        val uri = URIBuilder.getToolURI("raster", "water_brush")
    }

    override var brush = BrushPalette.waterbrush(context)
    var previousSize = 28f
    var previousAlpha = 0.02f

    override fun getLayout(): PathPointLayout {
        if (isStylus) {
            /**
             * The currently layout will use:
             * - Coordinate values (X and Y)
             * - Size - the size of the brush at any point of the stroke
             * - Rotation - the rotation of the brush
             */
            return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE,
                PathPoint.Property.ALPHA,
                PathPoint.Property.OFFSET_X,
                PathPoint.Property.OFFSET_Y
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
                PathPoint.Property.SIZE,
                PathPoint.Property.ALPHA
            )
        }
    }

    override val touchCalculator: Calculator = { previous, current, next ->
        // Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            initialValue = 40f,
            finalValue = 40f,
            minValue = 60f,
            maxValue = 40f,
            minSpeed = 38f,
            maxSpeed = 1500f,
            remap = { v: Float -> MathUtils.power(v, 0.65f) })

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        var alpha = current.computeValueBasedOnSpeed(
            previous,
            next,
            initialValue = 0.05f,
            finalValue = 0.05f,
            minValue = 0.2f,
            maxValue = 0.05f,
            minSpeed = 1000f,
            maxSpeed = 1500f,
            remap = { v -> MathUtils.power(v, 0.65f) })

        if (alpha == null) {
            alpha = previousAlpha
        } else {
            previousAlpha = alpha
        }

        PathPoint(current.x, current.y, size = size, alpha = alpha)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        //the width is going to be based on pressure
        var size = current.computeValueBasedOnPressure(
            minValue = 40f,
            maxValue = 60f,
            minPressure = 38f,
            maxPressure = 1500f,
            remap = { v: Float -> MathUtils.power(v, 0.65f) })

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        // if we tilt the pen we have bigger size
        size += MathUtils.mapTo(current.altitudeAngle!!, Range(0f, Math.PI.toFloat()/2), Range(0f, 40f))

        val offsetX = size * Math.sin(-current.azimuthAngle!!.toDouble()).toFloat() * 0.5f
        val offsetY = size * Math.cos(current.azimuthAngle!!.toDouble()).toFloat() * 0.5f

        var alpha = current.computeValueBasedOnSpeed(
            previous,
            next,
            initialValue = 0.05f,
            finalValue = 0.05f,
            minValue = 0.2f,
            maxValue = 0.05f,
            minSpeed = 1000f,
            maxSpeed = 1500f,
            remap = { v -> MathUtils.power(v, 0.65f) })

        if (alpha == null) {
            alpha = previousAlpha
        } else {
            previousAlpha = alpha
        }

        PathPoint(
            current.x,
            current.y,
            size = size,
            alpha = alpha,
            offsetX = offsetX,
            offsetY = offsetY
        )
    }

}