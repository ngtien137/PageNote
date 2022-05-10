package com.lhd.apps.pagenote.utils

import com.base.baselibrary.utils.getApplication
import java.io.File

object FileUtils {

    val ROOT_FOLDER = getApplication().filesDir

    fun getPdfFolder(): String {
        val pathFolder = "$ROOT_FOLDER/PDF"
        val folder = File(pathFolder)
        if (folder.exists()) {
            folder.mkdirs()
        }
        return pathFolder
    }

}