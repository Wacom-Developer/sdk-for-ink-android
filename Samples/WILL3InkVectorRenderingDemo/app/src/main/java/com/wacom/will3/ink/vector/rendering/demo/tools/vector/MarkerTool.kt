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
import com.wacom.will3.ink.vector.rendering.demo.tools.MathUtils
import com.wacom.will3.ink.vector.rendering.demo.tools.Range

class MarkerTool : VectorTool() {

    companion object {
        val uri = URIBuilder.getToolURI("vector", "marker")
    }

    override var brush = BrushPalette.circle()
    override var alphaValue = 0.25f

    override fun getLayout(): PathPointLayout {
        if (isStylus) {
            /**
             * The currently layout will use:
             * - Coordinate values (X and Y)
             * - Size - the size of the brush at any point of the stroke
             * - OffsetX - the x offset of the stroke
             * - OffsetY - the y offset of the stroke
             */
            return PathPointLayout(
                PathPoint.Property.X,
                PathPoint.Property.Y,
                PathPoint.Property.SIZE
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
                PathPoint.Property.SIZE
            )
        }
    }

    override val touchCalculator: Calculator = { previous, current, next ->
        PathPoint(current.x, current.y, size = 10f)
    }

    override val stylusCalculator: Calculator = { previous, current, next ->
        //we are going to base the size of the stroke on the pen tilt
        //so the more the tilt the more the thick
        var size = MathUtils.mapTo(current.altitudeAngle!!, Range(0f, Math.PI.toFloat()/2), Range(10f, 30f))

        PathPoint(
            current.x,
            current.y,
            size = size
        )
    }

}