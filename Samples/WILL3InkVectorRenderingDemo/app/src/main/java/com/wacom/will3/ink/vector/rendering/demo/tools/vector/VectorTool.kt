/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.tools.vector

import com.wacom.ink.Calculator
import com.wacom.ink.PathPoint
import com.wacom.ink.PathPointLayout
import com.wacom.will3.ink.vector.rendering.demo.brush.BrushPalette
import com.wacom.will3.ink.vector.rendering.demo.tools.Tool

open class VectorTool : Tool() {
    enum class DrawingMode {
        DRAWING,
        ERASING_PARTIAL_STROKE, ERASING_WHOLE_STROKE,
        SELECTING_PARTIAL_STROKE, SELECTING_WHOLE_STROKE
    }

    open var brush = BrushPalette.basic() // default brush
    open var alphaValue = 1f

    open var drawingMode = DrawingMode.DRAWING

    override fun getLayout(): PathPointLayout {
        return PathPointLayout(
            PathPoint.Property.X,
            PathPoint.Property.Y,
            PathPoint.Property.SIZE
        )
    }

    override val touchCalculator: Calculator = { previous, current, next ->
        //Use the following to compute size based on speed:
        var size = current.computeValueBasedOnSpeed(
            previous,
            next,
            minValue = 2f,
            maxValue = 6f,
            minSpeed = 100f,
            maxSpeed = 4000f,
            remap = { v -> v }
        )

        if (size == null) size = 2.0f

        PathPoint(current.x, current.y, size = size)
    }


}