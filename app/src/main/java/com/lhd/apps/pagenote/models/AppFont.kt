package com.lhd.apps.pagenote.models

import android.graphics.Typeface

class AppFont(var id: Int, var name: String, var typeface: Typeface) {

    var fontPath = ""

    override fun toString(): String {
        return "AppFont(id=$id, name='$name', typeface=$typeface)"
    }
}