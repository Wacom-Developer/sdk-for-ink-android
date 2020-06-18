/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.brush

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.wacom.ink.format.enums.RotationMode
import com.wacom.ink.format.rendering.RasterBrush
import com.wacom.ink.rendering.BlendMode
import com.wacom.will3.ink.raster.rendering.demo.R
import java.io.ByteArrayOutputStream

/**
 * Class collecting the all brushes which are used within the application.
 */
class BrushPalette {

    companion object {
        fun pencil(context: Context): RasterBrush {
            val opts = BitmapFactory.Options()
            opts.inSampleSize = 1
            opts.inScaled = false
            // Texture for shape
            val shapeTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_shape, opts)
            // Texture for fill
            val fillTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_fill_11, opts)

            val stream = ByteArrayOutputStream()
            shapeTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureByteArray = stream.toByteArray()

            val stream2 = ByteArrayOutputStream()
            fillTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            val fillTextureByteArray = stream2.toByteArray()

            // Create the raster brush
            var brush = RasterBrush(
                URIBuilder.getBrushURI("raster", "Pencil"), // name of the brush
                0.15f,                                          // spacing
                0.15f,                                         // scattering
                RotationMode.RANDOM,                                    // rotation mode
                listOf(shapeTextureByteArray),                          // shape texture
                listOf(), fillTextureByteArray,                         // fill texture
                "",                                         // fill texture URI
                fillTexture.width.toFloat(),                            // width of texture
                fillTexture.height.toFloat(),                           // height of texture
                false,                                       // randomized fill
                BlendMode.MAX                                            // mode of blending
            )

            shapeTexture.recycle()
            fillTexture.recycle()

            return brush
        }

        fun waterbrush(context: Context): RasterBrush {
            val opts = BitmapFactory.Options()
            opts.inSampleSize = 1
            opts.inScaled = false

            val shapeTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_shape, opts)
            val fillTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_fill_14, opts)

            val stream = ByteArrayOutputStream()
            shapeTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray = stream.toByteArray()

            val stream2 = ByteArrayOutputStream()
            fillTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            val fillTextureByteArray = stream2.toByteArray()

            var brush = RasterBrush(
                URIBuilder.getBrushURI("raster", "Waterbrush"),
                0.1f, 0.03f,
                RotationMode.RANDOM,
                listOf(shapeTextureButeArray),
                listOf(), fillTextureByteArray,
                "",
                fillTexture.width.toFloat(), fillTexture.height.toFloat(),
                true, BlendMode.MAX
            )

            shapeTexture.recycle()
            fillTexture.recycle()

            return brush
        }

        fun inkBrush(context: Context): RasterBrush {
            val opts = BitmapFactory.Options()
            opts.inSampleSize = 1
            opts.inScaled = false

            val shapeTexture1 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_128x128, opts)
            val shapeTexture2 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_64x64, opts)
            val shapeTexture3 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_32x32, opts)
            val shapeTexture4 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_16x16, opts)
            val shapeTexture5 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_8x8, opts)
            val shapeTexture6 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_4x4, opts)
            val shapeTexture7 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_2x2, opts)
            val shapeTexture8 = BitmapFactory.decodeResource(context.resources, R.drawable.fountain_brush_1x1, opts)

            val fillTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_fill_8, opts)

            val stream = ByteArrayOutputStream()
            shapeTexture1!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray1 = stream.toByteArray()

            stream.reset()
            shapeTexture2!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray2 = stream.toByteArray()

            stream.reset()
            shapeTexture3!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray3 = stream.toByteArray()

            stream.reset()
            shapeTexture4!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray4 = stream.toByteArray()

            stream.reset()
            shapeTexture5!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray5 = stream.toByteArray()

            stream.reset()
            shapeTexture6!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray6 = stream.toByteArray()

            stream.reset()
            shapeTexture7!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray7 = stream.toByteArray()

            stream.reset()
            shapeTexture8!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray8 = stream.toByteArray()
            stream.close()

            val stream2 = ByteArrayOutputStream()
            fillTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            val fillTextureByteArray = stream2.toByteArray()
            stream2.close()

            var brush = RasterBrush(
                URIBuilder.getBrushURI("raster", "Inkbrush"),
                0.035f, 0f,
                RotationMode.NONE,
                listOf(
                    shapeTextureButeArray1,
                    shapeTextureButeArray2,
                    shapeTextureButeArray3,
                    shapeTextureButeArray4,
                    shapeTextureButeArray5,
                    shapeTextureButeArray6,
                    shapeTextureButeArray7,
                    shapeTextureButeArray8
                ),
                listOf(), fillTextureByteArray,
                "",
                fillTexture.width.toFloat(), fillTexture.height.toFloat(),
                true, BlendMode.MAX
            )

            shapeTexture1.recycle()
            shapeTexture2.recycle()
            shapeTexture3.recycle()
            shapeTexture4.recycle()
            shapeTexture5.recycle()
            shapeTexture6.recycle()
            shapeTexture7.recycle()
            shapeTexture8.recycle()
            fillTexture.recycle()

            return brush
        }

        fun crayonbrush(context: Context): RasterBrush {
            val opts = BitmapFactory.Options()
            opts.inSampleSize = 1
            opts.inScaled = false

            val shapeTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_shape, opts)
            val fillTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_fill_17, opts)

            val stream = ByteArrayOutputStream()
            shapeTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray = stream.toByteArray()

            val stream2 = ByteArrayOutputStream()
            fillTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            val fillTextureByteArray = stream2.toByteArray()

            var brush = RasterBrush(
                URIBuilder.getBrushURI("raster", "Crayon"),
                0.1f, 0.05f,
                RotationMode.RANDOM,
                listOf(shapeTextureButeArray),
                listOf(), fillTextureByteArray,
                "",
                fillTexture.width.toFloat(), fillTexture.height.toFloat(),
                true, BlendMode.MAX
            )

            shapeTexture.recycle()
            fillTexture.recycle()

            return brush
        }

        fun eraser(context: Context): RasterBrush {
            val opts = BitmapFactory.Options()
            opts.inSampleSize = 1
            opts.inScaled = false

            val shapeTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.shape_circle, opts)
            val fillTexture =
                BitmapFactory.decodeResource(context.resources, R.drawable.essential_fill_8, opts)

            val stream = ByteArrayOutputStream()
            shapeTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val shapeTextureButeArray = stream.toByteArray()


            val stream2 = ByteArrayOutputStream()
            fillTexture!!.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            val fillTextureByteArray = stream2.toByteArray()

            var brush = RasterBrush(
                URIBuilder.getBrushURI("raster", "Eraser"),
                0.1f, 0f,
                RotationMode.RANDOM,
                listOf(shapeTextureButeArray),
                listOf(), fillTextureByteArray,
                "",
                fillTexture.width.toFloat(), fillTexture.height.toFloat(),
                false, BlendMode.MAX
            )

            shapeTexture.recycle()
            fillTexture.recycle()

            return brush
        }
    }
}