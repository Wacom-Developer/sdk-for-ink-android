/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.raster

import com.wacom.ink.*
import com.wacom.ink.pipeline.*
import com.wacom.ink.pipeline.base.ProcessorResult
import com.wacom.will3.ink.raster.rendering.demo.tools.raster.RasterTool

/**
 * RasterInkBuilder is a class that handles the Path building for the Raster Ink, using the geometry
 * pipeline.
 *
 * Pipeline processing for raster ink has fewer stages:
 * - PathProducer
 * - Smoother
 * - SplineProducer
 * - SplineInterpolator
 *
 */
class RasterInkBuilder() {

    // The pipeline components:
    lateinit var pathProducer: PathProducer
    lateinit var smoother: SmoothingFilter
    lateinit var splineProducer: SplineProducer
    lateinit var splineInterpolator: SplineInterpolator
    lateinit var pathSegment: PathSegment

    lateinit var tool: RasterTool

    fun updatePipeline(newTool: RasterTool) {
        tool = newTool
        var layout = tool.getLayout()
        // Path producer needs to know the layout
        pathProducer = PathProducer(layout, tool.getCalculator())
        smoother = SmoothingFilter(layout.size) // Dimension
        splineProducer = SplineProducer(layout) // Layout
        splineInterpolator = DistanceBasedInterpolator(
            spacing = tool.brush.spacing, // Spacing between two successive sample.
            splitCount = 1,               // Determines the number of iterations for the
            // discretization.
            calculateDerivatives = true,  // Calculate derivatives
            interpolateByLength = true    // Interpolate by length
        )
        pathSegment = PathSegment()

        splineProducer.keepAllData = true
    }

    fun updateInputMethod(isStylus: Boolean) {
        tool.isStylus = isStylus
        var layout = tool.getLayout()
        pathProducer = PathProducer(layout, tool.getCalculator())
        smoother = SmoothingFilter(layout.size)
        splineProducer = SplineProducer(layout)
        splineInterpolator = DistanceBasedInterpolator(
            spacing = tool.brush.spacing,
            splitCount = 1,
            calculateDerivatives = true,
            interpolateByLength = true
        )
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
        try {
            val (addedGeometry, predictedGeometry) = pathProducer.add(phase, addition, prediction)
            pathSegment.add(phase, addedGeometry, predictedGeometry)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * Build path from the data accumulated through the add calls.
     *
     * @return pair of path and predicted path
     */
    fun build(): ProcessorResult<InterpolatedSpline?> {
        val isFirst = pathSegment.isFirst
        val isLast = pathSegment.isLast

        val (smoothAddedGeometry, smoothPredictedGeomtry) = smoother.add(
            isFirst,
            isLast,
            pathSegment.accumulatedAddition,
            pathSegment.lastPrediction
        )
        pathSegment.reset()
        val (addedSpline, predictedSpline) = splineProducer.add(
            isFirst,
            isLast,
            smoothAddedGeometry,
            smoothPredictedGeomtry
        )

        val result = splineInterpolator.add(isFirst, isLast, addedSpline, predictedSpline)

        return result
    }

    fun processSpline(
        added: Spline,
        predicted: Spline?,
        isFirst: Boolean = true,
        isLast: Boolean = true
    ): ProcessorResult<InterpolatedSpline?> {
        return splineInterpolator.add(isFirst, isLast, added, predicted)
    }

}