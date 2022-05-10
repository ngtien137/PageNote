package com.lhd.apps.pagenote.view.dialog

import android.view.View
import android.view.ViewGroup
import com.base.baselibrary.dialog.BaseBindingFragmentDialog
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.databinding.DialogSaveBinding

class DialogSave(val listener: DialogSaveListener) : BaseBindingFragmentDialog<DialogSaveBinding>(),
    View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.dialog_save
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.rootDialog
    }

    override fun initBinding() {
        binding.viewListener = this
    }

    override fun setUp(view: View?) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSaveAsNote -> {
                listener.onClickSaveAsNote()
            }
            R.id.btnSaveAsPdf -> {
                listener.onClickSaveAsPdf()
            }
        }
    }

    interface DialogSaveListener {
        fun onClickSaveAsNote()
        fun onClickSaveAsPdf()
    }
}