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

class PencilTool(context: Context) : RasterTool(context) {
    private var previousSize = MIN_PENCIL_SIZE

    companion object {
        val uri = URIBuilder.getToolURI("raster", "pencil")

        // Minimum size of the pencil tip
        val MIN_PENCIL_SIZE = 4f

        // Maximum size of the pencil tip
        val MAX_PENCIL_SIZE = 25f

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

    override var brush = BrushPalette.pencil(context)
    var previousAlpha = 0.2f

    override fun getLayout(): PathPointLayout {
        // Define different layouts for stylus and touch input
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
            minValue = MIN_PENCIL_SIZE,
            maxValue = MAX_PENCIL_SIZE,
            minSpeed = 80f,
            maxSpeed = MAX_SPEED,
            // reverse behaviour
            remap = { 1f - it }
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
            minSpeed = 80f,
            maxSpeed = MAX_SPEED,
            // reverse behaviour
            remap = { 1f - it }
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
            ((PI_HALF - current.altitudeAngle!!) / (PI_HALF - MIN_ALTITUDE_ANGLE).toFloat()))
        // now, based on the tilt of the pencil the size of the brush size is increasing, as the
        // pencil tip is covering a larger area
        val size = max( MIN_PENCIL_SIZE, MIN_PENCIL_SIZE + (MAX_PENCIL_SIZE - MIN_PENCIL_SIZE)
                * tiltScale)
        // Change the intensity of alpha value by pressure of speed, if available else use speed
        var alpha = if (current.force == -1f) {
            current.computeValueBasedOnSpeed(
                previous,
                next,
                minValue = MIN_ALPHA,
                maxValue = MAX_ALPHA,
                minSpeed = 0f,
                maxSpeed = MAX_SPEED,
                // reverse behaviour
                remap = { 1.0f - it }
            )
        } else {
            current.computeValueBasedOnPressure(
                minValue = MIN_ALPHA,
                maxValue = MAX_ALPHA,
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