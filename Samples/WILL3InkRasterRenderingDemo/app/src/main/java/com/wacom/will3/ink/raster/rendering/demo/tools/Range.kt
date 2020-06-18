/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.tools

data class Range(val min: Float, val max: Float) {

    fun clamp(value: Float): Float {
        return Math.min(Math.max(value, min), max);
    }
}