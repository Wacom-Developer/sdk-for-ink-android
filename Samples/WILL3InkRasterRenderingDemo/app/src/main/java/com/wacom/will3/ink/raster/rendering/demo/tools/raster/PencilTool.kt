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

class PencilTool(context: Context) : RasterTool(context) {

    companion object {
        val uri = URIBuilder.getToolURI("raster", "pencil")
    }

    override var brush = BrushPalette.pencil(context)
    var previousSize = 6f
    var previousAlpha = 0.2f

    override fun getLayout(): PathPointLayout {
        if (isStylus) {
            return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE,
                PathPoint.Property.ALPHA,
                PathPoint.Property.OFFSET_X,
                PathPoint.Property.OFFSET_Y
            )
        } else {
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
            initialValue = 6f,
            finalValue = 10f,
            minValue = 6f,
            maxValue = 10f,
            minSpeed = 80f,
            maxSpeed = 1400f
        )

        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        var alpha = current.computeValueBasedOnSpeed(
            previous,
            next,
            initialValue = 0.2f,
            finalValue = 4f,
            minValue = 0.2f,
            maxValue = 4f,
            minSpeed = 80f,
            maxSpeed = 1400f
        )

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
            minValue = 6f,
            maxValue = 10f,
            minPressure = 80f,
            maxPressure = 1400f
        )!!

        //the alpha channel is going to be based on pressure
        var alpha = current.computeValueBasedOnPressure(
            minValue = 0.2f,
            maxValue = 4f,
            minPressure = 80f,
            maxPressure = 1400f
        )

        // if we tilt the pen we have bigger size
        size += MathUtils.mapTo(current.altitudeAngle!!, Range(0f, Math.PI.toFloat()/2), Range(0f, 10f))

        val offsetX = size * Math.sin(-current.azimuthAngle!!.toDouble()).toFloat() * 0.5f
        val offsetY = size * Math.cos(current.azimuthAngle!!.toDouble()).toFloat() * 0.5f

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