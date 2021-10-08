/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.vector

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.wacom.ink.*
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.enums.InkSensorMetricType
import com.wacom.ink.format.enums.InkSensorType
import com.wacom.ink.format.input.SensorChannel
import com.wacom.ink.format.tree.data.SensorData
import com.wacom.ink.format.util.ScalarUnit
import com.wacom.ink.manipulation.ManipulationMode
import com.wacom.ink.manipulation.SpatialModel
import com.wacom.ink.manipulation.callbacks.ErasingCallback
import com.wacom.ink.manipulation.callbacks.SelectingCallback
import com.wacom.ink.model.Identifier
import com.wacom.ink.model.InkStroke
import com.wacom.ink.model.StrokeAttributes
import com.wacom.will3.ink.vector.rendering.demo.*
import com.wacom.will3.ink.vector.rendering.demo.manipulation.SelectionBox
import com.wacom.will3.ink.vector.rendering.demo.manipulation.HistoryManager
import com.wacom.will3.ink.vector.rendering.demo.serialization.InkEnvironmentModel
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


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

    enum class State {
        DRAWING, ERASING, SELECTING
    }

    companion object {
        // when opening the waiting background it should stay for a minimum time,
        // otherwise it will show a flick effect not very nice. By default we has set this
        // minimum time to 0.1 seconds.
        private val BACKGROUND_MINIMUM_TIME = 100L

        // we need to set a maximum number of bitmaps, otherwise we can run out of memory
        private val MAX_NUMBER_BITMAPS = 5
    }

    // Relevant for serialization
    lateinit var inkEnvironmentModel: InkEnvironmentModel
    lateinit var sensorData: SensorData
    lateinit var channelList: List<SensorChannel>

    var drawingTool: VectorTool = PenTool() // default tool
    var inkBuilder = VectorInkBuilder()
    var deleteInkBuilder = VectorInkBuilder()

    private var currentPath = Path() // This will contain the current drawing path before it is finished

    // Ink Manipulations
    var strokes = mutableMapOf<Identifier, WillStroke>() // A list of existing strokes
    private lateinit var spatialModel: SpatialModel // The spatial model is user for
    private lateinit var historyManager: HistoryManager

    val scope = CoroutineScope(newSingleThreadContext("synchronizationPool"))
    val counterContext = newSingleThreadContext("CounterContext")

    private var unprocessedBegin = false

    lateinit var activity: MainActivity
    private var currentColor: Int = 0
    var isStylus: Boolean = false
    var newTool = false
    private var selectedStrokes = arrayListOf<Identifier>() // Ink Manipulations: Selection

    val paintSelected = Paint().also {
        it.isAntiAlias = true
        it.isFilterBitmap = true
        it.isDither = true
        it.color = Color.BLUE
    }

    val paintSelect = Paint().also {
        it.color = Color.BLACK
    }

    var selectionBox: SelectionBox? = null

    private val INVALID_POINTER_ID = -1
    private var activePointerID: Int = INVALID_POINTER_ID
    private var lastEvent: MotionEvent? = null

    private var nextSpline: Spline? = null

    // Because drawing path it is a high time consuming we are going to draw the path to a bitmap
    // and then draw the bitmap. We use a list of bitmaps instead of only a bitmap
    // to be able to handle when selection different z-order strokes.
    private var mBitmapList = arrayListOf<Pair<Bitmap, Boolean>>()
    private var mCanvas: Canvas? = null
    private var mBitmapPaint = Paint(Paint.DITHER_FLAG)

    private var mState = State.DRAWING
    private var mZOrder = 0

    private val channel = Channel<Pair<State, Pair<WillStroke?, Spline?>>>()

    private var numProcess = 0
    private var initialized = false
    private var selecting = false

    /**
     * In order to make the strokes look nice and smooth, it is recommended to enable:
     *  - Antialiasing
     *  - Dither
     */
    private var mDrawPaint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }

    private var mErasePaint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun createBmps() {
        try {
            mBitmapList.forEach { it.first.recycle() }
            mBitmapList.clear()

            var lastCanvas: Canvas? = null
            var lastSelectedCanvas: Canvas? = null
            var numBitmaps = 0

            val list = arrayListOf<Pair<Bitmap, Boolean>>()
            var selected = false
            var changed = true

            var selectedWidth = 0
            var selectedHeight = 0
            val matrix = Matrix()
            if (!selectedStrokes.isEmpty()) {
                val selectedBounds = RectF()
                for ((key, stroke) in strokes) {
                    if (selectedStrokes.contains(key)) {
                        val bounds = RectF()
                        stroke.path.computeBounds(bounds, true)
                        selectedBounds.union(bounds)
                    }
                }
                selectedWidth = selectedBounds.width().toInt()
                selectedHeight = selectedBounds.height().toInt()
                matrix.postTranslate(-selectedBounds.left, -selectedBounds.top)
            }

            val sortedStrokes = strokes.values.toList().sortedBy { it.zOrder }

            for (stroke in sortedStrokes) {
                if (stroke.id in selectedStrokes) {
                    if (!selected) {
                        changed = true
                    }
                    selected = true
                } else {
                    if (selected) {
                        changed = true
                    }
                    selected = false
                }

                if ((changed) && (numBitmaps <= MAX_NUMBER_BITMAPS)) {
                    if (selected) {
                        if (selectedWidth > 0 && selectedHeight > 0) {
                            val bitmap = Bitmap.createBitmap(
                                selectedWidth,
                                selectedHeight,
                                Bitmap.Config.ARGB_8888
                            )
                            list.add(Pair<Bitmap, Boolean>(bitmap, selected))
                            lastSelectedCanvas = Canvas(bitmap)
                        }
                    } else {
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        list.add(Pair<Bitmap, Boolean>(bitmap, selected))
                        lastCanvas = Canvas(bitmap)
                    }
                    numBitmaps++
                }

                if (selected) {
                    lastSelectedCanvas?.save()
                    lastSelectedCanvas?.setMatrix(matrix)
                    lastSelectedCanvas?.drawPath(stroke.path, paintSelected)
                    lastSelectedCanvas?.restore()
                } else {
                    val color = Color.argb(
                        (stroke.strokeAttributes.alpha * 255).toInt(),
                        (stroke.strokeAttributes.red * 255).toInt(),
                        (stroke.strokeAttributes.green * 255).toInt(),
                        (stroke.strokeAttributes.blue * 255).toInt()
                    )
                    mDrawPaint.color = color
                    lastCanvas?.drawPath(stroke.path, mDrawPaint)
                }

                changed = false
            }

            if (list.isEmpty()) {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                list.add(Pair<Bitmap, Boolean>(bitmap, false))
                mCanvas = Canvas(bitmap)
            } else {
                mCanvas = lastCanvas
            }

            mBitmapList = list
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createBmps()
    }

    fun setSpatialModel(spatialModel: SpatialModel) {
        this.spatialModel = spatialModel
        historyManager = HistoryManager()

        if (!initialized) {
            //CoroutineScope(newSingleThreadContext("erasing")).launch {
            GlobalScope.launch {
                processStrokes()
            }
        }
        initialized = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val matrix = selectionBox?.getMatrix()

        mBitmapList.forEach {
            if (it.second) {
                if (matrix != null) {
                    canvas.drawBitmap(it.first, matrix!!, paintSelected)
                }
            } else {
                canvas.drawBitmap(it.first, 0f, 0f, mBitmapPaint)
            }
        }

        when (mState) {
            State.DRAWING -> canvas.drawPath(currentPath, mDrawPaint)
            State.SELECTING -> canvas.drawPath(currentPath, paintSelect)
        }

        selectionBox?.drawSelectionBox(canvas)
    }

    fun onTouch(event: MotionEvent) {
        try {
            var canDraw = false
            if (selectionBox != null) {
                if (!selectionBox!!.onTouch(event)) {
                    if (!selectionBox!!.isChanged()) {
                        selectionBox = null
                        strokes = historyManager.undo(false)!!
                        spatialModel.reset()
                        for ((key, value) in strokes) {
                            spatialModel.add(value)
                        }

                        selectedStrokes.clear()
                        createBmps()
                        updateDrawing()
                        return
                    } else {
                        var newSelectionBox = selectionBox
                        selectionBox = null
                        activity.openBackground(activity.getString(R.string.applying_changes), true)
                        scope.launch {
                            newSelectionBox!!.applyChanges()
                            newSelectionBox = null
                            historyManager.resetRedo();
                            selectedStrokes.clear()
                            activity.runOnUiThread(java.lang.Runnable {
                                createBmps()
                                updateDrawing()
                                activity.closeBackground()
                            })
                        }
                        return
                    }
                }
            } else {
                canDraw = true
            }

            val action = event.actionMasked
            val pointerIndex =
                (event.action and MotionEvent.ACTION_POINTER_INDEX_MASK) shr MotionEvent.ACTION_POINTER_INDEX_SHIFT

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
                } else if (event.action == MotionEvent.ACTION_CANCEL) {
                    lastEvent?.action =
                        MotionEvent.ACTION_UP //we convert the latest event in END event
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onTouchForDrawing(event: MotionEvent) {
        if (event.resolveToolType() == InkInputType.PEN) {
            if ((newTool) || (!isStylus)) {
                newTool = false
                isStylus = true
                inkBuilder.updateInputMethod(isStylus)
                deleteInkBuilder.updateInputMethod(isStylus)
            }
        } else {
            if ((newTool) || (isStylus)) {
                newTool = false
                isStylus = false
                inkBuilder.updateInputMethod(isStylus)
                deleteInkBuilder.updateInputMethod(isStylus)
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
                    when (channel.typeURI) {
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
                activity.openBackground(activity.getString(R.string.deleting), false)
                if (pointerData.phase == Phase.END) {
                    if (inkBuilder.splineProducer.allData != null) {
                        if (!inkBuilder.splineProducer.allData!!.equals(nextSpline)) {
                            nextSpline = inkBuilder.splineProducer.allData!!.copy()
                            numProcess++
                            scope.launch {
                                channel.send(
                                    Pair<State, Pair<WillStroke?, Spline?>>(
                                        State.ERASING,
                                        Pair(null, nextSpline)
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // Assume we are selecting
                if (pointerData.phase == Phase.END) {
                    if (!selecting) {
                        scope.launch {
                            channel.send(
                                Pair<State, Pair<WillStroke?, Spline?>>(
                                    State.SELECTING,
                                    Pair(null, inkBuilder.splineProducer.allData!!.copy())
                                )
                            )
                        }
                    }
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
                if (mState == State.ERASING) {
                        mCanvas?.drawPath(path, mErasePaint)
                }
            }

            if (pointerData.phase == Phase.END) {
                if (mState == State.DRAWING) {
                    mCanvas?.drawPath(currentPath, mDrawPaint)
                }
                currentPath = Path()
            }

            updateDrawing()

        } else if (pointerData.phase == Phase.BEGIN) {
            unprocessedBegin = true
        }

    }

    private fun addStroke(event: MotionEvent) {
        val brush = inkBuilder.tool.brush
        val stroke = WillStroke(inkBuilder.splineProducer.allData!!.copy(), brush, mZOrder++)
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

            override var red: Float = Color.red(mDrawPaint.color) / 255f
            override var green: Float = Color.green(mDrawPaint.color) / 255f
            override var blue: Float = Color.blue(mDrawPaint.color) / 255f
            override var alpha: Float = mDrawPaint.alpha.toFloat() / 255
        }

        scope.launch {
            channel.send(Pair<State, Pair<WillStroke?, Spline?>>(State.DRAWING, Pair(stroke, null)))
        }
        //spatialModel.add(stroke)
        stroke.path = currentPath
        strokes[stroke.id] = stroke

        val copy = mutableListOf<WillStroke>()
        for ((key, value) in strokes) {
            copy.add(value)
        }

        historyManager.addStrokes(copy)
    }

    suspend fun removeStroke(spline: Spline) {
        withContext(counterContext) {
            val time = System.currentTimeMillis()
            runBlocking {
                val removed = arrayListOf<Identifier>()
                spatialModel.erase(
                    spline,
                    drawingTool.brush,
                    if (drawingTool.drawingMode == VectorTool.DrawingMode.ERASING_PARTIAL_STROKE)
                        ManipulationMode.PARTIAL_STROKE else ManipulationMode.WHOLE_STROKE,
                    object : ErasingCallback {
                        override fun onStrokeAdded(stroke: InkStroke) {
                            deleteBuildStroke(stroke as WillStroke)
                            for (id in removed) strokes.remove(id)
                        }

                        override fun onStrokeRemoved(id: Identifier) {
                            val removedStroke = strokes.remove(id)
                            if (removedStroke == null) {
                                removed.add(id)
                            }
                        }
                    }
                )

                val copy = mutableListOf<WillStroke>()
                for ((key, value) in strokes) {
                    copy.add(value)
                }

                historyManager.addStrokes(copy)
            }
            numProcess--
            if (numProcess == 0) {
                createBmps()
                updateDrawing()
                val wait = System.currentTimeMillis() - time
                if (wait < BACKGROUND_MINIMUM_TIME) {
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            activity.runOnUiThread {
                                activity.closeBackground()
                            }
                        }
                    }, BACKGROUND_MINIMUM_TIME - wait)
                } else {
                    activity.runOnUiThread {
                        activity.closeBackground()
                    }
                }
            }
        }
    }

    suspend fun selectStroke(spline: Spline) {
        withContext(counterContext) {
            val time = System.currentTimeMillis()
            runBlocking {
                activity.runOnUiThread {
                    activity.openBackground(activity.getString(R.string.selecting_strokes), true)
                }

                val removed = arrayListOf<Identifier>()
                //undoManager.addStrokes(strokes) // add the current stroke list in case we want to go back to its state

                spatialModel.select(spline,
                    if (drawingTool.drawingMode == VectorTool.DrawingMode.SELECTING_PARTIAL_STROKE)
                        ManipulationMode.PARTIAL_STROKE else ManipulationMode.WHOLE_STROKE,
                    object : SelectingCallback {
                        override fun onStrokeAdded(stroke: InkStroke) {
                            buildStroke(stroke as WillStroke, null)
                            for (id in removed) strokes.remove(id)
                        }

                        override fun onStrokeRemoved(id: Identifier) {
                            val removedStroke = strokes.remove(id)
                            if (removedStroke == null) {
                                removed.add(id)
                            }
                        }

                        override fun onStrokeSelected(id: Identifier) {
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

                val copy = mutableListOf<WillStroke>()
                for ((key, value) in strokes) {
                    copy.add(value)
                }

                historyManager.addStrokes(copy, false)
            }
            createBmps()
            updateDrawing()
            val wait = System.currentTimeMillis() - time
            if (wait < BACKGROUND_MINIMUM_TIME) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        activity.runOnUiThread {
                            activity.closeBackground()
                        }
                    }
                }, BACKGROUND_MINIMUM_TIME - wait)
            } else {
                activity.runOnUiThread {
                    activity.closeBackground()
                }
            }
            selecting = false
        }
    }

    fun setTool(tool: VectorTool) {
        newTool = true
        drawingTool = tool
        inkBuilder.updatePipeline(tool)
        deleteInkBuilder.updatePipeline(tool)
        selectedStrokes.clear()
        setColor(currentColor)

        if ((drawingTool.uri() == EraserVectorTool.uri) ||
            (drawingTool.uri() == EraserWholeStrokeTool.uri)
        ) {
            mState = State.ERASING
        } else if ((drawingTool.uri() == SelectorPartialStrokeTool.uri) ||
            (drawingTool.uri() == SelectorWholeStrokeTool.uri)
        ) {
            mState = State.SELECTING
        } else {
            mState = State.DRAWING
        }

        mDrawPaint.color = currentColor
    }

    fun setColor(color: Int) {
        currentColor = adjustAlpha(color, drawingTool.alphaValue)
        if (mState == State.DRAWING) {
            mDrawPaint.color = currentColor
        }
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
        currentPath = Path()
        strokes.clear()
        selectedStrokes.clear()
        selectionBox = null
        setZOrder(0)
        createBmps()
        updateDrawing()
    }

    fun undo() {
        if (selectionBox == null) {
            strokes = historyManager?.undo()!!
            spatialModel.reset()
            for ((key, value) in strokes) {
                spatialModel.add(value)
            }
            createBmps()
            updateDrawing()
        }
    }

    fun redo() {
        if (selectionBox == null) {
            strokes = historyManager?.redo()!!
            spatialModel.reset()
            for ((key, value) in strokes) {
                spatialModel.add(value)
            }
            createBmps()
            updateDrawing()
        }
    }


    fun buildStroke(stroke: WillStroke, channelList: List<SensorChannel>?) {
        // add the attributes
        addRequiredValuesToPath(stroke)

        // scale values
        /*if (channelList != null) {
            // get the default resolution for the current screen
            val sensorChannel = SensorChannel(
                InkSensorType.X,
                InkSensorMetricType.LENGTH,
                ScalarUnit.INCH,
                0.0f,
                0f,
                2
            )
            scaleValues(stroke, channelList, sensorChannel.resolution)
        }*/

        /*if (stroke.sensorData != null) {
            stroke.sensorData = inkEnvironmentModel.createSensorData(stroke.sensorData!!)
        }*/

        val (path, _) = inkBuilder.buildPath(stroke.spline, null, true, true)
        if (path != null) {
            stroke.path = path
            strokes[stroke.id] = stroke
        }
    }

    fun deleteBuildStroke(stroke: WillStroke) {
        // add the attributes
        addRequiredValuesToPath(stroke)

        val (path, _) = deleteInkBuilder.buildPath(stroke.spline, null, true, true)
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

    fun scaleValues(stroke: WillStroke, channelList: List<SensorChannel>, resolution: Double) {
        var resX = 0.0
        var resY = 0.0
        for (channel in channelList) {
            when (channel.typeURI) {
                InkSensorType.X -> resX = channel.resolution
                InkSensorType.Y -> resY = channel.resolution
            }
        }

        if ((resX > 0) && (resY > 0)) {
            var scaleFactor = Math.min(resolution / resX, resolution / resY).toFloat()
            if (scaleFactor != 1f) {
                stroke.spline.transform(scaleFactor, scaleFactor, scaleFactor, 0f, 0f, 0f, 0f, 0f)
            }
        }
    }

    fun updateDrawing() {
        activity.runOnUiThread { invalidate() }
    }

    fun setZOrder(value: Int) {
        mZOrder = value
    }

    suspend fun processStrokes() {
        while (true) {
            try {
                runBlocking {
                    //always waiting for inputs to process
                    val operation = channel.receive()
                    when (operation.first) {
                        State.ERASING -> removeStroke(operation.second.second!!)
                        State.DRAWING -> spatialModel.add(operation.second.first!!)
                        State.SELECTING -> selectStroke(operation.second.second!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}