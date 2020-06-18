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
import com.wacom.will3.ink.raster.rendering.demo.tools.MathUtils
import com.wacom.will3.ink.raster.rendering.demo.tools.Range


class CrayonTool(context: Context) : RasterTool(context) {

    companion object {
        val uri = URIBuilder.getToolURI("raster", "crayon")
    }

    override var brush = BrushPalette.crayonbrush(context)
    var previousSize = 18f
    var previousAlpha = 0.1f

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
            minValue = 18f,
            maxValue = 28f,
            minSpeed = 10f,
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
            minValue = 0.1f,
            maxValue = 0.6f,
            minSpeed = 10f,
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
        //we are going to base the size of the stroke on the pen tilt
        //so the more the tilt the more the thick
        var size = MathUtils.mapTo(current.altitudeAngle!!, Range(0f, Math.PI.toFloat()/2), Range(10f, 100f))
        val offsetX = size * Math.sin(-current.azimuthAngle!!.toDouble()).toFloat() * 0.5f
        val offsetY = size * Math.cos(current.azimuthAngle!!.toDouble()).toFloat() * 0.5f

        //the alpha channel is going to be based on pressure
        var alpha = MathUtils.mapTo(current.force!!, Range(0f, 1f), Range(0.1f, 0.6f))

        PathPoint(
            current.x,
            current.y,
            size = size,
            alpha = alpha,
            offsetX = offsetX,
            offsetY = -offsetY
        )
    }
}