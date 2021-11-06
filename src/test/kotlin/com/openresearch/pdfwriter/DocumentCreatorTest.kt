package com.openresearch.pdfwriter

import com.lowagie.text.Chunk
import com.lowagie.text.Element
import com.lowagie.text.FontFactory
import com.lowagie.text.Image
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.Phrase
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.draw.LineSeparator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import java.io.FileOutputStream

internal class DocumentCreatorTest : OpenFileTest(true) {

    private lateinit var documentCreator: DocumentCreator

    @BeforeEach
    override fun setUp() {
        super.setUp()
        documentCreator = DocumentCreator(FileOutputStream(file))
    }

    @Test
    fun createSampleDocument() {
        documentCreator.use { closable ->
            closable.createDocument { document ->
                val defaultPageSize = document.pageSize
                val p1 = Paragraph()
                p1.add(Chunk("chunk1\n", FontFactory.getFont(FontFactory.HELVETICA, 6f)))
                p1.add(Chunk("chunk2\n"))
                p1.add(Chunk("chunk3\n"))
                p1.add(Phrase("phrase1\n"))
                p1.add(Phrase("phrase2\n", FontFactory.getFont(FontFactory.HELVETICA, 6f)))
                p1.add(Phrase("phrase3"))
                document.add(p1)

                // add some extra paragraphs to test paging
                for (i in 1..50) {
                    document.add(Paragraph("loop$i\n"))
                }

                // add image on landscape page
                DocumentCreator.continueRotated(document)
                val image = Image.getInstance(FileUtil.getClasspathPath("/cat.jpg"))
                image.scalePercent(50f)
                val (x, y) = DocumentCreator.centeredImagePositionWithin(image, PageSize.A4.rotate())
                image.setAbsolutePosition(x, y)
                document.add(image)

                document.newPage()
                // add some extra paragraphs to test paging
                for (i in 1..50) {
                    document.add(Paragraph("loop$i\n"))
                }
                DocumentCreator.resetRotation(document, defaultPageSize)

                // add table on new page
                document.newPage()
                DocumentCreator.addVerticalSpacing(document, 50f)
                document.add(LineSeparator(2f, 100f, Color.BLACK, Element.ALIGN_CENTER, 0f))
                DocumentCreator.addVerticalSpacing(document, 50f)
                val table = PdfPTable(floatArrayOf(1f, 2f, 2f, 2f))
                table.widthPercentage = 100f
                for (i in 1..50) {
                    arrayOf("$i", "a$i", "b$i", "c$i").forEach {
                        val cell = PdfPCell(Phrase(it))
                        cell.paddingLeft = 5f
                        cell.paddingRight = cell.paddingLeft
                        cell.paddingTop = 5f
                        cell.paddingBottom = cell.paddingTop
                        cell.horizontalAlignment = Element.ALIGN_LEFT
                        cell.verticalAlignment = Element.ALIGN_BOTTOM
                        table.addCell(cell)
                    }
                }
                document.add(table)
                DocumentCreator.addVerticalSpacing(document, 50f)
                document.add(LineSeparator(2f, 100f, Color.BLACK, Element.ALIGN_CENTER, 0f))
            }
        }
    }

}
