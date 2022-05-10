package com.lhd.apps.pagenote.models

import androidx.lifecycle.MutableLiveData

data class InputItem(var liveText: MutableLiveData<String> = MutableLiveData("")) {

    fun setText(text: String) {
        liveText.value = text
    }

}