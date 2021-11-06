package com.openresearch.pdfwriter.fromhtml

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.File
import java.io.OutputStream
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Provides functionality to render a html file to pdf. See test for a sample. For this I wouldn't see any option to add
 * custom page breaks or single landscape pages.
 */
class Html2Pdf {

    companion object {

        /**
         * Renders a html file from given path to outputStream. All files have to be relative to the html file and
         * styles have to be included in the file. For including images in the html file make sure the width and height
         * is set on the image tage.
         * @param path The URL to the html file on your disk.
         * @param outputStream Target of the pdf. Can be FileOutputStream or ByteArrayOutputStream for example.
         * @param defaultImageWith Render width of an image if width attribute is missing. Default = 2000.
         * @param defaultImageHeight Render height of an image if height attribute is missing. Default = 1000.
         */
        fun render(
            path: URL,
            outputStream: OutputStream,
            defaultImageWith: Int = 2000,
            defaultImageHeight: Int = 1000
        ) {
            val htmlFile = File(path.toURI())
            val htmlDir = htmlFile.parentFile
            val xhtml = parseHtmlFile(htmlFile)

            outputStream.use { os ->
                val renderer = ITextRenderer()
                val sharedContext = renderer.sharedContext
                sharedContext.isPrint = true
                sharedContext.isInteractive = false
                sharedContext.replacedElementFactory = CustomElementFactoryImpl(
                    htmlDir.absolutePath, defaultImageWith, defaultImageHeight
                )
                renderer.setDocumentFromString(xhtml.html(), htmlDir.absolutePath)
                renderer.layout()
                renderer.createPDF(os)
            }
        }

        fun parseHtmlFile(file: File, encoding: String = StandardCharsets.UTF_8.name()): Document {
            val xhtml = Jsoup.parse(file, "UTF-8")
            xhtml.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
            return xhtml
        }
    }

}
