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
import kotlin.math.*


class CrayonTool(context: Context) : RasterTool(context) {

    companion object {
        val uri = URIBuilder.getToolURI("raster", "crayon")
        // Minimum size of the pencil tip
        val MIN_CRAYON_SIZE = 25f
        // Maximum size of the pencil tip
        val MAX_CRAYON_SIZE = 50f
        // Minimum alpha values for the particles
        val MIN_ALPHA = 0.1f
        // Maximum alpha values for the particles
        val MAX_ALPHA = 0.7f
        //  Unit for speed is px/second.
        //  NOTE: This needs to be optimized for different Pixel densities of different devices
        val MAX_SPEED = 15000f
        // Half PI value
        val PI_HALF = (PI / 2f).toFloat()
        // Minimum altitude angle seen in experiments
        val MIN_ALTITUDE_ANGLE = 0.4
    }

    override var brush = BrushPalette.crayonbrush(context)
    var previousSize = MIN_CRAYON_SIZE
    var previousAlpha = 0.1f

    override fun getLayout(): PathPointLayout {
       return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE,
                PathPoint.Property.ALPHA
            )
    }

    override val touchCalculator: Calculator = { previous, current, next ->
        // Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = MIN_CRAYON_SIZE,
            maxValue = MAX_CRAYON_SIZE,
            minSpeed = 10f,
            maxSpeed = MAX_SPEED,
            // reverse behaviour
            remap = { 1f - it}
        )
        if (size == null) {
            size = previousSize
        } else {
            previousSize = size
        }

        var alpha = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = MIN_ALPHA,
            maxValue = MAX_ALPHA,
            minSpeed = 10f,
            maxSpeed = MAX_SPEED,
            // reverse behaviour
            remap = { 1f - it}
        )
        if (alpha == null) {
            alpha = previousAlpha
        } else {
            previousAlpha = alpha
        }
        PathPoint(current.x, current.y, size = size, alpha = alpha)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        // calculate the offset of the pencil tip due to tilted position
        val cosAltitudeAngle = cos(current.altitudeAngle!!)
        val sinAzimuthAngle = sin(current.azimuthAngle!!)
        val cosAzimuthAngle = cos(current.azimuthAngle!!)
        val x = sinAzimuthAngle * cosAltitudeAngle
        val y = cosAltitudeAngle * cosAzimuthAngle
        val offsetY = 5f * -x
        val offsetX = 5f * -y
        // compute the rotation
        val rotation = current.computeNearestAzimuthAngle(previous)
        // Normalize the tilt be minimum seen altitude angle and the maximum with the pen straight up
        val tiltScale = min(1f,
            ((PencilTool.PI_HALF - current.altitudeAngle!!) / (PencilTool.PI_HALF - PencilTool.MIN_ALTITUDE_ANGLE).toFloat()))

        val size = max(
            CrayonTool.MIN_CRAYON_SIZE, CrayonTool.MIN_CRAYON_SIZE
                    + (CrayonTool.MAX_CRAYON_SIZE - CrayonTool.MIN_CRAYON_SIZE)
                * tiltScale)
        // Change the intensity of alpha value by pressure of speed, if available else use speed
        var alpha = if (current.force == -1f) {
            current.computeValueBasedOnSpeed(
                previous,
                next,
                minValue = PencilTool.MIN_ALPHA,
                maxValue = PencilTool.MAX_ALPHA,
                minSpeed = 0f,
                maxSpeed = 3500f,
                // reverse behaviour
                remap = { 1.0f - it}
            )
        } else {
            current.computeValueBasedOnPressure(
                minValue = PencilTool.MIN_ALPHA,
                maxValue = PencilTool.MAX_ALPHA,
                minPressure = 0.0f,
                maxPressure = 1.0f,
                remap = { v: Float -> v.toDouble().pow(1).toFloat() }
            )
        }
        if (alpha == null) {
            alpha = previousAlpha
        } else {
            previousAlpha = alpha
        }
        PathPoint(
            current.x, current.y,
            alpha = alpha, size = size, rotation = rotation,
            offsetX = offsetX, offsetY = offsetY
        )
    }
}