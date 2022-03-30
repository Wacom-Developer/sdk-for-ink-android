package com.wacom.will3.ink.vector.rendering.demo.export

import com.wacom.ink.DIPolygon
import com.wacom.ink.pipeline.*
import com.wacom.will3.ink.vector.rendering.demo.createVectorTool
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.OutputStream
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class SVGExporter {

    internal var splineInterpolator : SplineInterpolator = CurvatureBasedInterpolator(false, false)
    internal lateinit var brushApplier: BrushApplier
    internal var convexHullChainProducer = ConvexHullChainProducer()
    internal var polygonMerger = PolygonMerger()

    private var minX = Float.MAX_VALUE
    private var minY = Float.MAX_VALUE
    private var maxX = 0.0f
    private var maxY = 0.0f

    fun exportToSVG(out: OutputStream, strokes: List<WillStroke>, svgWidth: Float, svgHeight: Float, fit: Boolean) {
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document: Document = docBuilder.newDocument()

        val svg = document.createElementNS("http://www.w3.org/2000/svg", "svg")
        document.appendChild(svg)
        svg.setAttribute("viewBox", "0 0 ".plus(svgWidth).plus(" ").plus(svgHeight))

        // first of all we need to get the PostScript drawing commands from the stroke list
        drawStrokes(document, svg, strokes, svgWidth, svgHeight, fit)

        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()

        // ==== Start: Pretty print
        // https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        // ==== End: Pretty print

        transformer.transform(DOMSource(document), StreamResult(out))
    }

    fun drawStrokes(document: Document, svg: Element, strokes: List<WillStroke>, svgWidth: Float, svgHeight: Float, fit: Boolean) {
        val inkGroup = document.createElementNS("http://www.w3.org/2000/svg", "g")
        svg.appendChild(inkGroup)

        for (stroke in strokes) {
            drawStroke(document, inkGroup, stroke)
        }

        if (fit) {
            // if fit we put a transformation matrix scaling the strokes
            val scaleX = svgWidth / maxX
            val scaleY = svgHeight / maxY
            val scale = Math.min(scaleX, scaleY)
            inkGroup.setAttribute("transform", "matrix(".plus(scale).plus(",0,0,").plus(scale).plus(",0,0)"))
        }
    }

    private fun drawStroke(document: Document, inkGroup: Element, stroke: WillStroke) {
        brushApplier = BrushApplier(stroke.createStyle().createVectorTool().brush, true)

        val (addedPoints, predictedPoints) = splineInterpolator.add(
            true,
            true,
            stroke.spline,
            null
        )
        val (addedPolys, predictedPolys) = brushApplier.add(
            true,
            true,
            addedPoints,
            predictedPoints
        )
        val (addedHulls, predictedHulls) = convexHullChainProducer.add(
            true,
            true,
            addedPolys,
            predictedPolys
        )
        val (addedMerged, predictedMerged) = polygonMerger.add(
            true,
            true,
            addedHulls,
            predictedHulls
        )

        if (addedMerged != null) {

            val inkPath = document.createElementNS("http://www.w3.org/2000/svg", "path")
            inkPath.setAttribute("fill", String.format("#%02x%02x%02x",
                (stroke.strokeAttributes.red*255).roundToInt(),
                (stroke.strokeAttributes.green*255).roundToInt(),
                (stroke.strokeAttributes.blue*255).roundToInt()))
            inkPath.setAttribute("fill-opacity", stroke.strokeAttributes.alpha.toString());
            inkPath.setAttribute("d", drawPolygon(addedMerged));
            inkGroup.appendChild(inkPath);
        }

    }

    /**
     * Generate Bezier Path.
     *
     * @param polygons - input polygons
     * @return path
     */
    private fun drawPolygon(polygon: ArrayList<DIPolygon>): String {
        if (polygon.size <= 0) {
            return ""
        }

        var path = StringBuilder()
        for (poly in polygon) {
            for (j in 0 until poly.size step 2) {
                if (j == 0) {
                    path.append(" M ").append(poly[j]).append(" ").append(poly[j + 1])
                } else {
                    path.append(" L ").append(poly[j]).append(" ").append(poly[j + 1])
                }

                if (poly[j] > maxX) {
                    maxX = poly[j]
                }
                if (poly[j] < minX) {
                    minX = poly[j]
                }
                if (poly[j + 1] > maxY) {
                    maxY = poly[j + 1]
                }
                if (poly[j + 1] < minY) {
                    minY = poly[j + 1]
                }
            }
        }

        return path.toString()
    }

}