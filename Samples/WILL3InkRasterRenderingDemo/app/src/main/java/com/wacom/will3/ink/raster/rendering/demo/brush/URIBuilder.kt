/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.brush

/**
 * Building a URI according the URI scheme
 */
class URIBuilder {

    companion object {
        private const val SAMPLE = "will3-raster-ink-android-demo"

        // Type - raster | vector
        @JvmStatic
        fun getBrushURI(type: String, name: String): String {
            return "app://$SAMPLE/${type}-brush/${name}".format(SAMPLE, type, name)
        }

        @JvmStatic
        fun getToolURI(type: String, name: String): String {
            return "app://$SAMPLE/${type}-tool/${name}".format(SAMPLE, type, name)
        }
    }
}