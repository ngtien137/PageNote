package com.lhd.apps.pagenote.adapter

import androidx.lifecycle.LifecycleOwner
import com.base.baselibrary.adapter.SuperAdapter
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.models.AppFont

class AdapterFont(val lifecycleOwner: LifecycleOwner) : SuperAdapter<AppFont>(R.layout.item_font) {

    override fun getDefineLifecycleOwner(): LifecycleOwner? {
        return lifecycleOwner
    }

}