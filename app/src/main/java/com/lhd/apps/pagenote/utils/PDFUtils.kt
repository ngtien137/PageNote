package com.lhd.apps.pagenote.utils

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.view.View

object PDFUtils {

    fun drawBitmapToPage(
        document: PdfDocument,
        bitmap: Bitmap,
        view: View,
        pageNumber: Int
    ) {
        val pageInfo =
            PdfDocument.PageInfo.Builder(view.width, view.height, pageNumber)
                .create()
        val page = document.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, Paint(Paint.ANTI_ALIAS_FLAG))
        document.finishPage(page)
    }

}