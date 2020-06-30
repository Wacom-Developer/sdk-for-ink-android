/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.vector

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.wacom.ink.*
import com.wacom.ink.format.InkSensorType
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.input.SensorChannel
import com.wacom.ink.format.tree.data.SensorData
import com.wacom.ink.manipulation.ManipulationMode
import com.wacom.ink.manipulation.SpatialModel
import com.wacom.ink.manipulation.callbacks.ErasingCallback
import com.wacom.ink.manipulation.callbacks.SelectingCallback
import com.wacom.ink.model.InkStroke
import com.wacom.ink.model.StrokeAttributes
import com.wacom.will3.ink.vector.rendering.demo.historicalToPointerData
import com.wacom.will3.ink.vector.rendering.demo.manipulation.SelectionBox
import com.wacom.will3.ink.vector.rendering.demo.manipulation.UndoManager
import com.wacom.will3.ink.vector.rendering.demo.resolveToolType
import com.wacom.will3.ink.vector.rendering.demo.serialization.InkEnvironmentModel
import com.wacom.will3.ink.vector.rendering.demo.toPointerData
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.EraserVectorTool
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.EraserWholeStrokeTool
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.PenTool
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.VectorTool
import com.wacom.will3.ink.vector.rendering.demo.uri
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * This class is a simple view that it is used to draw vector inking.
 * The vector inking is a Path so we draw it extending the onDraw method
 * and drawing there the path.
 */
class VectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Relevant for serialization
    lateinit var inkEnvironmentModel: InkEnvironmentModel
    lateinit var sensorData: SensorData
    lateinit var channelList: List<SensorChannel>

    var drawingTool: VectorTool = PenTool() // default tool
    var inkBuilder = VectorInkBuilder()

    private var currentPath =
        Path() // This will contain the current drawing path before it is finished

    /**
     * In order to make the strokes look nice and smooth, it is recommended to enable:
     *  - Antialiasing
     *  - Dither
     */
    val paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }

    // Ink Manipulations
    private var strokes = mutableMapOf<String, WillStroke>() // A list of existing strokes
    private lateinit var spatialModel: SpatialModel // The spatial model is user for
    private lateinit var undoManager: UndoManager

    val counterContext = newSingleThreadContext("CounterContext")

    private var unprocessedBegin = false

    lateinit var activity: Activity
    private var currentColor: Int = 0
    var isStylus: Boolean = false
    var newTool = false
    private var selectedStrokes = arrayListOf<String>() // Ink Manipulations: Selection

    var paintSelected = Paint().also { it.color = Color.BLUE; it.alpha = 100 }

    var selectionBox: SelectionBox? = null

    private val INVALID_POINTER_ID = -1
    private var activePointerID: Int = INVALID_POINTER_ID
    private var lastEvent: MotionEvent? = null

    fun setSpatialModel(spatialModel: SpatialModel) {
        this.spatialModel = spatialModel
        undoManager = UndoManager(spatialModel)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        selectionBox?.drawSelectionBox(canvas)

        for ((id, stroke) in strokes) {

            if (id in selectedStrokes) {
                canvas.drawPath(stroke.path, paintSelected)
            } else {
                // Selection color
                val color = Color.argb(
                    (stroke.strokeAttributes.alpha * 255).toInt(),
                    (stroke.strokeAttributes.red * 255).toInt(),
                    (stroke.strokeAttributes.green * 255).toInt(),
                    (stroke.strokeAttributes.blue * 255).toInt()
                )
                paint.color = color
                canvas.drawPath(stroke.path, paint)
            }
        }

        // draw the current path,
        // because we have not finished the stroke we has not saved it yet
        if (drawingTool.uri() == EraserVectorTool.uri) {
            paint.color = Color.WHITE
        } else if (drawingTool.uri() == EraserWholeStrokeTool.uri) {
            paint.color = Color.RED
        } else {
            paint.color = currentColor
        }
        canvas.drawPath(currentPath, paint)
    }

    fun onTouch(event: MotionEvent) {
        var canDraw = false
        if (selectionBox != null) {
            if (!selectionBox!!.onTouch(event)) {
                if (!selectionBox!!.isChanged()){
                    strokes = undoManager.undo()!!
                }
                undoManager.reset()
                selectionBox = null
                selectedStrokes.clear()
                canDraw = true
            }
        } else {
            canDraw = true
        }

        val action = event.actionMasked
        val pointerIndex = (event.action and MotionEvent.ACTION_POINTER_INDEX_MASK) shr MotionEvent.ACTION_POINTER_INDEX_SHIFT

        if (action == MotionEvent.ACTION_DOWN) {
            activePointerID = event.getPointerId(pointerIndex)
        } else if ((activePointerID == INVALID_POINTER_ID) && (action == MotionEvent.ACTION_POINTER_DOWN)) {
            activePointerID = event.getPointerId(pointerIndex)
        }

        if (activePointerID == event.getPointerId(pointerIndex)) {
            if (action == MotionEvent.ACTION_POINTER_UP) {
                event.action = MotionEvent.ACTION_UP
            } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
                event.action = MotionEvent.ACTION_DOWN
            }

            // We save the last event just in case we receive a CANCEL action
            // when we have a CANCEL action we get indeterminate coordinate values,
            // so in this case we pass the latest value coordinates
            if ((event.action == MotionEvent.ACTION_DOWN) ||
                (event.action == MotionEvent.ACTION_MOVE) ||
                (event.action == MotionEvent.ACTION_UP)
            ) {
                lastEvent = MotionEvent.obtain(event)
            } else if (event.action == MotionEvent.ACTION_CANCEL){
                lastEvent?.action = MotionEvent.ACTION_UP //we convert the latest event in END event
            } else {
                lastEvent = null
            }

            if (lastEvent != null) {
                if (canDraw) {
                    onTouchForDrawing(lastEvent!!)
                }
            }

            if (event.action == MotionEvent.ACTION_UP) {
                lastEvent = null
                activePointerID = INVALID_POINTER_ID
            }

        }
    }

    private fun onTouchForDrawing(event: MotionEvent) {
        if (event.resolveToolType() == InkInputType.PEN) {
            if ((newTool) || (!isStylus)) {
                newTool = false
                isStylus = true
                inkBuilder.updateInputMethod(isStylus)
            }
        } else {
            if ((newTool) || (isStylus)) {
                newTool = false
                isStylus = false
                inkBuilder.updateInputMethod(isStylus)
            }
        }

        var pointerData: PointerData

        // First, we capture all the historical events
        if (event.historySize > 0) {
            for (i in 0 until event.historySize) {
                event.action
                pointerData = event.historicalToPointerData(i)
                inkBuilder.add(pointerData.phase, pointerData, null)
            }
        }

        pointerData = event.toPointerData()
        inkBuilder.add(pointerData.phase, pointerData, null)

        val (addedSpline, _) = inkBuilder.buildSpline()
        if (addedSpline != null) {
            if (drawingTool.drawingMode == VectorTool.DrawingMode.DRAWING) {
                if ((pointerData.phase == Phase.BEGIN) || (unprocessedBegin)) {
                    // create a new sensor data
                    val pair = inkEnvironmentModel.createSensorData(event)
                    sensorData = pair.first
                    channelList = pair.second
                }

                for (channel in channelList) {
                    when (channel.type) {
                        InkSensorType.X -> sensorData.add(channel, pointerData.x)
                        InkSensorType.Y -> sensorData.add(channel, pointerData.y)
                        InkSensorType.TIMESTAMP -> sensorData.addTimestamp(
                            channel,
                            pointerData.timestamp
                        )
                        InkSensorType.PRESSURE -> sensorData.add(channel, pointerData.force!!)
                        InkSensorType.ALTITUDE -> sensorData.add(
                            channel,
                            pointerData.altitudeAngle!!
                        )
                        InkSensorType.AZIMUTH -> sensorData.add(
                            channel,
                            pointerData.azimuthAngle!!
                        )
                    }
                }

                if ((pointerData.phase == Phase.END) &&
                    (inkBuilder.splineProducer.allData != null)
                ) {
                    addStroke(event)
                }
            } else if ((drawingTool.drawingMode == VectorTool.DrawingMode.ERASING_WHOLE_STROKE) ||
                (drawingTool.drawingMode == VectorTool.DrawingMode.ERASING_PARTIAL_STROKE)
            ) {
                if (pointerData.phase == Phase.END) {
                    removeStroke(inkBuilder.splineProducer.allData!!.copy())
                }
            } else {
                // Assume we are selecting
                if (pointerData.phase == Phase.END) {
                    selectStroke(inkBuilder.splineProducer.allData!!.copy())
                }
            }

            val (path, _) = inkBuilder.buildPath(
                addedSpline,
                null,
                pointerData.phase == Phase.BEGIN || unprocessedBegin,
                pointerData.phase == Phase.END
            )
            unprocessedBegin = false
            if (path != null) {
                currentPath.addPath(path)
            }

            if (pointerData.phase == Phase.END) {
                currentPath = Path()
            }

            invalidate()

        } else if (pointerData.phase == Phase.BEGIN) {
            unprocessedBegin = true
        }

    }

    private fun addStroke(event: MotionEvent) {
        val brush = inkBuilder.tool.brush
        val stroke = WillStroke(inkBuilder.splineProducer.allData!!.copy(), brush)
        stroke.sensorData = sensorData

        stroke.strokeAttributes = object : StrokeAttributes {
            override var size: Float = 10f
            override var rotation: Float = 0.0f

            override var scaleX: Float = 1.0f
            override var scaleY: Float = 1.0f
            override var scaleZ: Float = 1.0f

            override var offsetX: Float = 0.0f
            override var offsetY: Float = 0.0f
            override var offsetZ: Float = 0.0f

            override var red: Float = Color.red(paint.color) / 255f
            override var green: Float = Color.green(paint.color) / 255f
            override var blue: Float = Color.blue(paint.color) / 255f
            override var alpha: Float = paint.alpha.toFloat() / 255
        }

        spatialModel.add(stroke)
        stroke.path = currentPath
        strokes[stroke.id] = stroke
    }

    fun removeStroke(spline: Spline) {
        runBlocking {
            withContext(counterContext) {
                val removed = arrayListOf<String>()

                spatialModel.erase(
                    spline,
                    drawingTool.brush,
                    if (drawingTool.drawingMode == VectorTool.DrawingMode.ERASING_PARTIAL_STROKE)
                        ManipulationMode.PARTIAL_STROKE else ManipulationMode.WHOLE_STROKE,
                    object : ErasingCallback {
                        override fun onStrokeAdded(stroke: InkStroke) {
                            buildStroke(stroke as WillStroke)
                            for (id in removed) strokes.remove(id)
                        }

                        override fun onStrokeRemoved(id: String) {
                            val removedStroke = strokes.remove(id)
                            if (removedStroke == null) {
                                removed.add(id)
                            }
                        }
                    }
                )
                activity.runOnUiThread() {
                    invalidate()
                }
            }
        }
    }

    fun selectStroke(spline: Spline) {
        runBlocking {
            withContext(counterContext) {
                val removed = arrayListOf<String>()
                undoManager.addStrokes(strokes) // add the current stroke list in case we want to go back to its state

                spatialModel.select(spline,
                    if (drawingTool.drawingMode == VectorTool.DrawingMode.SELECTING_PARTIAL_STROKE)
                        ManipulationMode.PARTIAL_STROKE else ManipulationMode.WHOLE_STROKE,
                    object : SelectingCallback {
                        override fun onStrokeAdded(stroke: InkStroke) {
                            undoManager.addStroke(stroke.id)
                            buildStroke(stroke as WillStroke)
                            for (id in removed) strokes.remove(id)
                        }

                        override fun onStrokeRemoved(id: String) {
                            val removedStroke = strokes.remove(id)
                            if (removedStroke == null) {
                                removed.add(id)
                            } else {
                                undoManager.removeStroke(removedStroke)
                            }
                            activity.runOnUiThread() {
                                invalidate()
                            }
                        }

                        override fun onStrokeSelected(id: String) {
                            selectedStrokes.add(id)
                            selectionBox =
                                SelectionBox(
                                    this@VectorView,
                                    strokes,
                                    selectedStrokes,
                                    spatialModel
                                )
                        }
                    }
                )

                activity.runOnUiThread() {
                    invalidate()
                }
            }
        }
    }

    fun setTool(tool: VectorTool) {
        newTool = true
        drawingTool = tool
        inkBuilder.updatePipeline(tool)
        selectedStrokes.clear()
        setColor(currentColor)
    }

    fun setColor(color: Int) {
        currentColor = adjustAlpha(color, drawingTool.alphaValue)
    }

    fun adjustAlpha(@ColorInt color: Int, alphaValue: Float): Int {
        return Color.argb(
            (alphaValue * 255).toInt(),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    fun clear() {
        spatialModel.reset()
        currentPath = Path()
        strokes.clear()
        selectedStrokes.clear()
        invalidate()
    }


    fun buildStroke(stroke: WillStroke) {
        // add the attributes
        addRequiredValuesToPath(stroke)

        val (path, _) = inkBuilder.buildPath(stroke.spline, null, true, true)
        if (path != null) {
            stroke.path = path
            strokes[stroke.id] = stroke
        }
    }

    fun addRequiredValuesToPath(stroke: WillStroke) {
        var attributeList = ArrayList<Pair<PathPoint.Property, Float>>()
        var newLayout = stroke.spline.layout
        if ((!newLayout.hasProperty(PathPoint.Property.SIZE)) && (stroke.strokeAttributes.size != 0f)) {
            attributeList.add(Pair(PathPoint.Property.SIZE, stroke.strokeAttributes.size))
        }
        /*if ((!newLayout.hasProperty(PathPoint.Property.ROTATION)) && (stroke.strokeAttributes.rotation != 0f)) {
            attributeList.add(
                Pair(
                    PathPoint.Property.ROTATION,
                    stroke.strokeAttributes.rotation
                )
            )
        }
        if ((!newLayout.hasProperty(PathPoint.Property.SCALE_X)) && (stroke.strokeAttributes.scaleX != 1.0f)) {
            attributeList.add(Pair(PathPoint.Property.SCALE_X, stroke.strokeAttributes.scaleX))
        }
        if ((!newLayout.hasProperty(PathPoint.Property.SCALE_Y)) && (stroke.strokeAttributes.scaleY != 1.0f)) {
            attributeList.add(Pair(PathPoint.Property.SCALE_Y, stroke.strokeAttributes.scaleY))
        }
        if ((!newLayout.hasProperty(PathPoint.Property.SCALE_Z)) && (stroke.strokeAttributes.scaleZ != 1.0f)) {
            attributeList.add(Pair(PathPoint.Property.SCALE_Z, stroke.strokeAttributes.scaleZ))
        }
        if ((!newLayout.hasProperty(PathPoint.Property.OFFSET_X)) && (stroke.strokeAttributes.offsetX != 0f)) {
            attributeList.add(
                Pair(
                    PathPoint.Property.OFFSET_X,
                    stroke.strokeAttributes.offsetX
                )
            )
        }
        if ((!newLayout.hasProperty(PathPoint.Property.OFFSET_Y)) && (stroke.strokeAttributes.offsetY != 0f)) {
            attributeList.add(
                Pair(
                    PathPoint.Property.OFFSET_Y,
                    stroke.strokeAttributes.offsetY
                )
            )
        }
        if ((!newLayout.hasProperty(PathPoint.Property.OFFSET_Z)) && (stroke.strokeAttributes.offsetZ != 0f)) {
            attributeList.add(
                Pair(
                    PathPoint.Property.OFFSET_Z,
                    stroke.strokeAttributes.offsetZ
                )
            )
        }
        if ((!newLayout.hasProperty(PathPoint.Property.RED)) && (stroke.strokeAttributes.red != 0f)) {
            attributeList.add(Pair(PathPoint.Property.RED, stroke.strokeAttributes.red))
        }
        if ((!newLayout.hasProperty(PathPoint.Property.GREEN)) && (stroke.strokeAttributes.green != 0f)) {
            attributeList.add(Pair(PathPoint.Property.GREEN, stroke.strokeAttributes.green))
        }
        if ((!newLayout.hasProperty(PathPoint.Property.BLUE)) && (stroke.strokeAttributes.blue != 0f)) {
            attributeList.add(Pair(PathPoint.Property.BLUE, stroke.strokeAttributes.blue))
        }
        if ((!newLayout.hasProperty(PathPoint.Property.ALPHA)) && (stroke.strokeAttributes.alpha != 1f)) {
            attributeList.add(Pair(PathPoint.Property.ALPHA, stroke.strokeAttributes.alpha))
        }*/

        if (!attributeList.isEmpty()) {
            var cont = 1;
            val size = newLayout.size
            var newPath = FloatArrayList()
            var first = false
            for (index in 0 until stroke.spline.path.size) {
                newPath.add(stroke.spline.path[index])
                if (cont % size == 0) {
                    for (attribute in attributeList) {
                        newPath.add(attribute.second)
                        if (!first) {
                            newLayout = newLayout.createExtendedLayout(attribute.first)
                        }
                    }
                    first = true
                }
                cont++
            }

            stroke.spline.path = newPath
            stroke.spline.layout = newLayout
        }
    }

}