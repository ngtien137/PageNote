package com.lhd.apps.pagenote.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.baselibrary.utils.getAppTypeFace
import com.base.baselibrary.viewmodel.Event
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.models.AppFont
import com.lhd.apps.pagenote.models.InputItem
import com.lhd.apps.pagenote.repository.FontRepository
import com.lhd.apps.pagenote.repository.INoteRepository
import com.lhd.apps.pagenote.repository.NoteRepository
import com.lhd.apps.pagenote.utils.AppConst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InputNoteViewModel @Inject constructor(
    private val fontRepository: FontRepository,
    private val noteRepository: NoteRepository
) :
    ViewModel(), INoteRepository {

    val liveInputTexts by lazy {
        MutableLiveData(mutableListOf<InputItem>().apply {
        })
    }

    val liveFocusPosition by lazy {
        MutableLiveData(-1)
    }

    val liveMenuState by lazy {
        MutableLiveData(AppConst.STATE_CLOSE)
    }

    val liveListFont by lazy {
        MutableLiveData(ArrayList<AppFont>())
    }

    val liveFont by lazy {
        MutableLiveData(getAppTypeFace(R.font.poppins))
    }

    val liveFontSize by lazy {
        MutableLiveData(AppConst.DEFAULT_FONT_SIZE)
    }

    val liveModeFont by lazy {
        MutableLiveData(AppConst.STATE_CLOSE)
    }

    val liveModeFontSize by lazy {
        MutableLiveData(AppConst.STATE_CLOSE)
    }

    val eventLoading by lazy {
        MutableLiveData(Event())
    }

    fun getListInputText() = liveInputTexts.value ?: arrayListOf()

    fun addNewInputNote(onAdd: () -> Unit) {
        liveInputTexts.value?.let {
            it.add(InputItem())
            onAdd.invoke()
        }
    }

    fun changeStateMenu() {
        liveMenuState.value =
            if (liveMenuState.value == AppConst.STATE_OPEN) AppConst.STATE_CLOSE else AppConst.STATE_OPEN
    }

    fun openFontMenu(state: Int = AppConst.STATE_OPEN) {
        liveModeFont.value = state
    }

    fun openFontSizeMenu(state: Int = AppConst.STATE_OPEN) {
        liveModeFontSize.value = state
    }

    fun isFontMenuOpen() = liveModeFont.value == AppConst.STATE_OPEN
    fun isFontSizeMenuOpen() = liveModeFontSize.value == AppConst.STATE_OPEN

    fun getListFont() {
        if (liveListFont.value.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val list = fontRepository.getListFont()
                withContext(Dispatchers.Main) {
                    liveListFont.value = list
                }
            }
        }
    }

    fun changeFontSizeByMultiple(multipleValue: Float) {
        liveFontSize.value = AppConst.DEFAULT_FONT_SIZE * multipleValue
    }

    override fun getNoteRepository(): NoteRepository {
        return noteRepository
    }

}