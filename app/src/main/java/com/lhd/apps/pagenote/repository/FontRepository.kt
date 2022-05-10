package com.lhd.apps.pagenote.repository

import android.graphics.Typeface
import com.base.baselibrary.utils.getApplication
import com.base.baselibrary.views.ext.loge
import com.lhd.apps.pagenote.models.AppFont
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FontRepository @Inject constructor() {

    fun getListFont(): ArrayList<AppFont> {
        val result = ArrayList<AppFont>()
        val assetManager = getApplication().assets
        val listFiles = assetManager.list("fonts")
        listFiles?.forEach { fontNameWithExtension ->
            val fontPath = "fonts/${fontNameWithExtension}"
            val fontFile = File(fontPath)
            val typeface = Typeface.createFromAsset(assetManager, fontPath)
            result.add(AppFont(result.size, fontFile.nameWithoutExtension, typeface).apply {
                this.fontPath = fontPath
            })
        }
        return result

    }

}