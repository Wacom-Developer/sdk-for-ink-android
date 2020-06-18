/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.vector

import android.graphics.Path
import com.wacom.ink.DIPolygon
import com.wacom.ink.pipeline.base.DataSequenceProcessor
import com.wacom.ink.pipeline.base.ProcessorResult

/**
 * Class that is designed to be the final part of the pipeline. It takes polygons as input and
 * produces Android Paths.
 */
class PolygonToBezierPathProducer : DataSequenceProcessor<ArrayList<DIPolygon>, Path>() {
    init {
        allData = Path()
    }

    /**
     * Add data.
     *
     * @param addition - the addition
     * @param prediction - the prediction
     * @return pair of path and predicted path
     */
    override fun add(
        addition: ArrayList<DIPolygon>?,
        prediction: ArrayList<DIPolygon>?
    ): ProcessorResult<Path?> {
        val addedPath: Path?
        if (addition != null) {
            addedPath = generateBezierPolys(addition)
            if (addedPath != null) {
                allData!!.addPath(addedPath)
            }
        } else {
            addedPath = null
        }

        val predictedPath: Path?
        if (prediction != null) {
            predictedPath = generateBezierPolys(prediction)
        } else {
            predictedPath = null
        }

        return ProcessorResult(addedPath, predictedPath)
    }

    /**
     * Reset the path producer.
     *
     */
    override fun reset() {
        allData?.reset()
    }

    /**
     * Generate Bezier Path.
     *
     * @param polygons - input polygons
     * @return path
     */
    private fun generateBezierPolys(polygons: ArrayList<DIPolygon>): Path? {
        if (polygons.size <= 0) {
            return null
        }

        val result = Path()

        for (poly in polygons) {
            for (j in 0 until poly.size step 2) {
                if (j == 0) {
                    result.moveTo(poly[j], poly[j + 1])
                } else {
                    result.lineTo(poly[j], poly[j + 1])
                }
            }
            result.close()
        }
        return result
    }
}