/*
 * Copyright (C) 2021 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.manipulation

import android.graphics.PointF
import android.graphics.RectF

class Utils {

    companion object {

        fun getLength(x: Float, y: Float): Float {
            return kotlin.math.sqrt(x * x + y * y)
        }

        fun getAngle(pt1: PointF, pt2: PointF): Float {
            val dot = pt1.x * pt2.x + pt1.y * pt2.y
            val det = pt1.x * pt2.y - pt1.y * pt2.x
            val angle = kotlin.math.atan2(det, dot) / Math.PI.toFloat() * 180
            return (angle + 360) % 360
        }

        fun degToRadian(deg:Float): Float {
            return deg*Math.PI.toFloat()/180f
        }

        fun rectFromCenter(centerX:Float, centerY:Float, width:Float, height:Float): RectF {
            return RectF(centerX-width/2, centerY-height/2, centerX+width/2, centerY+height/2)
        }

    }

}