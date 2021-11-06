package com.openresearch.pdfwriter

import com.lowagie.text.Document
import com.lowagie.text.Image
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfWriter
import java.io.Closeable
import java.io.OutputStream
import java.util.function.Consumer

/**
 * Wrapper for making creation of pdfs with OpenPDF easier. See test for a sample.
 *
 * OpenPDF is a fork of iText so a lot of iText docs could still be valid.
 */
class DocumentCreator(
    private val outputStream: OutputStream,
    defaultPageSize: Rectangle = PageSize.A4
) : Closeable {

    private val document = Document(defaultPageSize)
    private var open = false

    fun createDocument(changes: Consumer<Document>) {
        if (open) {
            throw IllegalStateException("document already open")
        }
        open = true
        PdfWriter.getInstance(document, outputStream)
        document.open()
        changes.accept(document)
    }

    override fun close() {
        if (open) {
            document.close()
            outputStream.flush()
            outputStream.close()
            open = true
        } else {
            throw IllegalStateException("document not open")
        }
    }

    companion object {

        fun continueRotated(document: Document) {
            val preSize = document.pageSize
            document.pageSize = preSize.rotate()
            document.newPage()
        }

        fun resetRotation(document: Document, defaultPageSize: Rectangle) {
            document.pageSize = defaultPageSize
        }

        fun centeredImagePositionWithin(image: Image, container: Rectangle): Array<Float> {
            val x = (container.width - image.scaledWidth) / 2
            val y = (container.height - image.scaledHeight) / 2
            return arrayOf(x, y)
        }

        fun addVerticalSpacing(document: Document, amount: Float) {
            document.add(Paragraph(amount, "\u00a0"))
        }
    }

}
