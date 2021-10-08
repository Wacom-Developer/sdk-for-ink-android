/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.vector

import android.graphics.Color
import android.graphics.Path
import com.wacom.ink.Spline
import com.wacom.ink.format.rendering.PathPointProperties
import com.wacom.ink.format.rendering.Style
import com.wacom.ink.format.tree.data.SensorData
import com.wacom.ink.format.tree.groups.StrokeGroupNode
import com.wacom.ink.format.tree.nodes.StrokeNode
import com.wacom.ink.model.IdentifiableImpl
import com.wacom.ink.model.Identifier
import com.wacom.ink.model.InkStroke
import com.wacom.ink.model.StrokeAttributes
import com.wacom.ink.rendering.VectorBrush
import kotlinx.coroutines.launch
import java.util.*

data class WillStroke(
        override val spline: Spline,
        override val vectorBrush: VectorBrush,
        val zOrder: Int
) : InkStroke {
    override var id: Identifier = Identifier()

    override var strokeAttributes: StrokeAttributes = object: StrokeAttributes {
        override var size: Float = 0f
        override var rotation: Float = 0.0f

        override var scaleX: Float = 1.0f
        override var scaleY: Float = 1.0f
        override var scaleZ: Float = 1.0f

        override var offsetX: Float = 0.0f
        override var offsetY: Float = 0.0f
        override var offsetZ: Float = 0.0f

        override var red: Float = 0.0f
        override var green: Float = 0.0f
        override var blue: Float = 0.0f
        override var alpha: Float = 1.0f
    }

    override var sensorDataOffset: Int = 0
    override var sensorDataMappings: Array<Int> = arrayOf()

    //TODO start collecting sensor data
    var sensorData: SensorData? = null
    var path: Path = Path()

    var strokeNode: StrokeNode? = null
    var originalStrokeId : Identifier = id

    fun createStyle(): Style {
        return Style(vectorBrush.name, 1, props = PathPointProperties(
            size = strokeAttributes.size,
            rotation = strokeAttributes.rotation,
            scaleX = strokeAttributes.scaleX,
            scaleY = strokeAttributes.scaleY,
            scaleZ = strokeAttributes.scaleZ,
            offsetX = strokeAttributes.offsetX,
            offsetY = strokeAttributes.offsetY,
            offsetZ = strokeAttributes.offsetZ,
            red = strokeAttributes.red,
            green = strokeAttributes.green,
            blue =   strokeAttributes.blue,
            alpha =  strokeAttributes.alpha
        ), renderModeUri = "will://rasterization/3.0/blend-mode/SourceOver")
    }

    protected fun finalize() {
        if (strokeNode != null) {
            var groupNode = strokeNode!!.parent
            if ((groupNode != null) && (groupNode is StrokeGroupNode)) {
                groupNode.remove(strokeNode!!)
            }
        }
    }

    fun clone() : WillStroke {
        val stroke = WillStroke(spline, vectorBrush, zOrder)
        stroke.sensorData = sensorData
        stroke.id = id
        stroke.strokeAttributes = strokeAttributes
        stroke.path = path

        return stroke
    }
}