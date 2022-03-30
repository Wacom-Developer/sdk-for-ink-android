package com.wacom.will3.ink.vector.rendering.demo.export

import android.graphics.Path
import com.wacom.ink.DIPolygon
import com.wacom.ink.PathSegment
import com.wacom.ink.pipeline.*
import com.wacom.ink.pipeline.base.ProcessorResult
import com.wacom.ink.protobuf.Will3_0_0
import com.wacom.will3.ink.vector.rendering.demo.createVectorTool
import com.wacom.will3.ink.vector.rendering.demo.vector.PolygonToBezierPathProducer
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PDFExporter {

    internal var splineInterpolator : SplineInterpolator = CurvatureBasedInterpolator(false, false)
    internal lateinit var brushApplier: BrushApplier
    internal var convexHullChainProducer = ConvexHullChainProducer()
    internal var polygonMerger = PolygonMerger()

    private var psCommands = StringBuilder() // PostScript commands for drawing the inking
    private var graphicStates = arrayListOf<Float>(); // Store the alphas

    private var minX = Float.MAX_VALUE
    private var minY = Float.MAX_VALUE
    private var maxX = 0.0f
    private var maxY = 0.0f

    companion object {
        const val PDF_A4_WIDTH = 595.0f
        const val PDF_A4_HEIGHT = 842.0f

        private const val PDF_TEMPLATE = "%PDF-1.4\n" +
                "%âãÏÓ\n" +
                "1 0 obj\n" +
                "<</Type/Page/Parent 3 0 R/Contents 2 0 R/MediaBox[0 0 $1$ $2$]/Resources<</ProcSet[/PDF]/ExtGState<<$3$>>>>>>\n" +
                "endobj\n" +
                "2 0 obj\n" +
                "<</Length $4$>>stream\n" +
                "$5$\n" +
                "endstream\n" +
                "endobj\n" +
                "3 0 obj\n" +
                "<</Type/Pages/Count 1/Kids[1 0 R]>>\n" +
                "endobj\n" +
                "4 0 obj\n" +
                "<</Type/Catalog/Pages 3 0 R>>\n" +
                "endobj\n" +
                "5 0 obj\n" +
                "<</Producer($6$)/CreationDate(D:$7$)/ModDate(D:$8$)>>\n" +
                "endobj\n" +
                "xref\n" +
                "0 6\n" +
                "0000000000 65535 f\n" +
                "$9$ 00000 n\n" +
                "$10$ 00000 n\n" +
                "$11$ 00000 n\n" +
                "$12$ 00000 n\n" +
                "$13$ 00000 n\n" +
                "trailer\n" +
                "<</Size 6/Root 4 0 R/Info 5 0 R/ID[<$14$><$15$>]>>\n" +
                "startxref\n" +
                "$16$\n" +
                "%%EOF"
    }

    fun exportToPDF(strokes: List<WillStroke>, pdfWidth: Float, pdfHeight: Float, fit: Boolean):String {
        // first of all we need to get the PostScript drawing commands from the stroke list
        drawStrokes(strokes, pdfWidth, pdfHeight, fit)

        val commands = psCommands.toString()

        val date = SimpleDateFormat("yyyyMMddHHmmss").format(Date()).plus("Z")

        var pdf = PDF_TEMPLATE.replace("$1$", pdfWidth.toString())
                              .replace("$2$", pdfHeight.toString())
                              .replace("$3$", getGSStates())
                              .replace("$4$", commands.length.toString())
                              .replace("$5$", commands)
                              .replace("$6$", "Wacom")
                              .replace("$7$", date)
                              .replace("$8$", date);

        pdf = pdf.replace("$9$", this.fill(pdf.indexOf("1 0 obj")))
                 .replace("$10$", this.fill(pdf.indexOf("2 0 obj")))
                 .replace("$11$", this.fill(pdf.indexOf("3 0 obj")))
                 .replace("$12$", this.fill(pdf.indexOf("4 0 obj")))
                 .replace("$13$", this.fill(pdf.indexOf("5 0 obj")))
                 .replace("$14$", UUID.randomUUID().toString().replace("-", ""))
                 .replace("$15$", UUID.randomUUID().toString().replace("-", ""));

        pdf = pdf.replace("$16$", pdf.indexOf("xref").toString());

        return pdf;
    }

    fun getGSStates():String {
        var gsStates = StringBuilder()
        graphicStates.forEachIndexed { index, alpha ->
                gsStates.append("/GS").append(index+1).append("<</ca ").append(alpha).append(">>")
        }

        return gsStates.toString()
    }

    fun fill(offset: Int):String {
        var str = ("0000000000".plus(offset))
        return str.substring(str.length-10)
    }

    fun drawStrokes(strokes: List<WillStroke>, pdfWidth: Float, pdfHeight: Float, fit: Boolean) {
        for (stroke in strokes) {
            drawStroke(stroke)
        }

        if (fit) {
            // if fit we put a transformation matrix scaling the strokes
            val scaleX = pdfWidth / maxX
            val scaleY = pdfHeight / maxY
            val scale = Math.min(scaleX, scaleY)
            val matrix = scale.toString().plus(" 0 0 ").plus(-scale).plus(" 0 ").plus(pdfHeight)
                .plus(" cm\n")
            psCommands.insert(0, matrix)
        } else {
            // Strokes have Y coordinates from top = 0 to bottom = maxHeight,
            // while PDF have Y coordinates from bottom = 0 to top = maxHeight,
            // so we need to flip the Y coordinates. We do it with the following transformation matrix.
            val matrix = "1 0 0 -1 0 ".plus(pdfHeight).plus(" cm\n")
            psCommands.insert(0, matrix)
        }
    }

    private fun drawStroke(stroke: WillStroke) {
        psCommands.append("q\n") //save the graphics state
        val alpha = stroke.strokeAttributes.alpha
        if (!graphicStates.contains(alpha)) {
            graphicStates.add(alpha)
        }

        psCommands.append("/GS").append(graphicStates.indexOf(alpha)+1).append(" gs\n") //put the alpha state
        psCommands.append(stroke.strokeAttributes.red).append(" ").append(stroke.strokeAttributes.green).append(" ").append(stroke.strokeAttributes.blue).append(" rg\n") //put the stroke color

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
            drawPolygon(addedMerged)
        }

        psCommands.append("Q\n") // restore the graphics state
    }

    /**
     * Generate Bezier Path.
     *
     * @param polygons - input polygons
     * @return path
     */
    private fun drawPolygon(polygon: ArrayList<DIPolygon>) {
        if (polygon.size <= 0) {
            return
        }

        for (poly in polygon) {
            for (j in 0 until poly.size step 2) {
                if (j == 0) {
                    psCommands.append(poly[j]).append(" ").append(poly[j + 1]).append(" m ")
                } else {
                    psCommands.append(poly[j]).append(" ").append(poly[j + 1]).append(" l ")
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
            //psCommands.append(" h ")
        }
        psCommands.append("f ")
    }

}