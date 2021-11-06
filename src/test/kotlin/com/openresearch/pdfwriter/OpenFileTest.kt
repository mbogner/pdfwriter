package com.openresearch.pdfwriter

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.Locale

abstract class OpenFileTest(
    private val openCreated: Boolean = false
) {

    companion object {
        private val operatingSystem = System.getProperty("os.name").lowercase(Locale.getDefault())
        val isMac = operatingSystem.contains("mac")
    }

    lateinit var file: File

    @BeforeEach
    open fun setUp() {
        file = File.createTempFile("test-", ".pdf")
    }

    @AfterEach
    open fun tearDown() {
        println("created file ${file.absoluteFile}")
        if (openCreated && isMac) {
            Runtime.getRuntime().exec("open ${file.absoluteFile}")
        }
    }

}
