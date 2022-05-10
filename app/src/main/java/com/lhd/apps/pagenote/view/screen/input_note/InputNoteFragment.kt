package com.lhd.apps.pagenote.view.screen.input_note

import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import androidx.core.view.drawToBitmap
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.base.baselibrary.utils.getAppTypeFace
import com.base.baselibrary.utils.observer
import com.base.baselibrary.views.ext.toast
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.adapter.AdapterFont
import com.lhd.apps.pagenote.adapter.ListenerAdapterFont
import com.lhd.apps.pagenote.databinding.FragmentInputNoteBinding
import com.lhd.apps.pagenote.databinding.LayoutEditTextBinding
import com.lhd.apps.pagenote.models.AppFont
import com.lhd.apps.pagenote.utils.AppConst
import com.lhd.apps.pagenote.utils.FileUtils
import com.lhd.apps.pagenote.utils.PDFUtils
import com.lhd.apps.pagenote.view.BaseMainFragment
import com.lhd.apps.pagenote.view.custom.LimitedEditText
import com.lhd.apps.pagenote.view.dialog.DialogSave
import com.lhd.apps.pagenote.viewmodel.InputNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileOutputStream

@AndroidEntryPoint
class InputNoteFragment : BaseMainFragment<FragmentInputNoteBinding>(), ListenerAdapterFont,
    DialogSave.DialogSaveListener {

    private val viewModel by viewModels<InputNoteViewModel>()

    private val listBinding by lazy {
        hashMapOf<Int, LayoutEditTextBinding>()
    }

    private val adapterFont by lazy {
        AdapterFont(rootActivity).also {
            it.listener = this
        }
    }

    private val dialogSave by lazy {
        DialogSave(this)
    }

    override fun isSetCustomColorStatusBar(): Boolean {
        return true
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }

    override fun isDarkText(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_input_note
    }

    override fun initBinding() {
        binding.viewModel = viewModel
        binding.adapterFont = adapterFont
    }

    override fun initView() {
        val layoutInput = binding.llLayoutInput
        if (layoutInput.isEmpty()) {
            addNewInput()
        }
        binding.slFontSize.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                viewModel.changeFontSizeByMultiple(value)
            }
        }
        observer(viewModel.liveListFont) {
            adapterFont.data = it
        }
    }

    private fun addNewInput() {
        val layoutInput = binding.llLayoutInput
        viewModel.addNewInputNote { }
        val itemIndex = layoutInput.childCount
        val bindingInput = if (listBinding[itemIndex] == null) {
            DataBindingUtil.inflate<LayoutEditTextBinding>(
                layoutInflater,
                R.layout.layout_edit_text,
                null,
                false
            ).also {
                listBinding[itemIndex] = it
            }
        } else {
            listBinding[itemIndex]!!
        }
        bindingInput.liveText = viewModel.getListInputText()[itemIndex].liveText
        bindingInput.liveFont = viewModel.liveFont
        bindingInput.liveFontSize = viewModel.liveFontSize
        bindingInput.lifecycleOwner = viewLifecycleOwner
        val edt = bindingInput.edtItem
        edt.onClickDown = {
            viewModel.liveFocusPosition.value = itemIndex
        }
        edt.onLimitWithLastLineData = { overflowText, currentDataCursor ->
            val currentCursorLineIndex = currentDataCursor.currentCursorLineIndex
            val currentCursorIndex = currentDataCursor.currentCursorIndex
            val isOverflowAtEnd = currentCursorLineIndex == edt.limitedLine
            if (isOverflowAtEnd) {
                //Đang nhập tại cuối trang thì hết trang, nhảy sang trang mới
                //Cập nhật vị trí hiện tại nếu nhảy sang trang mới
                viewModel.liveFocusPosition.value = viewModel.liveFocusPosition.value!! + 1
            }
            if (itemIndex == viewModel.getListInputText().size - 1) {
                viewModel.getListInputText().let { list ->
                    addNewInput()
                    applyOverflowText(
                        edt,
                        itemIndex,
                        overflowText,
                        isOverflowAtEnd,
                        currentDataCursor
                    )
                }
            } else {
                applyOverflowText(edt, itemIndex, overflowText, isOverflowAtEnd, currentDataCursor)
            }
        }
        if (viewModel.liveFocusPosition.value == itemIndex) {
            edt.requestFocus()
        }
        layoutInput.addView(bindingInput.root)
    }

    private fun applyOverflowText(
        currentEdt: LimitedEditText,
        itemInputLayoutIndex: Int,
        overflowText: String,
        isOverflowAtEnd: Boolean,
        currentDataCursor: LimitedEditText.DataCursor
    ) {
        listBinding[itemInputLayoutIndex + 1]?.edtItem?.let { nextEdt ->
            if (isOverflowAtEnd) {
                //Đang nhập tại cuối trang thì hết trang, nhảy sang trang mới
                nextEdt.requestFocus()
            } else {
                if (itemInputLayoutIndex == viewModel.liveFocusPosition.value) {
                    //Khi gọi hàm setText, editText sẽ bị mất focus, cần gọi lại focus cho editText đang edit
                    currentEdt.requestFocus()
                    currentEdt.setSelection(currentDataCursor.currentCursorIndex)
                }
            }
            val appendTextForNextEditText = overflowText + nextEdt.text.toString()
            nextEdt.setText(appendTextForNextEditText)
        }
    }

    override fun onBackPressed() {
        when {
            viewModel.isFontMenuOpen() -> {
                viewModel.openFontMenu(AppConst.STATE_CLOSE)
            }
            viewModel.isFontSizeMenuOpen() -> {
                viewModel.openFontSizeMenu(AppConst.STATE_CLOSE)
            }
            else -> super.onBackPressed()
        }
    }

    override fun onViewClick(vId: Int) {
        when (vId) {
            R.id.btnClose -> {
                onBackPressed()
            }
            R.id.btnMenu -> {
                viewModel.changeStateMenu()
            }
            R.id.btnSave -> {

            }
            R.id.miFont -> {
                viewModel.getListFont()
                viewModel.openFontMenu()
            }
            R.id.btnCloseMenuFont -> {
                viewModel.openFontMenu(AppConst.STATE_CLOSE)
            }
            R.id.miFontSize -> {
                viewModel.openFontSizeMenu()
            }
            R.id.btnCloseMenuFontSize -> {
                viewModel.openFontSizeMenu(AppConst.STATE_CLOSE)
            }
        }
    }

    //region font listener

    override fun onFontClick(appFont: AppFont, itemPosition: Int) {
        viewModel.liveFont.value = appFont.typeface
    }

    //endregion

    //region save action

    override fun onClickSaveAsNote() {

    }

    override fun onClickSaveAsPdf() {
        rootActivity.grantPermission(rootActivity.listPermissionWriteRead) {
            val layoutInput = binding.llLayoutInput
            val size = layoutInput.childCount
            val document = PdfDocument()
            for (index in 0 until size) {
                val edt = listBinding[index]!!.edtItem
                val isCursorVisible = edt.isCursorVisible
                edt.isCursorVisible = false
                val bitmap = edt.drawToBitmap()
                edt.isCursorVisible = isCursorVisible
                PDFUtils.drawBitmapToPage(document, bitmap, edt, index + 1)
                val outputStream =
                    FileOutputStream("${FileUtils.getPdfFolder()}/test.pdf")
                document.writeTo(outputStream)
                toast("Done")
            }
        }
    }

    //endregion

}