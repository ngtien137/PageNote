package com.lhd.apps.pagenote.adapter

import com.base.baselibrary.adapter.listener.ListItemListener
import com.lhd.apps.pagenote.models.AppFont

interface ListenerAdapterFont : ListItemListener {

    fun onFontClick(appFont: AppFont, itemPosition: Int)

}