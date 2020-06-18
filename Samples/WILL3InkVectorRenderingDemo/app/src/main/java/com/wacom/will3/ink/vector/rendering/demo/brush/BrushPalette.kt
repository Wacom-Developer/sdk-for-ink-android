/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.brush

import com.wacom.ink.FloatArrayList
import com.wacom.ink.rendering.BrushPrototype
import com.wacom.ink.rendering.ShapeFactory
import com.wacom.ink.rendering.VectorBrush

/**
 * Class collecting the all brushes which are used within the application.
 */
class BrushPalette {

    companion object {
        fun basic(): VectorBrush {
            return VectorBrush(
                URIBuilder.getBrushURI("vector", "Basic"),
                arrayOf(
                    BrushPrototype(FloatArrayList(ShapeFactory.createCircle(3)), 0.3f)
                )
            )
        }

        fun circle(): VectorBrush {
            return VectorBrush(
                URIBuilder.getBrushURI("vector", "Circle"),
                arrayOf(
                    BrushPrototype(FloatArrayList(ShapeFactory.createCircle(4)), 0f),
                    BrushPrototype(FloatArrayList(ShapeFactory.createCircle(8)), 2f),
                    BrushPrototype(FloatArrayList(ShapeFactory.createCircle(16)), 6f),
                    BrushPrototype(FloatArrayList(ShapeFactory.createCircle(32)), 18f)
                )
            )
        }
    }
}