/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.vector

import android.graphics.Path
import com.wacom.ink.PathSegment
import com.wacom.ink.Phase
import com.wacom.ink.PointerData
import com.wacom.ink.Spline
import com.wacom.ink.pipeline.*
import com.wacom.ink.pipeline.base.ProcessorResult
import com.wacom.will3.ink.vector.rendering.demo.tools.Tool
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.VectorTool

/**
 * VectorInkBuilder is a class that handles the stroke building, using the geometry pipeline.
 * This ink builder processes the input to the whole pipeline in order to produce polygons from the
 * input. After that the polygons are converted into Android Paths.
 *
 * The pipeline processing for vector ink has the following stages:
 * - PathProducer
 * - Smoother
 * - SplineProducer
 * - SplineInterpolator
 * - BrushApplier
 * - ConvexHullChainProducer
 * - PolygonMerger
 * - PolygonSimplifier
 * - PolygonToBezierPathProducer
 */
class VectorInkBuilder() {

    // The pipeline components:
    internal lateinit var pathProducer: PathProducer
    internal lateinit var smoother: SmoothingFilter
    internal lateinit var splineProducer: SplineProducer
    internal lateinit var splineInterpolator: SplineInterpolator
    internal lateinit var brushApplier: BrushApplier
    internal lateinit var convexHullChainProducer: ConvexHullChainProducer
    internal lateinit var polygonMerger: PolygonMerger
    internal lateinit var bezierPathBuilder: PolygonToBezierPathProducer
    internal lateinit var pathSegment: PathSegment

    lateinit var tool: VectorTool

    fun updatePipeline(newTool: Tool) {
        if (newTool !is VectorTool) {
            throw IllegalArgumentException("Invalid tool")
        } else {
            tool = newTool
            var layout = tool.getLayout()
            pathProducer = PathProducer(layout, tool.getCalculator())
            smoother = SmoothingFilter(
                layout.count(),     // Dimension
                30
            )                 // Moving average window size
            splineProducer = SplineProducer(layout) // Layout
            splineInterpolator = CurvatureBasedInterpolator(false,  false)  // Interpolate by length
            brushApplier = BrushApplier(
                tool.brush,             // Vector brush
                true
            )        // Keep all data
            convexHullChainProducer = ConvexHullChainProducer()
            polygonMerger = PolygonMerger()
            bezierPathBuilder = PolygonToBezierPathProducer()
            pathSegment = PathSegment()
            splineProducer.keepAllData = true
        }
    }

    fun updateInputMethod(isStylus: Boolean) {
        tool.isStylus = isStylus
        var layout = tool.getLayout()
        pathProducer = PathProducer(layout, tool.getCalculator())
        smoother = SmoothingFilter(layout.count(), 30)
        splineProducer = SplineProducer(layout)
        splineInterpolator = CurvatureBasedInterpolator(false, false)
        brushApplier = BrushApplier(tool.brush, true)
        convexHullChainProducer = ConvexHullChainProducer()
        polygonMerger = PolygonMerger()
        bezierPathBuilder = PolygonToBezierPathProducer()
        pathSegment = PathSegment()
        splineProducer.keepAllData = true
    }

    /**
     * Add data to the VectorInkBuilder.
     *
     * @param phase - the phase of the input
     * @param addition - the addition
     * @param prediction - the prediction (if available)
     */
    fun add(phase: Phase, addition: PointerData?, prediction: PointerData?) {
        val (addedGeometry, predictedGeometry) = pathProducer.add(phase, addition, prediction)
        pathSegment.add(phase, addedGeometry, predictedGeometry)
    }

    /**
     * Build spline from the data accumulated through ethe add calls.
     * @return pair of path and predicted path
     */
    fun buildSpline(): ProcessorResult<Spline?> {
        if (pathSegment.accumulatedAddition.size == 0) return ProcessorResult(null, null)

        val isFirst = pathSegment.isFirst
        val isLast = pathSegment.isLast

        val (addedSmoothGeometry, predictedSmoothGeometry) = smoother.add(
            isFirst,
            isLast,
            pathSegment.accumulatedAddition,
            pathSegment.lastPrediction
        )
        pathSegment.reset()

        val result =
            splineProducer.add(isFirst, isLast, addedSmoothGeometry, predictedSmoothGeometry)

        return result
    }

    fun buildPath(
        addedSpline: Spline,
        predictedSpline: Spline?,
        isFirst: Boolean,
        isLast: Boolean
    ): ProcessorResult<Path?> {
        val (addedPoints, predictedPoints) = splineInterpolator.add(
            isFirst,
            isLast,
            addedSpline,
            predictedSpline
        )
        val (addedPolys, predictedPolys) = brushApplier.add(
            isFirst,
            isLast,
            addedPoints,
            predictedPoints
        )
        val (addedHulls, predictedHulls) = convexHullChainProducer.add(
            isFirst,
            isLast,
            addedPolys,
            predictedPolys
        )
        val (addedMerged, predictedMerged) = polygonMerger.add(
            isFirst,
            isLast,
            addedHulls,
            predictedHulls
        )

        return bezierPathBuilder.add(isFirst, isLast, addedMerged, predictedMerged)
    }
}