/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.vector

import com.wacom.ink.Spline
import com.wacom.ink.format.tree.data.Stroke
import com.wacom.ink.format.tree.nodes.StrokeNode
import com.wacom.ink.model.IdentifiableImpl
import com.wacom.ink.model.Identifier
import com.wacom.ink.model.InkStroke
import com.wacom.ink.model.InkStrokeFactory
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke

class WillStrokeFactory: InkStrokeFactory {
    override fun createStroke(
        newSpline: Spline, originalStroke: InkStroke,
        firstPointIndex: Int, pointsCount: Int): InkStroke {
        originalStroke as WillStroke
        val stroke = WillStroke(newSpline, originalStroke.vectorBrush, originalStroke.zOrder)

        stroke.strokeAttributes = originalStroke.strokeAttributes
        stroke.sensorData = originalStroke.sensorData
        stroke.sensorDataOffset = firstPointIndex

        stroke.strokeNode = StrokeNode(Stroke(stroke.id, stroke.spline, stroke.createStyle()))
        //val group = originalStroke.strokeNode!!.parent as StrokeGroupNode

        //group.add(stroke.strokeNode!!)

        stroke.originalStrokeId = originalStroke.originalStrokeId
        if (stroke.sensorData != null) {
            stroke.strokeNode!!.data.sensorDataID = stroke.sensorData!!.id
            stroke.strokeNode!!.data.sensorDataOffset = stroke.sensorDataOffset
        }

        return stroke
    }
}