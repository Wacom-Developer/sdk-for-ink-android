/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.manipulation

import android.graphics.*
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.wacom.ink.manipulation.SpatialModel
import com.wacom.will3.ink.vector.rendering.demo.R
import com.wacom.will3.ink.vector.rendering.demo.createVectorTool
import com.wacom.will3.ink.vector.rendering.demo.vector.VectorInkBuilder
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class SelectionBox(
    val view: View,
    val strokes: Map<String, WillStroke>,
    val selectedList: List<String>,
    val spatialModel: SpatialModel
) {

    private enum class ControlPoint {
        CORNER_LEFT_UP, MIDDLE_UP, CORNER_RIGHT_UP, MIDDLE_RIGHT, CORNER_RIGHT_DOWN, MIDDLE_DOWN, CORNER_LEFT_DOWN, MIDDLE_LEFT
    }

    private var left = 0f
    private var top = 0f
    private var right = 0f
    private var bottom = 0f
    private var middle_x = 0f
    private var middle_y = 0f

    private var editing = false
    private var firstPoint = PointF(0f, 0f)
    private var lastPoint = PointF(0f, 0f)
    private var movingPoint = PointF(0f, 0f)
    private var controlPoint: ControlPoint? = null
    private var originalBounds = RectF()
    private var newBounds = RectF()

    //--  This is for manipulate selecting strokes with two fingers
    var originX = Float.NaN
    var originY = Float.NaN

    var totalTransformationAngle = 0.0f
    var totalTransformationScale = 1.0f

    private var lastX1 = 0.0f
    private var lastX2 = 0.0f
    private var lastY1 = 0.0f
    private var lastY2 = 0.0f

    private var pivotPointX = 0.0f
    private var pivotPointY = 0.0f

    var transformMultipoint = false

    val originalPaths = HashMap<String, Path>()
    //-- end manipulate variables

    var changed = false

    init {
        originalBounds = getBounds()
        updateSelectionBox()
    }

    fun onTouch(event: MotionEvent): Boolean {
        //using multitouch we can move, rotate and scale the selected strokes
        if (event.pointerCount > 1) {
            transformMultipoint = true
            onTouchForTransform(event)
        } else if (event.action == MotionEvent.ACTION_DOWN) {
            firstPoint.x = event.x
            firstPoint.y = event.y
            controlPoint = getControlPoint(event.x, event.y)
            if ((controlPoint != null) || (isInside(event.x, event.y))) {
                editing = true
            } else {
                editing = false
            }
            lastPoint.x = event.x
            lastPoint.y = event.y
        } else if (editing) {
            if (event.action == MotionEvent.ACTION_MOVE) {
                if (controlPoint != null) {
                    val intermediateBounds = getBounds()
                    when (controlPoint) {
                        ControlPoint.CORNER_LEFT_UP -> {
                            intermediateBounds.left += (event.x - lastPoint.x).toInt()
                            intermediateBounds.top += (event.y - lastPoint.y).toInt()
                        }
                        ControlPoint.MIDDLE_UP -> {
                            intermediateBounds.top += (event.y - lastPoint.y).toInt()
                        }
                        ControlPoint.CORNER_RIGHT_UP -> {
                            intermediateBounds.right += (event.x - lastPoint.x).toInt()
                            intermediateBounds.top += (event.y - lastPoint.y).toInt()
                        }
                        ControlPoint.MIDDLE_RIGHT -> {
                            intermediateBounds.right += (event.x - lastPoint.x).toInt()
                        }
                        ControlPoint.CORNER_RIGHT_DOWN -> {
                            intermediateBounds.right += (event.x - lastPoint.x).toInt()
                            intermediateBounds.bottom += (event.y - lastPoint.y).toInt()
                        }
                        ControlPoint.MIDDLE_DOWN -> {
                            intermediateBounds.bottom += (event.y - lastPoint.y).toInt()
                        }
                        ControlPoint.CORNER_LEFT_DOWN -> {
                            intermediateBounds.left += (event.x - lastPoint.x).toInt()
                            intermediateBounds.bottom += (event.y - lastPoint.y).toInt()
                        }
                        ControlPoint.MIDDLE_LEFT -> {
                            intermediateBounds.left += (event.x - lastPoint.x).toInt()
                        }
                    }
                    lastPoint.x = event.x
                    lastPoint.y = event.y
                    scaleSelectedStrokes(intermediateBounds)
                } else if (isInside(event.x, event.y)) {
                    movingPoint.x = event.x - lastPoint.x
                    movingPoint.y = event.y - lastPoint.y
                    lastPoint.x = event.x
                    lastPoint.y = event.y

                    //move the selection
                    moveSelectedStrokes()
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (transformMultipoint) {
                    transformMultipoint = false
                    transformSelection(
                        totalTransformationScale,
                        totalTransformationScale,
                        totalTransformationScale,
                        0f,
                        0f,
                        totalTransformationAngle,
                        pivotPointX,
                        pivotPointY
                    )

                    totalTransformationAngle = 0.0f
                    totalTransformationScale = 1.0f

                    originX = Float.NaN
                    originY = Float.NaN
                } else {
                    transformSelection(
                        scaleX = newBounds.width() / originalBounds.width(),
                        scaleY = newBounds.height() / originalBounds.height(),
                        translateX = newBounds.left - originalBounds.left,
                        translateY = newBounds.top - originalBounds.top,
                        pivotX = originalBounds.left,
                        pivotY = originalBounds.top
                    )
                }
                originalBounds = getBounds()
                updateSelectionBox()
            }
        }
        return editing
    }

    fun getBounds(): RectF {
        val completeBounds = RectF()
        for ((key, stroke) in strokes) {
            if (selectedList.contains(key)) {
                val bounds = RectF()
                stroke.path.computeBounds(bounds, true)
                completeBounds.union(bounds)
            }
        }
        return completeBounds
    }

    fun updateSelectionBox() {
        newBounds = getBounds()

        left = newBounds.left
        top = newBounds.top
        right = newBounds.right
        bottom = newBounds.bottom
        middle_x = left + (right - left) / 2
        middle_y = top + (bottom - top) / 2
    }

    fun drawSelectionBox(canvas: Canvas) {
        val paint = Paint()
        paint.color = view.context.resources.getColor(R.color.selection_background)
        paint.style = Paint.Style.FILL
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

        val radius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            7.5f,
            view.context.resources.displayMetrics
        )

        // Control point fill color
        paint.color = view.context.resources.getColor(R.color.selection_control_point)
        canvas.drawCircle(left, bottom, radius, paint)
        canvas.drawCircle(middle_x, bottom, radius, paint)
        canvas.drawCircle(right, bottom, radius, paint)
        canvas.drawCircle(right, middle_y, radius, paint)
        canvas.drawCircle(right, top, radius, paint)
        canvas.drawCircle(middle_x, top, radius, paint)
        canvas.drawCircle(left, top, radius, paint)
        canvas.drawCircle(left, middle_y, radius, paint)

        // Control point border
        paint.color = view.context.resources.getColor(R.color.selection_control_point_border)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(left, bottom, radius, paint)
        canvas.drawCircle(middle_x, bottom, radius, paint)
        canvas.drawCircle(right, bottom, radius, paint)
        canvas.drawCircle(right, middle_y, radius, paint)
        canvas.drawCircle(right, top, radius, paint)
        canvas.drawCircle(middle_x, top, radius, paint)
        canvas.drawCircle(left, top, radius, paint)
        canvas.drawCircle(left, middle_y, radius, paint)
    }

    fun isInside(x: Float, y: Float): Boolean {
        val rect = RectF(left, top, right, bottom)
        return rect.contains(x, y)
    }

    fun isChanged(): Boolean {
        return changed
    }

    private fun getControlPoint(x: Float, y: Float): ControlPoint? {
        val tolerance = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            7.5f,
            view.context.resources.displayMetrics
        )

        var controlPoint: ControlPoint? = null
        if (x >= left - tolerance && x <= left + tolerance &&
            y >= top - tolerance && y <= top + tolerance
        ) {
            controlPoint =
                ControlPoint.CORNER_LEFT_UP
        }
        if (x >= middle_x - tolerance && x <= middle_x + tolerance &&
            y >= top - tolerance && y <= top + tolerance
        ) {
            controlPoint =
                ControlPoint.MIDDLE_UP
        }
        if (x >= right - tolerance && x <= right + tolerance &&
            y >= top - tolerance && y <= top + tolerance
        ) {
            controlPoint =
                ControlPoint.CORNER_RIGHT_UP
        }
        if (x >= right - tolerance && x <= right + tolerance &&
            y >= middle_y - tolerance && y <= middle_y + tolerance
        ) {
            controlPoint =
                ControlPoint.MIDDLE_RIGHT
        }
        if (x >= right - tolerance && x <= right + tolerance &&
            y >= bottom - tolerance && y <= bottom + tolerance
        ) {
            controlPoint =
                ControlPoint.CORNER_RIGHT_DOWN
        }
        if (x >= middle_x - tolerance && x <= middle_x + tolerance &&
            y >= bottom - tolerance && y <= bottom + tolerance
        ) {
            controlPoint =
                ControlPoint.MIDDLE_DOWN
        }
        if (x >= left - tolerance && x <= left + tolerance &&
            y >= bottom - tolerance && y <= bottom + tolerance
        ) {
            controlPoint =
                ControlPoint.CORNER_LEFT_DOWN
        }
        if (x >= left - tolerance && x <= left + tolerance &&
            y >= middle_y - tolerance && y <= middle_y + tolerance
        ) {
            controlPoint =
                ControlPoint.MIDDLE_LEFT
        }
        return controlPoint
    }

    fun moveSelectedStrokes() {
        val matrix = Matrix()
        matrix.preTranslate(movingPoint.x, movingPoint.y)

        for ((id, stroke) in strokes) {
            if (selectedList.contains(id)) {
                val newPath = Path(strokes[id]!!.path)
                newPath.transform(matrix)
                stroke.path = newPath
            }
        }

        updateSelectionBox()
        view.invalidate()
    }

    fun scaleSelectedStrokes(intermediateBounds: RectF) {
        val scaleX = intermediateBounds.width() / newBounds.width()
        val scaleY = intermediateBounds.height() / newBounds.height()
        val matrix = Matrix()

        matrix.preScale(scaleX, scaleY, newBounds.left, newBounds.top)
        matrix.preTranslate(
            intermediateBounds.left - newBounds.left,
            intermediateBounds.top - newBounds.top
        )

        for ((id, stroke) in strokes) {
            if (selectedList.contains(id)) {
                val newPath = Path(strokes[id]!!.path)
                newPath.transform(matrix)
                stroke.path = newPath
            }
        }

        updateSelectionBox()
        view.invalidate()
    }

    fun transformSelection(
        scaleX: Float = 1f,
        scaleY: Float = 1f,
        scaleSize: Float = 1f,
        translateX: Float = 0f,
        translateY: Float = 0f,
        angle: Float = 0f,
        pivotX: Float = 0f,
        pivotY: Float = 0f
    ) {
        if ((scaleX != 1f) ||
            (scaleY != 1f) ||
            (scaleSize != 1f) ||
            (translateX != 0f) ||
            (translateY != 0f) ||
            (angle != 0f)) {
            changed = true
        }

        for (id in selectedList) {
            val stroke = strokes[id]!!

            spatialModel.remove(stroke.id)
            stroke.spline.transform(
                scaleX,
                scaleY,
                scaleSize,
                translateX,
                translateY,
                angle,
                pivotX,
                pivotY
            )
            spatialModel.add(stroke)

            var transformInkBuilder = VectorInkBuilder()
            transformInkBuilder.updatePipeline(stroke.createStyle().createVectorTool())
            val (path, _) = transformInkBuilder.buildPath(stroke.spline, null, true, true)

            if (path != null) {
                stroke.path = path
            }
        }

        view.invalidate()
    }

    // This function will transform the existing selected strokes.
    // Note: This only modifying the existing path, not the capture ink strokes
    private fun onTouchForTransform(event: MotionEvent) {
        var centerX = 0f
        var centerY = 0f

        for (i in 0 until event.pointerCount) {
            centerX += event.getX(i)
            centerY += event.getY(i)
        }

        centerX /= event.pointerCount
        centerY /= event.pointerCount


        var x1 = event.getX(0)
        var y1 = event.getY(0)
        var x2 = event.getX(1)
        var y2 = event.getY(1)

        if (originX.isNaN() && originY.isNaN()) {
            originX = centerX
            originY = centerY

            for ((id, stroke) in strokes) {
                if (id in selectedList) {
                    originalPaths[id] = Path(stroke.path)
                }
            }

            lastX1 = x1
            lastY1 = y1
            lastX2 = x2
            lastY2 = y2

            return
        }

        val angle1 = atan2(y1 - y2, x1 - x2)
        val angle2 = atan2(lastY1 - lastY2, lastX1 - lastX2)
        val theta = angle1 - angle2
        val angle = theta * 180 / PI.toFloat()

        val d1 = sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
        val d2 = sqrt((lastX1 - lastX2).pow(2) + (lastY1 - lastY2).pow(2))


        val scale = d1 / d2

        originX = centerX
        originY = centerY

        pivotPointX = centerX
        pivotPointY = centerY

        lastX1 = x1
        lastY1 = y1
        lastX2 = x2
        lastY2 = y2

        totalTransformationAngle += angle
        totalTransformationScale *= scale

        val mx = Matrix()
        mx.setScale(
            totalTransformationScale,
            totalTransformationScale,
            originalBounds.centerX(),
            originalBounds.centerY()
        )
        mx.postRotate(totalTransformationAngle, originalBounds.centerX(), originalBounds.centerY())

        for ((id, stroke) in strokes) {
            if (id in selectedList) {
                val newPath = Path(originalPaths[id])
                newPath.transform(mx)
                stroke.path = newPath
            }
        }

        updateSelectionBox()
        view.invalidate()
    }

}
