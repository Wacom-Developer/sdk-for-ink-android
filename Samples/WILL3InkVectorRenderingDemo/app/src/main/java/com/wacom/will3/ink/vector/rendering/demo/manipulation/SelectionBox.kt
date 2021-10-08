/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.manipulation

import android.graphics.*
import android.util.TypedValue
import android.view.MotionEvent
import com.wacom.ink.manipulation.SpatialModel
import com.wacom.ink.model.Identifier
import com.wacom.will3.ink.vector.rendering.demo.R
import com.wacom.will3.ink.vector.rendering.demo.createVectorTool
import com.wacom.will3.ink.vector.rendering.demo.vector.VectorInkBuilder
import com.wacom.will3.ink.vector.rendering.demo.vector.VectorView
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke
import kotlin.math.sin


class SelectionBox(
    val view: VectorView,
    val strokes: Map<Identifier, WillStroke>,
    val selectedList: List<Identifier>,
    val spatialModel: SpatialModel) {

    enum class ControlPoint {
        CORNER_LEFT_UP, MIDDLE_UP, CORNER_RIGHT_UP, MIDDLE_RIGHT, CORNER_RIGHT_DOWN, MIDDLE_DOWN, CORNER_LEFT_DOWN, MIDDLE_LEFT, ROTATION_POINT
    }

    companion object {
        private val ROTATION_POINT_OFFSET = 30f //dp
    }

    private val rotationPointOffsetPixels = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        ROTATION_POINT_OFFSET,
        view.activity.resources.displayMetrics
    )

    private val radius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        7.5f,
        view.context.resources.displayMetrics
    )

    private val tolerance = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        10f,
        view.context.resources.displayMetrics
    )

    private var editing = false
    private var firstPoint = PointF(0f, 0f)
    private var lastPoint = PointF(0f, 0f)
    private var movingPoint = PointF(0f, 0f)
    private var controlPoint: ControlPoint? = null
    private var originalBounds = RectF()
    private var newBounds = RectF()

    private var mAngle = 0f
    private val mStartVector = PointF()
    private var mStartAngle = 0f
    private var mBitmap: Bitmap? = null

    init {
        originalBounds = getBounds()
        newBounds = RectF(originalBounds.left, originalBounds.top, originalBounds.right, originalBounds.bottom)

        mBitmap = Bitmap.createBitmap(originalBounds.width().toInt(), originalBounds.height().toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mBitmap!!)
        var paint = Paint(Paint.DITHER_FLAG).also {
            it.color = Color.BLUE
            it.style = Paint.Style.FILL
        }
        val matrix = Matrix()
        matrix.preTranslate(-originalBounds.left, -originalBounds.top)
        canvas.setMatrix(matrix)
        for ((id, stroke) in strokes) {
            if (selectedList.contains(id)) {
                canvas.drawPath(stroke.path, paint)
            }
        }
    }

    //--- Public methods ---

    /**
     * Indicates if there has been some transformation on the selected strokes
     */
    fun isChanged(): Boolean {
        val scaleX = newBounds.width() / originalBounds.width()
        val scaleY = newBounds.height() / originalBounds.height()
        val offsetX = newBounds.centerX() - originalBounds.centerX()
        val offsetY = newBounds.centerY() - originalBounds.centerY()
        if (scaleX != 1f || scaleY != 1f || offsetX != 0f || offsetY != 0f || mAngle != 0f) {
            return true
        }

        return false
    }

    /**
     * Apply the changes to the selected strokes
     */
    fun applyChanges() {
        var scaleX = newBounds.width() / originalBounds.width()
        var scaleY = newBounds.height() / originalBounds.height()
        val offsetX = newBounds.centerX() - originalBounds.centerX()
        val offsetY = newBounds.centerY() - originalBounds.centerY()

        for (id in selectedList) {
            val stroke = strokes[id]!!
            spatialModel.remove(stroke.id)
            stroke.spline.transform(scaleX, scaleY, (scaleX+scaleY)/2, offsetX, offsetY, mAngle, originalBounds.centerX(), originalBounds.centerY())
            spatialModel.add(stroke)

            var transformInkBuilder = VectorInkBuilder()
            transformInkBuilder.updatePipeline(stroke.createStyle().createVectorTool())
            val (path1, _) = transformInkBuilder.buildPath(stroke.spline, null, true, true)

            if (path1 != null) {
                stroke.path = path1
            }
        }
    }

    /**
     * Handles onTouch event
     */
    fun onTouch(event: MotionEvent): Boolean {
        val matrix = Matrix()
        matrix.postRotate(-mAngle, newBounds.centerX(), newBounds.centerY())

        val tEvent = MotionEvent.obtain(event)
        tEvent.transform(matrix)

        if (event.action == MotionEvent.ACTION_DOWN) {
            firstPoint.x = event.x
            firstPoint.y = event.y
            controlPoint = getControlPoint(tEvent.x, tEvent.y)
            if ((controlPoint != null) || (isInside(tEvent.x, tEvent.y))) {
                editing = true

                if (controlPoint == ControlPoint.ROTATION_POINT) {
                    mStartVector.x = event.x - newBounds.centerX()
                    mStartVector.y = event.y - newBounds.centerY()
                    mStartAngle = mAngle
                }

            } else {
                editing = false
            }
            lastPoint.x = event.x
            lastPoint.y = event.y
        } else if (editing) {
            if (event.action == MotionEvent.ACTION_MOVE) {
                if (controlPoint != null) {
                    if (controlPoint == ControlPoint.ROTATION_POINT) {
                        val angle = Utils.getAngle(mStartVector, PointF(event.x - newBounds.centerX(), event.y - newBounds.centerY()))
                        rotateSelectedStrokes(angle, mStartAngle)
                    } else {
                        val deltaX = event.x - lastPoint.x
                        val deltaY = event.y - lastPoint.y
                        val alpha = kotlin.math.atan2(deltaY, deltaX)
                        val deltaL = Utils.getLength(deltaX, deltaY)
                        resizeSelectedStrokes(deltaL, alpha, controlPoint!!)

                        lastPoint.x = event.x
                        lastPoint.y = event.y
                    }
                } else if (isInside(tEvent.x, tEvent.y)) {
                    movingPoint.x = event.x - lastPoint.x
                    movingPoint.y = event.y - lastPoint.y
                    lastPoint.x = event.x
                    lastPoint.y = event.y

                    //move the selection
                    moveSelectedStrokes()
                }
            }
        }

        if (editing) {
            view.updateDrawing()
        }

        return editing
    }

    fun getMatrix(): Matrix {
        val scaleX = newBounds.width() / originalBounds.width()
        val scaleY = newBounds.height() / originalBounds.height()

        val m = Matrix()
        m.postTranslate(newBounds.left, newBounds.top)
        m.postScale(scaleX, scaleY, newBounds.left, newBounds.top)
        m.postRotate(mAngle, newBounds.centerX(), newBounds.centerY())
        return m
    }

    //--- Draw section ----

    /**
     * Draw the selection box and return a transformation matrix with the transformations
     * that needs to be apply to the selected strokes path
     */
    fun drawSelectionBox(canvas: Canvas) {
        val matrix = Matrix()
        matrix.postRotate(mAngle, newBounds.centerX(), newBounds.centerY())
        canvas.save()
        canvas.setMatrix(matrix)
        drawSelectionBoxInternal(canvas)
        canvas.restore()
    }

    private fun drawSelectionBoxInternal(canvas: Canvas) {
        val paint = Paint()
        paint.color = view.context.resources.getColor(R.color.selection_background)

        paint.style = Paint.Style.FILL
        canvas.drawRect(newBounds, paint)

        paint.color = Color.BLACK
        paint.strokeWidth = 2f

        canvas.drawLine(
            newBounds.centerX(),
            newBounds.top,
            newBounds.centerX(),
            newBounds.top - rotationPointOffsetPixels,
            paint
        )

        paint.color = view.context.resources.getColor(R.color.rotation_control_point)
        canvas.drawCircle(newBounds.centerX(), newBounds.top - rotationPointOffsetPixels, radius, paint)

        // Control point fill color
        paint.color = view.context.resources.getColor(R.color.selection_control_point)
        canvas.drawCircle(newBounds.left, newBounds.bottom, radius, paint)
        canvas.drawCircle(newBounds.left, newBounds.top, radius, paint)
        canvas.drawCircle(newBounds.right, newBounds.top, radius, paint)
        canvas.drawCircle(newBounds.right, newBounds.bottom, radius, paint)
        canvas.drawCircle(newBounds.left, newBounds.centerY(), radius, paint)
        canvas.drawCircle(newBounds.centerX(), newBounds.top, radius, paint)
        canvas.drawCircle(newBounds.right, newBounds.centerY(), radius, paint)
        canvas.drawCircle(newBounds.centerX(), newBounds.bottom, radius, paint)

        // Control point border
        paint.color = view.context.resources.getColor(R.color.selection_control_point_border)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(newBounds.left, newBounds.bottom, radius, paint)
        canvas.drawCircle(newBounds.left, newBounds.top, radius, paint)
        canvas.drawCircle(newBounds.right, newBounds.top, radius, paint)
        canvas.drawCircle(newBounds.right, newBounds.bottom, radius, paint)
        canvas.drawCircle(newBounds.left, newBounds.centerY(), radius, paint)
        canvas.drawCircle(newBounds.centerX(), newBounds.top, radius, paint)
        canvas.drawCircle(newBounds.right, newBounds.centerY(), radius, paint)
        canvas.drawCircle(newBounds.centerX(), newBounds.bottom, radius, paint)
        canvas.drawCircle(newBounds.centerX(), newBounds.top - rotationPointOffsetPixels, radius, paint)
    }

    // ---- End draw section


    // --- transformation section

    private fun moveSelectedStrokes() {
        newBounds.left += movingPoint.x
        newBounds.right += movingPoint.x
        newBounds.top += movingPoint.y
        newBounds.bottom += movingPoint.y
    }

    fun rotateSelectedStrokes(angle:Float, startAngle:Float) {
        var rotateAngle = kotlin.math.round(angle + mStartAngle)
        if (rotateAngle >= 360) {
            rotateAngle -= 360
        } else if (rotateAngle < 0) {
            rotateAngle += 360
        }
        if (rotateAngle > 356 || rotateAngle < 4) {
            rotateAngle = 0f
        } else if (rotateAngle > 86 && rotateAngle < 94) {
            rotateAngle = 90f
        } else if (rotateAngle > 176 && rotateAngle < 184) {
            rotateAngle = 180f
        } else if (rotateAngle > 266 && rotateAngle < 274) {
            rotateAngle = 270f
        }

        mAngle = rotateAngle
    }

    fun resizeSelectedStrokes(length:Float, alpha:Float, controlPoint: ControlPoint) {
        val beta = alpha - Utils.degToRadian(mAngle)
        var deltaW = length * kotlin.math.cos(beta)
        var deltaH = length * kotlin.math.sin(beta)

        when (controlPoint) {
            ControlPoint.CORNER_LEFT_UP -> {
                deltaW = -deltaW
                val centerX = newBounds.centerX() - deltaW/2 * kotlin.math.cos(Utils.degToRadian(mAngle)) - deltaH/2 * kotlin.math.sin(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() - deltaW/2 * kotlin.math.sin(Utils.degToRadian(mAngle)) + deltaH/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width()+deltaW, newBounds.height()-deltaH)
            }
            ControlPoint.MIDDLE_UP -> {
                deltaH = -deltaH
                val centerX = newBounds.centerX() + deltaH/2 * kotlin.math.sin(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() - deltaH/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width(), newBounds.height()+deltaH)
            }
            ControlPoint.CORNER_RIGHT_UP -> {
                val centerX = newBounds.centerX() + deltaW/2 * kotlin.math.cos(Utils.degToRadian(mAngle)) - deltaH/2 * kotlin.math.sin(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() + deltaW/2 * kotlin.math.sin(Utils.degToRadian(mAngle)) + deltaH/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width()+deltaW, newBounds.height()-deltaH)
            }
            ControlPoint.MIDDLE_RIGHT -> {
                val centerX = newBounds.centerX() + deltaW/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() + deltaW/2 * sin(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width()+deltaW, newBounds.height())
            }
            ControlPoint.CORNER_RIGHT_DOWN -> {
                val centerX = newBounds.centerX() + deltaW/2 * kotlin.math.cos(Utils.degToRadian(mAngle)) - deltaH/2 * kotlin.math.sin(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() + deltaW/2 * kotlin.math.sin(Utils.degToRadian(mAngle)) + deltaH/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width()+deltaW, newBounds.height()+deltaH)
            }
            ControlPoint.MIDDLE_DOWN -> {
                val centerX = newBounds.centerX() - deltaH/2 * kotlin.math.sin(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() + deltaH/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width(), newBounds.height()+deltaH)
            }
            ControlPoint.CORNER_LEFT_DOWN -> {
                deltaW = -deltaW
                val centerX = newBounds.centerX() - deltaW/2 * kotlin.math.cos(Utils.degToRadian(mAngle)) - deltaH/2 * kotlin.math.sin(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() - deltaW/2 * kotlin.math.sin(Utils.degToRadian(mAngle)) + deltaH/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width()+deltaW, newBounds.height()+deltaH)
            }
            ControlPoint.MIDDLE_LEFT -> {
                deltaW = -deltaW
                val centerX = newBounds.centerX() - deltaW/2 * kotlin.math.cos(Utils.degToRadian(mAngle))
                val centerY = newBounds.centerY() - deltaW/2 * sin(Utils.degToRadian(mAngle))
                newBounds = Utils.rectFromCenter(centerX, centerY, newBounds.width()+deltaW, newBounds.height())
            }
        }
    }


    // --- end transformation section


    // --- utility functions

    /**
     * get the bounds of the union of all selected strokes path
     */
    private fun getBounds(): RectF {
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

    private fun getControlPoint(x: Float, y: Float): ControlPoint? {
        var controlPoint: ControlPoint? = null
        if (x >= newBounds.left - tolerance && x <= newBounds.left + tolerance &&
            y >= newBounds.top - tolerance && y <= newBounds.top + tolerance) {
            controlPoint = ControlPoint.CORNER_LEFT_UP
        }
        if (x >= newBounds.centerX() - tolerance && x <= newBounds.centerX() + tolerance &&
            y >= newBounds.top - tolerance && y <= newBounds.top + tolerance) {
            controlPoint = ControlPoint.MIDDLE_UP
        }
        if (x >= newBounds.right - tolerance && x <= newBounds.right + tolerance &&
            y >= newBounds.top - tolerance && y <= newBounds.top + tolerance) {
            controlPoint = ControlPoint.CORNER_RIGHT_UP
        }
        if (x >= newBounds.right - tolerance && x <= newBounds.right + tolerance &&
            y >= newBounds.centerY() - tolerance && y <= newBounds.centerY() + tolerance) {
            controlPoint = ControlPoint.MIDDLE_RIGHT
        }
        if (x >= newBounds.right - tolerance && x <= newBounds.right + tolerance &&
            y >= newBounds.bottom - tolerance && y <= newBounds.bottom + tolerance) {
            controlPoint = ControlPoint.CORNER_RIGHT_DOWN
        }
        if (x >= newBounds.centerX() - tolerance && x <= newBounds.centerX() + tolerance &&
            y >= newBounds.bottom - tolerance && y <= newBounds.bottom + tolerance) {
            controlPoint = ControlPoint.MIDDLE_DOWN
        }
        if (x >= newBounds.left - tolerance && x <= newBounds.left + tolerance &&
            y >= newBounds.bottom - tolerance && y <= newBounds.bottom + tolerance) {
            controlPoint = ControlPoint.CORNER_LEFT_DOWN
        }
        if (x >= newBounds.left - tolerance && x <= newBounds.left + tolerance &&
            y >= newBounds.centerY() - tolerance && y <= newBounds.centerY() + tolerance) {
            controlPoint = ControlPoint.MIDDLE_LEFT
        }
        if (x >= newBounds.centerX() - tolerance && x <= newBounds.centerX() + tolerance &&
            y >= (newBounds.top-rotationPointOffsetPixels) - tolerance && y <= (newBounds.top-rotationPointOffsetPixels) + tolerance) {
            controlPoint = ControlPoint.ROTATION_POINT
        }
        return controlPoint
    }

    private fun isInside(x: Float, y: Float):Boolean {
        return newBounds.contains(x, y)
    }
}
