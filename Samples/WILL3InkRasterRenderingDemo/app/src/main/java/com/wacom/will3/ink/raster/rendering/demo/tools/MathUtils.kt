/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.tools

class MathUtils {

    companion object {
        fun power(v: Float, p: Float): Float {
            return Math.pow(v.toDouble(), p.toDouble()).toFloat()
        }

        fun periodic(v: Float, p: Float): Float {
            return (0.5 - 0.5 * Math.cos(p * Math.PI * v)).toFloat()
        }

        fun sigmoid(t: Float, k: Float): Float {
            return (1 + k) * t / (Math.abs(t) + k)
        }

        fun sigmoid1(v: Float, p: Float, minValue: Float = 0.0f, maxValue: Float = 1.0f): Float {
            val middle: Float = (maxValue + minValue) * 0.5f
            val halInterval = (maxValue - minValue) * 0.5f
            val t = (v - middle) / halInterval
            val s = sigmoid(t, p)
            return middle + s * halInterval
        }

        fun mapTo(value: Float, src: Range, dest: Range): Float {
            return (value - src.min) / (src.max - src.min) * (dest.max - dest.min) + dest.min;
        }

        fun mapTo(value: Float, src: Range, dest: Range, remapFunction: (Float) -> Float): Float {
            if (remapFunction != null) {
                var newSrc = Range(remapFunction(src.min), remapFunction(src.max))
                return mapTo(remapFunction(value), newSrc, dest)
            } else {
                return mapTo(value, src, dest)
            }
        }
    }
}