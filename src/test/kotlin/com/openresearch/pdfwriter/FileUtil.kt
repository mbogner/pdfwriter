package com.openresearch.pdfwriter

import java.net.URL

class FileUtil private constructor() {
    companion object {
        fun getClasspathPath(classpathResourcePath: String): URL {
            return {}.javaClass.getResource(classpathResourcePath)!!
        }
    }
}
