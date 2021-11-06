package com.openresearch.pdfwriter.fromhtml

import com.lowagie.text.Image
import org.apache.commons.io.IOUtils
import org.w3c.dom.Element
import org.xhtmlrenderer.extend.FSImage
import org.xhtmlrenderer.extend.ReplacedElement
import org.xhtmlrenderer.extend.ReplacedElementFactory
import org.xhtmlrenderer.extend.UserAgentCallback
import org.xhtmlrenderer.layout.LayoutContext
import org.xhtmlrenderer.pdf.ITextFSImage
import org.xhtmlrenderer.pdf.ITextImageElement
import org.xhtmlrenderer.render.BlockBox
import org.xhtmlrenderer.simple.extend.FormSubmissionListener
import java.io.FileInputStream
import java.io.InputStream

open class CustomElementFactoryImpl constructor(
    private val baseDir: String,
    private val defaultImageWith: Int = 2000,
    private val defaultImageHeight: Int = 1000,
) : ReplacedElementFactory {

    override fun createReplacedElement(
        c: LayoutContext?,
        box: BlockBox?,
        uac: UserAgentCallback?,
        cssWidth: Int,
        cssHeight: Int
    ): ReplacedElement? {
        val e = box!!.element
        when (e.nodeName) {
            "img" -> {
                val imagePath = e.getAttribute("src")
                try {
                    val input: InputStream = FileInputStream("$baseDir/$imagePath")
                    val bytes: ByteArray = IOUtils.toByteArray(input)
                    val image: Image = Image.getInstance(bytes)
                    val fsImage: FSImage = ITextFSImage(image)
                    if (cssWidth != -1 || cssHeight != -1) {
                        fsImage.scale(cssWidth, cssHeight)
                    } else {
                        fsImage.scale(defaultImageWith, defaultImageHeight)
                    }
                    return ITextImageElement(fsImage)
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
            }
        }
        return null
    }

    override fun reset() {
        // do nothing
    }

    override fun remove(e: Element?) {
        // do nothing
    }

    override fun setFormSubmissionListener(listener: FormSubmissionListener?) {
        // do nothing
    }
}
