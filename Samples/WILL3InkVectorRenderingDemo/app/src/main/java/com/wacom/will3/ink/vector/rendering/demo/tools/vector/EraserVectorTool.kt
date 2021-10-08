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

class EraserVectorTool : VectorTool() {

    companion object {
        val uri = URIBuilder.getToolURI("vector", "eraser")
    }

    override var brush = BrushPalette.circle()

    override fun getLayout(): PathPointLayout {
        return PathPointLayout(
            PathPoint.Property.X,
            PathPoint.Property.Y,
            PathPoint.Property.SIZE
        )
    }

    override var drawingMode = DrawingMode.ERASING_PARTIAL_STROKE

    override val touchCalculator: Calculator = { previous, current, next ->
        //Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = 8f,
            maxValue = 112f,
            minSpeed = 720f,
            maxSpeed = 3900f
        )
        if (size == null) size = 1.0f
        PathPoint(current.x, current.y, size = size)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        //Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = 8f,
            maxValue = 112f,
            minSpeed = 720f,
            maxSpeed = 3900f
        )
        if (size == null) size = 1.0f
        PathPoint(current.x, current.y, size = size)
    }
}