/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.tools.raster

import android.content.Context
import com.wacom.ink.rendering.BlendMode
import com.wacom.will3.ink.raster.rendering.demo.brush.BrushPalette
import com.wacom.will3.ink.raster.rendering.demo.tools.Tool

open class RasterTool(context: Context): Tool() {

    open var brush = BrushPalette.pencil(context) // default brush

    open fun getBlendMode(): BlendMode {
        return BlendMode.SOURCE_OVER
    }

}