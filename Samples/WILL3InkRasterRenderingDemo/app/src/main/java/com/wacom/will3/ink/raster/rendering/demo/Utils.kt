/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.MotionEvent
import com.wacom.ink.Phase
import com.wacom.ink.PointerData
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.rendering.RasterBrush
import com.wacom.ink.format.rendering.Style
import com.wacom.ink.format.tree.nodes.StrokeNode
import com.wacom.ink.rasterization.ParticleBrush
import com.wacom.ink.rasterization.RotationMode
import com.wacom.ink.rendering.BlendMode
import com.wacom.will3.ink.raster.rendering.demo.tools.raster.*
import com.wacom.will3.ink.raster.rendering.demo.tools.Tool
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

    return PointerData(
        this.x,
        this.y,
        phase,
        timestamp = this.eventTime,
        force = this.pressure,
        altitudeAngle = getAxisValue(MotionEvent.AXIS_TILT),
        azimuthAngle = orientation
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

    return PointerData(
        getHistoricalX(index), getHistoricalY(index),
        phase, timestamp = getHistoricalEventTime(index),
        force = getHistoricalPressure(index),
        altitudeAngle = getHistoricalAxisValue(MotionEvent.AXIS_TILT, index),
        azimuthAngle = orientation
    )
}

fun MotionEvent.resolveToolType(): InkInputType {
    return when (this.getToolType(0)) {
        MotionEvent.TOOL_TYPE_STYLUS -> InkInputType.PEN
        MotionEvent.TOOL_TYPE_FINGER -> InkInputType.TOUCH
        else -> InkInputType.PEN
    }
}

fun com.wacom.ink.format.enums.RotationMode.convert(): RotationMode {
    return when (this) {
        com.wacom.ink.format.enums.RotationMode.NONE -> RotationMode.NONE
        com.wacom.ink.format.enums.RotationMode.RANDOM -> RotationMode.RANDOM
        com.wacom.ink.format.enums.RotationMode.TRAJECTORY -> RotationMode.TRAJECTORY
    }
}

fun RasterBrush.toParticleBrush(): ParticleBrush {
    val pb = ParticleBrush()
    pb.blendMode = blendMode
    pb.scattering = scattering
    pb.rotationMode = rotationMode.convert()

    val opts = BitmapFactory.Options()
    opts.inSampleSize = 1
    opts.inScaled = false


    val shapeTextures = Array<Bitmap>(shapeTextures.size) {
        val bytes = shapeTextures[it]
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
    }

    val fillTexture = BitmapFactory.decodeByteArray(fillTexture, 0, fillTexture.size, opts)

    pb.allocateTextures(shapeTextures, arrayOf(fillTexture), fillWidth.toInt(), fillHeight.toInt())

    return pb
}

fun Tool.uri(): String {
    when (this) {
        is CrayonTool -> return CrayonTool.uri
        is EraserRasterTool -> return EraserRasterTool.uri
        is InkBrushTool -> return InkBrushTool.uri
        is PencilTool -> return PencilTool.uri
        is WaterbrushTool -> return WaterbrushTool.uri
    }

    return return PencilTool.uri //default value
}

fun StrokeNode.createRasterTool(context: Context): RasterTool {
    return when (data.style!!.brushURI) {
        CrayonTool.uri -> CrayonTool(context)
        EraserRasterTool.uri -> EraserRasterTool(context)
        InkBrushTool.uri -> InkBrushTool(context)
        WaterbrushTool.uri -> WaterbrushTool(context)
        //PencilTool.uri -> return PencilTool(context)
        else -> PencilTool(context) //default tool
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

fun BlendMode.uri(): String {
    return when (name) {
        "SOURCE_OVER" -> "will://rasterization/3.0/blend-mode/SourceOver"
        "DESTINATION_OVER" -> "will://rasterization/3.0/blend-mode/DestinationOver"
        "DESTINATION_IN" -> "will://rasterization/3.0/blend-mode/DestinationIn"
        "DESTINATION_OUT" -> "will://rasterization/3.0/blend-mode/DestinationOut"
        "LIGHTER" -> "will://rasterization/3.0/blend-mode/Lighter"
        "COPY" -> "will://rasterization/3.0/blend-mode/Copy"
        "MIN" -> "will://rasterization/3.0/blend-mode/Min"
        "MAX" -> "will://rasterization/3.0/blend-mode/Max"
        else -> "will://rasterization/3.0/blend-mode/SourceOver"
    }
}
