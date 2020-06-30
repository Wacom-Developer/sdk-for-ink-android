/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.raster

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.wacom.ink.InterpolatedSpline
import com.wacom.ink.Phase
import com.wacom.ink.StrokeConstants
import com.wacom.ink.egl.EGLRenderingContext
import com.wacom.ink.format.InkSensorType
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.enums.InkSensorMetricType
import com.wacom.ink.format.input.*
import com.wacom.ink.format.rendering.PathPointProperties
import com.wacom.ink.format.rendering.RasterBrush
import com.wacom.ink.format.rendering.Style
import com.wacom.ink.format.tree.data.SensorData
import com.wacom.ink.format.tree.data.Stroke
import com.wacom.ink.format.tree.nodes.StrokeNode
import com.wacom.ink.model.IdentifiableImpl
import com.wacom.ink.rasterization.InkCanvas
import com.wacom.ink.rasterization.Layer
import com.wacom.ink.rasterization.StrokeRenderer
import com.wacom.ink.rendering.BlendMode
import com.wacom.will3.ink.raster.rendering.demo.*
import com.wacom.will3.ink.raster.rendering.demo.serialization.InkEnvironmentModel
import com.wacom.will3.ink.raster.rendering.demo.tools.raster.EraserRasterTool
import com.wacom.will3.ink.raster.rendering.demo.tools.raster.PencilTool
import com.wacom.will3.ink.raster.rendering.demo.tools.raster.RasterTool

/**
 * This is a surface for drawing raster inking.
 * Extends from SurfaceView
 */
class RasterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    interface InkingSurfaceListener {
        fun onSurfaceCreated()
    }

    private var inkCanvas: InkCanvas? = null
    private lateinit var viewLayer: Layer
    private lateinit var strokesLayer: Layer
    private lateinit var currentFrameLayer: Layer
    private lateinit var strokeRenderer: StrokeRenderer

    private var rasterInkBuilder = RasterInkBuilder() //The ink builder
    var rasterTool: RasterTool = PencilTool(context)
    private var defaults: StrokeConstants = StrokeConstants() //The stroke defaults

    // for serialisation
    lateinit var inkEnvironmentModel: InkEnvironmentModel // information about the environment
    lateinit var sensorData: SensorData
    lateinit var channelList: List<SensorChannel>

    lateinit var listener: InkingSurfaceListener

    var strokeNodeList = mutableListOf<Pair<StrokeNode, RasterBrush>>()
    var sensorDataList = mutableListOf<SensorData>()

    var isStylus: Boolean = false
    var newTool = false

    init {
        rasterInkBuilder.updatePipeline(PencilTool(context))
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);

        // the drawing is going to be performer on background
        // on a surface, so we initialize the surface
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, w: Int, h: Int) {
                // here, once the surface is crated, we are going to initialize ink canvas

                // firs we check that there is no other inkCanvas, in case there is we dispose it
                if (inkCanvas?.isDisposed == false) {
                    releaseResources();
                }

                inkCanvas =
                    InkCanvas(holder, EGLRenderingContext.EGLConfiguration(8, 8, 8, 8, 8, 8))
                viewLayer = inkCanvas!!.createViewLayer(w, h)
                strokesLayer = inkCanvas!!.createLayer(w, h)
                currentFrameLayer = inkCanvas!!.createLayer(w, h)

                inkCanvas!!.clearLayer(currentFrameLayer)

                strokeRenderer =
                    StrokeRenderer(inkCanvas, PencilTool(context).brush.toParticleBrush(), w, h)

                drawStrokes(strokeNodeList)
                renderView()

                if (listener != null) {
                    listener.onSurfaceCreated()
                }

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                releaseResources();
            }

            override fun surfaceCreated(p0: SurfaceHolder?) {
                //we don't have to do anything here
            }

        })

    }

    // This function is going to be call when we touch the surface
    fun surfaceTouch(event: MotionEvent) {
        if (event.resolveToolType() == InkInputType.PEN) {
            if ((newTool) || (!isStylus)) {
                newTool = false
                isStylus = true
                rasterInkBuilder.updateInputMethod(isStylus)
            }
        } else {
            if ((newTool) || (isStylus)) {
                newTool = false
                isStylus = false
                rasterInkBuilder.updateInputMethod(isStylus)
            }
        }

        for (i in 0 until event.historySize) {
            val pointerData = event.historicalToPointerData(i)
            rasterInkBuilder.add(pointerData.phase, pointerData, null)
        }

        val pointerData = event.toPointerData()
        rasterInkBuilder.add(pointerData.phase, pointerData, null)

        val (added, predicted) = rasterInkBuilder.build()

        if (pointerData.phase == Phase.BEGIN) {
            // initialize the sensor data each time a new stroke begin
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
                InkSensorType.ALTITUDE -> sensorData.add(channel, pointerData.altitudeAngle!!)
                InkSensorType.AZIMUTH -> sensorData.add(channel, pointerData.azimuthAngle!!)
            }
        }

        if ((pointerData.phase == Phase.END) && (rasterInkBuilder.splineProducer.allData != null)) {
            addStroke()
            sensorDataList.add(sensorData)
        }


        if (added != null) {
            drawStroke(event, added, predicted)
        }

        renderView()
    }

    private fun addStroke() {
        // Adding the style
        val style = Style(
            rasterTool.brush.name,              // Brush URI
            1,           // Particle random seed
            props = PathPointProperties(   // Coloring path properties
                red = defaults.red,
                green = defaults.green,
                blue = defaults.blue,
                alpha = defaults.alpha
            ),
            renderModeUri = rasterTool.getBlendMode().uri()
        )
        // Adding stroke to the Stroke Repository
        val path = Stroke(
            IdentifiableImpl.generateUUID(),                    // Generated UUID
            rasterInkBuilder.splineProducer.allData!!.copy(),   // Spline
            style                                               // Style
        )

        // Adding a node to the Ink tree
        val node = StrokeNode(IdentifiableImpl.generateUUID(), path)
        node.data.sensorDataID = sensorData.id
        node.data.sensorDataOffset = 0
        strokeNodeList.add(Pair(node, rasterTool.brush))
    }

    fun addStroke(strokeNode: StrokeNode, brush: RasterBrush) {
        strokeNodeList.add(Pair(strokeNode, brush))
    }

    fun addSensorData(sensorData: SensorData) {
        sensorDataList.add(sensorData)
    }

    fun setTool(tool: RasterTool) {
        newTool = true
        rasterTool = tool
        strokeRenderer.strokeBrush = rasterTool.brush.toParticleBrush()
        rasterInkBuilder.updatePipeline(rasterTool)

        if (tool is EraserRasterTool) {
            defaults.alpha = 0f
        }

    }

    fun setColor(color: Int) {
        defaults.red = Color.red(color) / 255f
        defaults.green = Color.green(color) / 255f
        defaults.blue = Color.blue(color) / 255f
        //defaults.alpha = 1f
    }


    // Renders the canvas content on the screen.
    private fun renderView() {
        inkCanvas!!.setTarget(viewLayer)
        // Copy the current frame layer in the view layer to present it on the screen.
        inkCanvas!!.drawLayer(currentFrameLayer, BlendMode.COPY)
        inkCanvas!!.invalidate()
    }

    // Draw stroke
    private fun drawStroke(
        event: MotionEvent,
        added: InterpolatedSpline,
        predicted: InterpolatedSpline?
    ) {
        strokeRenderer.drawPoints(added, defaults, event.action == MotionEvent.ACTION_UP)

        if (predicted != null) {
            strokeRenderer.drawPrelimPoints(predicted, defaults)
        }

        if (event.action != MotionEvent.ACTION_UP) {
            inkCanvas!!.setTarget(currentFrameLayer, strokeRenderer.strokeUpdatedArea)
            inkCanvas!!.clearColor()
            inkCanvas!!.drawLayer(strokesLayer, BlendMode.SOURCE_OVER)
            strokeRenderer.blendStrokeUpdatedArea(currentFrameLayer, rasterTool.getBlendMode())
        } else {
            strokeRenderer.blendStroke(strokesLayer, rasterTool.getBlendMode())
            inkCanvas!!.setTarget(currentFrameLayer)
            inkCanvas!!.clearColor()
            inkCanvas!!.drawLayer(strokesLayer, BlendMode.SOURCE_OVER)
        }
    }

    // Dispose the resources
    private fun releaseResources() {
        strokeRenderer.dispose()

        viewLayer.dispose()
        strokesLayer.dispose()
        currentFrameLayer.dispose()

        inkCanvas!!.dispose()
    }

    fun drawStrokes(strokeList: MutableList<Pair<StrokeNode, RasterBrush>>) {
        for (stroke in strokeList) {
            drawStroke(stroke.first, stroke.second)
        }
    }

    fun drawStroke(stroke: StrokeNode, brush: RasterBrush) {
        val style = stroke.data.style
        val renderModeUri = stroke.data.style?.renderModeUri ?: ""
        var renderMode = BlendMode.values().find { it.uri() == renderModeUri } ?: BlendMode.SOURCE_OVER

        defaults.red = style?.props?.red ?: 0f
        defaults.green = style?.props?.green ?: 0f
        defaults.blue = style?.props?.blue ?: 0f
        defaults.alpha = style?.props?.alpha ?: 1f

        val spline = stroke.data.spline
        val (added, _) = rasterInkBuilder.processSpline(spline, null)

        if (added != null) {
            strokeRenderer.strokeBrush = brush.toParticleBrush()
            strokeRenderer.drawPoints(added, defaults, true)

            strokeRenderer.blendStroke(strokesLayer, renderMode)
            inkCanvas?.setTarget(currentFrameLayer)
            inkCanvas?.clearColor()
            inkCanvas?.drawLayer(strokesLayer, BlendMode.SOURCE_OVER)
            renderView()
        }
    }

    fun clear() {
        strokeNodeList.clear()
        sensorDataList.clear()
        if ((inkCanvas != null) && (!inkCanvas!!.isDisposed)) {
            inkCanvas!!.clearLayer(currentFrameLayer)
            inkCanvas!!.clearLayer(viewLayer)
            inkCanvas!!.clearLayer(strokesLayer)
            renderView()
        }
    }

}