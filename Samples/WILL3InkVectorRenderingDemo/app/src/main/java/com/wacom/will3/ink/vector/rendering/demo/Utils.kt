/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo

import android.view.MotionEvent
import com.wacom.ink.Phase
import com.wacom.ink.PointerData
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.rendering.Style
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.*
import com.wacom.will3.ink.vector.rendering.demo.tools.Tool
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min

/**
 * Converts a motion event to PointerData.
 *
 * @return the PointerData for the event.
 */
fun MotionEvent.toPointerData(): PointerData {
    val phase = when (this.action) {
        MotionEvent.ACTION_DOWN -> Phase.BEGIN
        MotionEvent.ACTION_MOVE -> Phase.UPDATE
        MotionEvent.ACTION_UP -> Phase.END
        else -> Phase.END
    }
    // compute the azimuth angle to achieve cross-platform consistency
    val orientationAngle = getOrientation(0) + PI / 2
    val azimuthAngle = if (orientationAngle > PI) {
        (orientationAngle - 2 * PI).toFloat()
    } else {
        orientationAngle.toFloat()
    }
    // compute the altitude angle to achieve cross-platform consistency
    val altitudeAngle = (PI / 2 - this.getAxisValue(MotionEvent.AXIS_TILT, 0)).toFloat()

    return PointerData(
        this.x,
        this.y,
        phase,
        timestamp = this.eventTime,
        force = this.pressure,
        altitudeAngle = altitudeAngle,
        azimuthAngle = azimuthAngle
    )
}

/**
 * Converts a historical point in the event to PointerData.
 *
 * @param index the index of the historical point
 * @return the PointerData for the historical point
 */
fun MotionEvent.historicalToPointerData(index: Int): PointerData {
    val phase = Phase.UPDATE
    val orientationAngle = getHistoricalOrientation(0, index) + PI/2
    val azimuthAngle = if (orientationAngle > PI) {
        (orientationAngle - 2 * PI).toFloat()
    } else {
        orientationAngle.toFloat()
    }
    val altitudeAngle = (PI / 2 - this.getHistoricalAxisValue(MotionEvent.AXIS_TILT, index)).toFloat()
    return PointerData(
        getHistoricalX(index), getHistoricalY(index),
        phase, timestamp = getHistoricalEventTime(index),
        force = getHistoricalPressure(index),
        altitudeAngle = altitudeAngle,
        azimuthAngle = azimuthAngle
    )
}

fun MotionEvent.resolveToolType(): InkInputType {
    return when (this.getToolType(0)) {
        MotionEvent.TOOL_TYPE_STYLUS -> InkInputType.PEN
        MotionEvent.TOOL_TYPE_FINGER -> InkInputType.TOUCH
        else -> InkInputType.PEN
    }
}

fun Tool.uri(): String {
    when (this) {
        is BrushTool -> return BrushTool.uri
        is FeltTool -> return FeltTool.uri
        is MarkerTool -> return MarkerTool.uri
        is EraserVectorTool -> return EraserVectorTool.uri
        is EraserWholeStrokeTool -> return EraserWholeStrokeTool.uri
        is SelectorWholeStrokeTool -> return SelectorWholeStrokeTool.uri
        is SelectorPartialStrokeTool -> return SelectorPartialStrokeTool.uri
    }
     return PenTool.uri //default value
}

fun Style.createVectorTool(): VectorTool {
    return when (brushURI) {
        BrushTool.uri -> BrushTool()
        FeltTool.uri -> FeltTool()
        MarkerTool.uri -> MarkerTool()
        else -> PenTool()
    }
}

/**
 * A helper method that calculates a normalized value based on the pressure of the pointer.
 *
 * @param minValue Min result value.
 * @param maxValue Max result value.
 * @param minPressure Pressure is clamped to this value if speed is below the value.
 * @param maxPressure Pressure is clamped to this value if speed is above the value.
 * @param reverse Pressure will be reversed
 * @param remap A lambda that that defines a custom transformation on the normalized speed value.
 * @return
 */
fun PointerData.computeValueBasedOnPressure(
    minValue: Float,
    maxValue: Float,
    minPressure: Float = 100f, maxPressure: Float = 4000f,
    reverse: Boolean = false,
    remap: ((Float) -> (Float))? = null
): Float? {
    val normalizePressure = if (reverse) {
        minPressure + (1-force!!) * (maxPressure - minPressure)
    } else {
        minPressure + force!! * (maxPressure - minPressure)
    }

    val pressureClamped = min(max(normalizePressure, minPressure), maxPressure)
    var k = (pressureClamped - minPressure) / (maxPressure - minPressure)

    if (remap != null)
        k = remap(k)

    return minValue + k * (maxValue - minValue)
}

