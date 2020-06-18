/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.tools.raster

import android.content.Context
import com.wacom.ink.Calculator
import com.wacom.ink.PathPoint
import com.wacom.ink.PathPointLayout
import com.wacom.ink.rendering.BlendMode
import com.wacom.will3.ink.raster.rendering.demo.brush.BrushPalette
import com.wacom.will3.ink.raster.rendering.demo.brush.URIBuilder

class EraserRasterTool(context: Context) : RasterTool(context) {

    companion object {
        val uri = URIBuilder.getToolURI("raster", "eraser")
    }

    override var brush = BrushPalette.eraser(context)

    override fun getLayout(): PathPointLayout {
        return PathPointLayout(
            PathPoint.Property.X,
            PathPoint.Property.Y,
            PathPoint.Property.SIZE,
            PathPoint.Property.RED,
            PathPoint.Property.GREEN,
            PathPoint.Property.BLUE,
            PathPoint.Property.ALPHA
        )
    }

    override val touchCalculator: Calculator = { previous, current, next ->
        // Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = 8f,
            maxValue = 112f,
            minSpeed = 720f,
            maxSpeed = 3900f
        )
        if (size == null) size = 8f

        PathPoint(current.x, current.y, size = size, red = 1f, green = 1f, blue = 1f, alpha = 1f)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        // Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = 8f,
            maxValue = 112f,
            minSpeed = 720f,
            maxSpeed = 3900f
        )
        if (size == null) size = 8f

        PathPoint(current.x, current.y, size = size, red = 1f, green = 1f, blue = 1f, alpha = 1f)
    }

    override fun getBlendMode(): BlendMode {
        return BlendMode.DESTINATION_OUT
    }

}