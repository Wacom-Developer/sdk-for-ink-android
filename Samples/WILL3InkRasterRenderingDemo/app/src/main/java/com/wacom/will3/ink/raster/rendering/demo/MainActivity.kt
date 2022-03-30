/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wacom.ink.format.InkModel
import com.wacom.ink.format.input.SensorChannel
import com.wacom.ink.format.rendering.RasterBrush
import com.wacom.ink.format.serialization.Will3Codec
import com.wacom.ink.format.tree.groups.StrokeGroupNode
import com.wacom.ink.format.tree.nodes.StrokeNode
import com.wacom.ink.model.IdentifiableImpl
import com.wacom.ink.model.Identifier
import com.wacom.will3.ink.raster.rendering.demo.brush.BrushPalette
import com.wacom.will3.ink.raster.rendering.demo.raster.RasterView
import com.wacom.will3.ink.raster.rendering.demo.serialization.InkEnvironmentModel
import com.wacom.will3.ink.raster.rendering.demo.tools.raster.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), RasterView.InkingSurfaceListener {

    val OPEN_FILE_ACTION = 1
    val CREATE_FILE_ACTION = 2
    val EXPORT_TO_JPEG_ACTION = 3
    val EXPORT_TO_PNG_ACTION = 4

    //-- Variables For serialisation
    private lateinit var mainGroup: StrokeGroupNode // This is a list of StrokeNode.

    // A StrokeNode contains information about an Stroke
    private lateinit var inkModel: InkModel // This is the main serializable class, what we save and

    // Environment information to save in the ink model
    private lateinit var inkEnvironmentModel: InkEnvironmentModel

    //-- End serialisation

    private var defaultDrawingTool = PencilTool.uri
    private var drawingTool: RasterTool? = null

    private var popupWindow: PopupWindow? = null

    private var drawingColor: Int = Color.argb(255, 74, 74, 74)
    private var lastEvent: MotionEvent? = null

    private var tempStrokeFile: String? = null
    private var refreshing: Boolean = false
    private var currentBackground: Int = 0

    private val scope = CoroutineScope(newSingleThreadContext("synchronizationPool"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We maintain he same orientation
        disableRotation()

        if (savedInstanceState != null) {
            drawingColor = savedInstanceState.getInt("selectedColor")
            if (savedInstanceState.getString("selectedTool") != null) {
                defaultDrawingTool = savedInstanceState.getString("selectedTool")!!
            }
            tempStrokeFile = savedInstanceState.getString("strokes")
            currentBackground = savedInstanceState.getInt("currentBackground")
            refreshing = true

        } else {
            resetInkModel()
        }

        inkEnvironmentModel =
            InkEnvironmentModel(this) // Initializes the environment data for serialization

        setColor(drawingColor) //set default color

        background_waiting.setOnTouchListener { _, event ->
            true
        }

        rasterDrawingSurface.setOnTouchListener { _, event ->
            // We save the last event just in case we receive a CANCEL action
            // when we have a CANCEL action we get indeterminate coordinate values,
            // so in this case we pass the latest value coordinates
            if ((event.action == MotionEvent.ACTION_DOWN) ||
                (event.action == MotionEvent.ACTION_MOVE) ||
                (event.action == MotionEvent.ACTION_UP)
            ) {
                lastEvent = MotionEvent.obtain(event)
            } else {
                lastEvent?.action = MotionEvent.ACTION_UP //we convert the latest event in END event
            }

            if (lastEvent != null) {
                rasterDrawingSurface.surfaceTouch(lastEvent!!)
            }

            if (event.action == MotionEvent.ACTION_UP) {
                lastEvent = null
            }
            true
        }

        rasterDrawingSurface.inkEnvironmentModel = inkEnvironmentModel
        rasterDrawingSurface.listener = this

        selectPaper(currentBackground)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        // save the current selected tool, color and strokes
        refreshing = true
        val tempFile = File(cacheDir, "tempStrokes.uim")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        save(Uri.fromFile(tempFile))

        savedInstanceState.putString("strokes", tempFile.absolutePath)
        savedInstanceState.putString("selectedTool", drawingTool?.uri())
        savedInstanceState.putInt("selectedColor", drawingColor)

        savedInstanceState.putInt("currentBackground", currentBackground)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Make sure the request was successful
        if (resultCode == Activity.RESULT_OK) {
            // Check which request we're responding to
            if (requestCode == OPEN_FILE_ACTION) {
                scope.launch {
                    load(data!!.data!!)
                }
            } else if (requestCode == CREATE_FILE_ACTION) {
                save(data!!.data!!)
            } else if (requestCode == EXPORT_TO_PNG_ACTION) {
                saveToRaster(data!!.data!!, Bitmap.CompressFormat.PNG)
            } else if (requestCode == EXPORT_TO_JPEG_ACTION) {
                saveToRaster(data!!.data!!, Bitmap.CompressFormat.JPEG)
            }
        }
    }

    fun selectColor(view: View) {
        ColorPickerPopup.Builder(this)
            .initialColor(drawingColor) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(false)
            .showValue(false)
            .build()
            .show(view, object : ColorPickerObserver() {
                override fun onColorPicked(color: Int) {
                    this@MainActivity.setColor(color)
                }

                fun onColor(color: Int, fromUser: Boolean) {}
            })
    }

    fun setColor(color: Int) {
        drawingColor = color
        btnColor.setColorFilter(drawingColor, PorterDuff.Mode.SRC_ATOP)
        rasterDrawingSurface.setColor(drawingColor)
    }

    fun selectTool(uri: String) {
        val view = when (uri) {
            WaterbrushTool.uri -> btn_water_brush
            CrayonTool.uri -> btn_crayon
            EraserRasterTool.uri -> btn_eraser
            else -> btn_pencil
        }
        selectTool(view)
    }

    fun selectTool(view: View) {
        when (view.id) {
            R.id.btn_pencil -> setTool(view, PencilTool(this))
            R.id.btn_water_brush -> setTool(view, WaterbrushTool(this))
            R.id.btn_crayon -> setTool(view, CrayonTool(this))
            R.id.btn_eraser -> setTool(view, EraserRasterTool(this))
        }
    }

    fun setTool(view: View, tool: RasterTool) {
        drawingTool = tool

        val dt = drawingTool as RasterTool
        rasterDrawingSurface.setTool(dt)
        highlightTool(view)
    }

    fun highlightTool(view: View) {
        btn_pencil.setActivated(false)
        btn_water_brush.setActivated(false)
        btn_ink_brush.setActivated(false)
        btn_crayon.setActivated(false)
        btn_eraser.setActivated(false)
        view.setActivated(true)
    }

    fun load(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("*/*");
        startActivityForResult(intent, OPEN_FILE_ACTION)
    }

    fun load(path: Uri) {
        this@MainActivity.runOnUiThread(java.lang.Runnable {
            if (!refreshing) {
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }

            background_waiting.visibility = View.VISIBLE

            // Before loading we clear the screen
            rasterDrawingSurface.clear();

        })

        try {
            getContentResolver().openFileDescriptor(path, "r").use { pfd ->
                FileInputStream(pfd?.fileDescriptor).use { fileInputStream ->

                    val bytes = fileInputStream.readBytes()
                    inkModel = Will3Codec.decode(bytes)
                    mainGroup = inkModel.inkTree!!.root!! as StrokeGroupNode

                    val it = inkModel.inkTree!!.root!!.iterator()
                    while (it.hasNext()) {
                        val stroke = it.next()
                        if (stroke is StrokeNode) {
                            val brushUri = stroke.data.style?.brushURI
                            if (brushUri != null) {
                                var brush = inkModel.brushRepository.getBrush(brushUri)
                                if (brush == null) {
                                    // try to find a compatible brush
                                    brush = BrushPalette.getBrush(this, brushUri)
                                }

                                if (brush is RasterBrush) { // we are in raster tool
                                    var sensorChannelList: List<SensorChannel>? = null
                                    val sensorDataID = stroke.data.sensorDataID
                                    if (sensorDataID != null) {
                                        rasterDrawingSurface.addSensorData(
                                            inkModel.sensorDataRepository.get(
                                                sensorDataID
                                            )!!
                                        )

                                        val inputContextId =
                                            inkModel.sensorDataRepository.get(sensorDataID)?.inputContextId
                                        if (inputContextId != null) {
                                            val sensorContextId =
                                                inkModel.inputConfiguration.getInputContext(
                                                    inputContextId
                                                )
                                                    ?.sensorContextId
                                            if (sensorContextId != null) {
                                                sensorChannelList =
                                                    inkModel.inputConfiguration.getSensorContext(
                                                        sensorContextId
                                                    )?.getSensorChannelsContexts()?.first()
                                                        ?.getAll()
                                            }
                                        }
                                    }

                                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                                        rasterDrawingSurface.drawStroke(
                                            stroke,
                                            brush,
                                            sensorChannelList
                                        )
                                    })

                                    rasterDrawingSurface.addStroke(stroke, brush)
                                }
                            }
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        this@MainActivity.runOnUiThread(java.lang.Runnable {
            if (!refreshing) {
                Toast.makeText(this, "Loading completed.", Toast.LENGTH_SHORT).show()
            }
            background_waiting.visibility = View.GONE
            rasterDrawingSurface.setTool(drawingTool!!)
            setColor(drawingColor)
        })

        refreshing = false
    }

    private fun resetInkModel() {
        inkModel = InkModel()

        val root = StrokeGroupNode(Identifier())

        inkModel.inkTree.root = root
        mainGroup = StrokeGroupNode(Identifier())
        root.add(mainGroup)

        //TODO inkModel.brushRepository.addVectorBrush(brush)
        //TODO splines.clear()
        //TODO paths.clear()
    }


    fun save(uri: Uri) {
        if (!refreshing) {
            Toast.makeText(this, "Saving..", Toast.LENGTH_SHORT).show()
        }

        inkEnvironmentModel.registerInModel(inkModel)
        inkModel.knowledgeGraph.add(
            inkModel.inkTree!!.root!!.id.toUUIDString(),
            "created",
            "" + System.currentTimeMillis()
        )
        inkModel.knowledgeGraph.add(inkModel.inkTree!!.root!!.id.toUUIDString(), "author", "WILL 3")


        // For raster serialization
        for (sensorData in rasterDrawingSurface.sensorDataList) {
            inkModel.sensorDataRepository.add(sensorData)
        }

        for (stroke in rasterDrawingSurface.strokeNodeList) {
            if (inkModel.brushRepository.getBrush(stroke.second.name) == null) {
                inkModel.brushRepository.addRasterBrush(stroke.second)
            }
            if (!mainGroup.contains(stroke.first)) {
                mainGroup.add(stroke.first)
            }
        }

        getContentResolver().openFileDescriptor(uri, "w").use { pfd ->
            FileOutputStream(pfd?.fileDescriptor).use { fileOutputStream ->
                Will3Codec.encode(inkModel, fileOutputStream)
            }
        }
    }

    override fun onSurfaceCreated() {
        if (drawingTool != null) {
            rasterDrawingSurface.setTool(drawingTool!!)
        } else {
            selectTool(defaultDrawingTool) // set default tool
        }

        if (tempStrokeFile != null) {
            scope.launch {
                load(Uri.fromFile(File(tempStrokeFile)))
                File(tempStrokeFile).delete()
                tempStrokeFile = null
            }
        }
    }

    fun clear(view: View) {
        resetInkModel()
        rasterDrawingSurface.clear()
    }

    fun openPaperDialog(view: View) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.background_selector, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        val screenPos = IntArray(2)
        view.getLocationOnScreen(screenPos)
        popupWindow!!.showAtLocation(
            view,
            Gravity.NO_GRAVITY,
            screenPos[0],
            screenPos[1] + navbar_container.height
        )
    }

    fun selectPaper(pos: Int) {
        if (pos == 0) {
            changeBackground(R.drawable.btn_paper_01, R.drawable.background1)
        } else if (pos == 1) {
            changeBackground(R.drawable.btn_paper_02, R.drawable.background2)
        } else {
            changeBackground(R.drawable.btn_paper_03, R.drawable.btn_paper_03)
        }
    }

    fun selectPaper(view: View) {
        when (view.id) {
            R.id.btnBackground1 -> {
                changeBackground(R.drawable.btn_paper_01, R.drawable.background1)
                currentBackground = 0
            }
            R.id.btnBackground2 -> {
                changeBackground(R.drawable.btn_paper_02, R.drawable.background2)
                currentBackground = 1
            }
            R.id.btnBackground3 -> {
                changeBackground(R.drawable.btn_paper_03, R.drawable.btn_paper_03)
                currentBackground = 2
            }
        }
    }

    fun changeBackground(background: Int, paper: Int) {
        btnBackground.setImageResource(background)
        drawingLayout.setBackgroundResource(paper)
        if (popupWindow != null) {
            popupWindow!!.dismiss()
        }
    }

    fun selectSaveFile(view: View) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_TITLE, "ink.uim");
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, CREATE_FILE_ACTION)
    }

    /*fun enableRotation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }*/

    fun disableRotation() {
        val rotation: Int = getWindowManager().getDefaultDisplay().getRotation()
        when (getResources().getConfiguration().orientation) {
            Configuration.ORIENTATION_PORTRAIT -> if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_180) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            Configuration.ORIENTATION_LANDSCAPE -> if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
            }
        }
    }

    fun openMenu(view: View) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.menu_layout, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        val screenPos = IntArray(2)
        view.getLocationOnScreen(screenPos)
        popupWindow?.showAtLocation(
            view,
            Gravity.NO_GRAVITY,
            screenPos[0],
            screenPos[1] + navbar_container.height
        )
    }

    fun exportToPNG(view: View) {
        if (popupWindow != null) {
            popupWindow!!.dismiss()
        }

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.setType("image/png")
        intent.putExtra(Intent.EXTRA_TITLE, "ink.png");
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, EXPORT_TO_PNG_ACTION)
    }

    fun exportToJPEG(view: View) {
        if (popupWindow != null) {
            popupWindow!!.dismiss()
        }

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.setType("image/jpeg")
        intent.putExtra(Intent.EXTRA_TITLE, "ink.jpeg");
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, EXPORT_TO_JPEG_ACTION)
    }

    fun saveToRaster(uri: Uri, format: Bitmap.CompressFormat) {
        try {
            getContentResolver().openFileDescriptor(uri, "w").use { pfd ->
                FileOutputStream(pfd?.fileDescriptor).use { fileOutputStream ->
                    val bmp = rasterDrawingSurface.toBitmap(Color.WHITE)
                    bmp?.compress(format, 100, fileOutputStream);
                    bmp.recycle()
                }
            }
            Toast.makeText(this, "Exported successfully.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ".plus(e.message), Toast.LENGTH_SHORT).show()
        }
    }
}
