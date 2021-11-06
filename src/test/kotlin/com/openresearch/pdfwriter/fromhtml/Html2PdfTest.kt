package com.openresearch.pdfwriter.fromhtml

import com.openresearch.pdfwriter.FileUtil
import com.openresearch.pdfwriter.OpenFileTest
import org.junit.jupiter.api.Test
import java.io.FileOutputStream

internal class Html2PdfTest : OpenFileTest() {

    @Test
    fun render() {
        Html2Pdf.render(FileUtil.getClasspathPath("/sample.html"), FileOutputStream(file))
    }

}
