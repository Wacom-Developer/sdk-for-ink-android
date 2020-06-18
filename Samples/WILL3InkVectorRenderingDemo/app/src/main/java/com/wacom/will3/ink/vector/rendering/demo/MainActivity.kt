/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aditya.filebrowser.Constants
import com.aditya.filebrowser.FileChooser
import com.aditya.filebrowser.FolderChooser
import com.wacom.ink.format.InkModel
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.input.SensorChannel
import com.wacom.ink.format.serialization.Will3Codec
import com.wacom.ink.format.tree.data.Stroke
import com.wacom.ink.format.tree.groups.StrokeGroupNode
import com.wacom.ink.format.tree.nodes.StrokeNode
import com.wacom.ink.manipulation.SpatialModel
import com.wacom.ink.model.IdentifiableImpl
import com.wacom.ink.model.StrokeAttributes
import com.wacom.ink.rendering.VectorBrush
import com.wacom.will3.ink.vector.rendering.demo.serialization.InkEnvironmentModel
import com.wacom.will3.ink.vector.rendering.demo.tools.Tool
import com.wacom.will3.ink.vector.rendering.demo.tools.vector.*
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStrokeFactory
import kotlinx.android.synthetic.main.activity_main.*
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    val OPEN_FILE_ACTION = 1
    val SELECT_FOLDER_ACTION = 2

    //-- Variables For serialisation
    private lateinit var mainGroup: StrokeGroupNode // This is a list of StrokeNode.

    // A StrokeNode contains information about an Stroke
    private lateinit var inkModel: InkModel // This is the main serializable class, what we save and

    // Environment information to save in the ink model
    private lateinit var inkEnvironmentModel: InkEnvironmentModel

    //-- End serialisation

    // This is used for inking vectors.
    // Note: This is only necessary when we want to removing on vector inking
    private lateinit var spatialModel: SpatialModel

    private var defaultDrawingTool = PenTool.uri
    private var drawingTool: Tool? = null

    private var popupWindow: PopupWindow? = null

    private var drawingColor: Int = Color.argb(255, 74, 74, 74)

    private var tempStrokeFile: String? = null
    private var refreshing: Boolean = false
    private var currentBackground: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            drawingColor = savedInstanceState.getInt("selectedColor")
            if (savedInstanceState.getString("selectedTool") != null) {
                defaultDrawingTool = savedInstanceState.getString("selectedTool")!!
            }
            tempStrokeFile = savedInstanceState.getString("strokes")
            currentBackground = savedInstanceState.getInt("currentBackground")
            refreshing = true
        } else {
            resetInkModel() // Init and reset the InkModel for serialization
        }

        inkEnvironmentModel =
            InkEnvironmentModel(this) // Initializes the environment data for serialization

        spatialModel = SpatialModel(WillStrokeFactory()) // Initializes the spatial model
        vectorDrawingView.setSpatialModel(spatialModel)
        vectorDrawingView.activity = this

        setColor(drawingColor) //set default color
        vectorDrawingView.setOnTouchListener { _, event ->

            vectorDrawingView.onTouch(event)
            true
        }

        vectorDrawingView.inkEnvironmentModel = inkEnvironmentModel
        vectorDrawingView.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (tempStrokeFile != null) {
                    load(tempStrokeFile)
                    File(tempStrokeFile).delete()
                    tempStrokeFile = null
                }
                vectorDrawingView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        setTool(btn_pen, PenTool()) // Set default tool
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
        save(tempFile.absolutePath)

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
                load(data!!.data!!.path)
            } else if (requestCode == SELECT_FOLDER_ACTION) {
                openSaveAsDialog(data!!.data!!.path)
            }
        }
    }

    fun selectColor(view: View) {
        ColorPickerPopup.Builder(this)
            .initialColor(drawingColor) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(false) // Enable alpha slider or not
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
        vectorDrawingView.setColor(drawingColor)
    }

    fun selectTool(view: View) {
        when (view.id) {
            R.id.btn_pen -> setTool(view, PenTool())
            R.id.btn_felt -> setTool(view, FeltTool())
            R.id.btn_brush -> setTool(view, BrushTool())
            R.id.btn_marker -> setTool(view, MarkerTool())
            R.id.btn_eraser_partial_stroke -> setTool(view, EraserVectorTool())
            R.id.btn_eraser_whole_stroke -> setTool(view, EraserWholeStrokeTool())
            R.id.btn_selector -> setTool(view, SelectorPartialStrokeTool())
            R.id.btn_selector_whole_stroke -> setTool(view, SelectorWholeStrokeTool())
        }
    }

    fun setTool(view: View, tool: Tool) {
        drawingTool = tool
        vectorDrawingView.setTool(drawingTool as VectorTool)
        highlightTool(view)
    }

    fun highlightTool(view: View) {
        btn_pen.setActivated(false)
        btn_felt.setActivated(false)
        btn_brush.setActivated(false)
        btn_marker.setActivated(false)
        btn_eraser_partial_stroke.setActivated(false)
        btn_eraser_whole_stroke.setActivated(false)
        btn_selector.setActivated(false)
        btn_selector_whole_stroke.setActivated(false)
        view.setActivated(true)
    }

    fun load(view: View) {
        val intent = Intent(applicationContext, FileChooser::class.java)
        intent.putExtra(
            Constants.SELECTION_MODE,
            Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal
        )
        intent.putExtra(Constants.ALLOWED_FILE_EXTENSIONS, "uim")
        startActivityForResult(intent, OPEN_FILE_ACTION)
    }

    fun load(path: String?) {
        if (!refreshing) {
            Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
        }

        // Before loading we clear the screen
        vectorDrawingView.clear();

        try {
            val fileInputStream = FileInputStream(File(path))

            val bytes = fileInputStream.readBytes()
            inkModel = Will3Codec.decode(bytes)
            mainGroup = inkModel.inkTree!!.root!! as StrokeGroupNode

            val it = inkModel.inkTree!!.root!!.iterator()
            while (it.hasNext()) {
                val stroke = it.next()
                if (stroke is StrokeNode) {
                    val brushUri = stroke.data.style?.brushURI
                    if (brushUri != null) {
                        val brush = inkModel.brushRepository.getBrush(brushUri)
                        if (brush is VectorBrush) {
                            val spline = stroke.data.spline

                            val willStroke = WillStroke(spline, brush)
                            willStroke.strokeNode = stroke

                            if (stroke.data.sensorDataID != null) {
                                willStroke.sensorData =
                                    inkModel.sensorDataRepository.get(stroke.data.sensorDataID!!)
                            }

                            willStroke.strokeAttributes = object : StrokeAttributes {
                                override var size: Float = stroke.data.style?.props?.size ?: 10f
                                override var rotation: Float =
                                    stroke.data.style?.props?.rotation ?: 0.0f

                                override var scaleX: Float =
                                    stroke.data.style?.props?.scaleX ?: 1.0f
                                override var scaleY: Float =
                                    stroke.data.style?.props?.scaleY ?: 1.0f
                                override var scaleZ: Float =
                                    stroke.data.style?.props?.scaleZ ?: 1.0f

                                override var offsetX: Float =
                                    stroke.data.style?.props?.offsetX ?: 0.0f
                                override var offsetY: Float =
                                    stroke.data.style?.props?.offsetY ?: 0.0f
                                override var offsetZ: Float =
                                    stroke.data.style?.props?.offsetZ ?: 0.0f

                                override var red: Float = stroke.data.style?.props?.red ?: 0f
                                override var green: Float = stroke.data.style?.props?.green ?: 0f
                                override var blue: Float = stroke.data.style?.props?.blue ?: 0f
                                override var alpha: Float = stroke.data.style?.props?.alpha ?: 1f
                            }

                            synchronized(spatialModel) {
                                /*var sensorChannelList: List<SensorChannel>? = null
                                val inputContextId = willStroke.sensorData?.inputContextId
                                if (inputContextId != null) {
                                    val sensorContextId =
                                        inkModel.inputConfiguration.getInputContext(inputContextId)
                                            ?.sensorContextId
                                    if (sensorContextId != null) {
                                        sensorChannelList =
                                            inkModel.inputConfiguration.getSensorContext(
                                                sensorContextId
                                            )?.getSensorChannelsContexts()?.first()?.getAll()
                                    }
                                }*/

                                vectorDrawingView.buildStroke(willStroke)
                                spatialModel.add(willStroke)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!refreshing) {
            Toast.makeText(this, "Loading completed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetInkModel() {
        inkModel = InkModel()

        val root = StrokeGroupNode(IdentifiableImpl.generateUUID())

        inkModel.inkTree.root = root
        mainGroup = StrokeGroupNode(IdentifiableImpl.generateUUID())
        root.add(mainGroup)

        //TODO inkModel.brushRepository.addVectorBrush(brush)
        //TODO splines.clear()
        //TODO paths.clear()
    }


    fun save(filePath: String?) {
        if (!refreshing) {
            Toast.makeText(this, "Saving..", Toast.LENGTH_SHORT).show()
        }

        inkEnvironmentModel.registerInModel(inkModel)
        inkModel.knowledgeGraph.add(
            inkModel.inkTree!!.root!!.id,
            "created",
            "" + System.currentTimeMillis()
        )
        inkModel.knowledgeGraph.add(inkModel.inkTree!!.root!!.id, "author", "WILL 3")

        // For vector serialization
        for ((_, stroke) in spatialModel.getStrokes()) {
            stroke as WillStroke
            if (stroke.sensorData != null) {
                inkModel.sensorDataRepository.add(stroke.sensorData!!)
            }

            if (inkModel.brushRepository.getBrush(stroke.vectorBrush.name) == null) {
                inkModel.brushRepository.addVectorBrush(stroke.vectorBrush)
            }

            if (stroke.strokeNode == null) {
                stroke.strokeNode = StrokeNode(
                    IdentifiableImpl.generateUUID(),
                    Stroke(
                        stroke.id,
                        stroke.spline,
                        stroke.createStyle(),
                        stroke.sensorData?.id,
                        stroke.sensorDataOffset
                    )
                )
            }

            if (!mainGroup.contains(stroke.strokeNode!!)) {
                mainGroup.add(stroke.strokeNode!!)
            }
        }

        var fileOutputStream: OutputStream? = null

        try {
            val file = File(filePath)
            fileOutputStream = FileOutputStream(file)
            println("serializing to: " + file.absoluteFile)
            Will3Codec.encode(inkModel, fileOutputStream)
            println("serializing to: " + file.absoluteFile + " | " + file.length())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileOutputStream?.close()
        }
    }

    fun clear(view: View) {
        resetInkModel()
        vectorDrawingView.clear()
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

    fun selectPaper(pos:Int) {
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
        vectorDrawingView.setBackgroundResource(paper)
        if (popupWindow != null) {
            popupWindow!!.dismiss()
        }
    }

    fun selectFolder(view: View) {
        val intent = Intent(applicationContext, FolderChooser::class.java)
        intent.putExtra(
            Constants.SELECTION_MODE,
            Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal
        )
        startActivityForResult(intent, SELECT_FOLDER_ACTION)
    }

    fun openSaveAsDialog(folder: String?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.filenamedialog, null)
        val editName: EditText = view.findViewById(R.id.filename_edit)
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Add file name")
            setView(view)
            setPositiveButton(getString(android.R.string.ok)) { dialog, which ->
                var name = editName.text.toString()
                if (!name.endsWith(".uim")) {
                    name += ".uim"
                }
                save(folder + "/" + name)
            }
            show()
        }
    }

    fun createTool(uri: String, context: Context): Tool {
        when (uri) {
            BrushTool.uri -> return BrushTool()
            EraserVectorTool.uri -> return EraserVectorTool()
            FeltTool.uri -> return FeltTool()
            MarkerTool.uri -> return MarkerTool()
            PenTool.uri -> return PenTool()
            else -> return PenTool() //default tool
        }
    }

}
