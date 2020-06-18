/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.tools.vector

import com.wacom.ink.Calculator
import com.wacom.ink.PathPoint
import com.wacom.ink.PathPointLayout
import com.wacom.will3.ink.vector.rendering.demo.brush.BrushPalette

class SelectorWholeStrokeTool : VectorTool() {

    companion object {
        val uri = "tool@selector_whole_stroke"
    }

    override var brush = BrushPalette.basic()

    override fun getLayout(): PathPointLayout {
        return PathPointLayout(
            PathPoint.Property.X,
            PathPoint.Property.Y,
            PathPoint.Property.SIZE,
            PathPoint.Property.RED,
            PathPoint.Property.GREEN,
            PathPoint.Property.BLUE
        )
    }

    override var drawingMode = DrawingMode.SELECTING_WHOLE_STROKE

    override val touchCalculator: Calculator = { previous, current, next ->
        //Use the following to compute size based on speed:
        PathPoint(current.x, current.y, size = 3f, red = 1f, green = 1f, blue = 1f)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        //Use the following to compute size based on speed:
        PathPoint(current.x, current.y, size = 3f, red = 1f, green = 1f, blue = 1f)
    }
}