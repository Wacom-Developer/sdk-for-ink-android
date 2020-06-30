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
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class WaterbrushTool(context: Context) : RasterTool(context) {

    companion object {
        val uri = URIBuilder.getToolURI("raster", "water_brush")
        val MIN_BRUSH_SIZE = 40f
        // Maximum size of the pencil tip
        val MAX_BRUSH_SIZE = 60f
        // Minimum alpha values for the particles
        val MIN_ALPHA = 0.2f
        // Maximum alpha values for the particles
        val MAX_ALPHA = 0.5f
        //  Unit for speed is px/second.
        //  NOTE: This needs to be optimized for different Pixel densities of different devices
        val MAX_SPEED = 7500f
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
                    PathPoint.Property.ROTATION,
                    PathPoint.Property.OFFSET_X,
                    PathPoint.Property.OFFSET_Y,
                    PathPoint.Property.ALPHA
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
                minValue = MIN_BRUSH_SIZE,
                maxValue = MAX_BRUSH_SIZE,
                minSpeed = 38f,
                maxSpeed = MAX_SPEED,
                // reverse behaviour
                remap = { 1f - it})

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
                minValue = MIN_ALPHA,
                maxValue = 0.5f,
                minSpeed = 1000f,
                maxSpeed = MAX_SPEED,
                // reverse behaviour
                remap = { 1f - it})

        if (alpha == null) {
            alpha = previousAlpha
        } else {
            previousAlpha = alpha
        }

        PathPoint(current.x, current.y, size = size, alpha = alpha)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        var size = if (current.force == -1f) {
            current.computeValueBasedOnSpeed(
                    previous,
                    next,
                    minValue = 30f,
                    maxValue = 80f,
                    minSpeed = 0f,
                    maxSpeed = 3500f,
                    remap = { v: Float -> v.toDouble().pow(1.17).toFloat() }
            )

        } else {
            current.computeValueBasedOnPressure(
                    minValue = 30f,
                    maxValue = 80f,
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

        // Change the intensity of alpha value by pressure of speed
        var alpha = if (current.force == -1f) {
            current.computeValueBasedOnSpeed(
                    previous,
                    next,
                    minValue = MIN_ALPHA,
                    maxValue = MAX_ALPHA,
                    minSpeed = 0f,
                    maxSpeed = 3500f,
                    remap = { v: Float -> v.toDouble().pow(1.17).toFloat() }
            )
        } else {
            current.computeValueBasedOnPressure(
                    minValue = MIN_ALPHA,
                    maxValue = MAX_ALPHA,
                    minPressure = 0.0f,
                    maxPressure = 1.0f,
                    remap = { v: Float -> v.toDouble().pow(1.17).toFloat() }
            )
        }
        if (alpha == null) {
            alpha = previousAlpha
        } else {
            previousAlpha = alpha
        }
        val cosAltitudeAngle = cos(current.altitudeAngle!!)
        val sinAzimuthAngle = sin(current.azimuthAngle!!)
        val cosAzimuthAngle = cos(current.azimuthAngle!!)
        // calculate the offset of the pencil tip due to tilted position
        val x = sinAzimuthAngle * cosAltitudeAngle
        val y = cosAltitudeAngle * cosAzimuthAngle
        val offsetY = 5f * -x
        val offsetX = 5f * -y

        val rotation = current.computeNearestAzimuthAngle(previous)
        PathPoint(
                current.x, current.y,
                alpha = alpha,
                size = size,
                rotation = rotation,
                offsetX = offsetX,
                offsetY = offsetY)
    }
}