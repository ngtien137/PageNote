package com.lhd.apps.pagenote.view.dialog

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.base.baselibrary.dialog.BaseBindingFragmentDialog
import com.base.baselibrary.utils.getAppString
import com.base.baselibrary.utils.vibrateDevice
import com.base.baselibrary.views.custom.SimpleTextWatch
import com.base.baselibrary.views.ext.showKeyBoard
import com.lhd.apps.pagenote.MainActivity
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.databinding.DialogInputTextBinding
import java.io.File

class DialogInputText(
    val rootActivity: MainActivity,
    val builder: Builder,
    val isApplyCurrentName: Boolean = false,
    val onSave: (path: String) -> Unit
) :
    BaseBindingFragmentDialog<DialogInputTextBinding>(), View.OnClickListener {

    var outputNameIsFile: Boolean = true
    val currentFile = File(builder.currentPath)
    val regex = Regex("[A-Za-z0-9()_ ]+")

    //Thêm điều kiện cho nút confirm
    var listExtraDialogCondition = ArrayList<DialogCondition>()

    override fun initBinding() {
        binding.viewListener = this
        binding.builder = builder
    }

    override fun setUp(view: View?) {
        binding.edtName.post {
            binding.edtName.setText(currentFile.nameWithoutExtension)
            binding.edtName.setSelection(binding.edtName.text.length)
            binding.edtName.requestFocus()
            binding.edtName.selectAll()
            binding.edtName.showKeyBoard()
        }
        binding.edtName.addTextChangedListener(object : SimpleTextWatch {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val name = binding.edtName.text.trim().toString()
                val parentPath = builder.destinationFolder
                val extension = currentFile.extension
                val oldName = currentFile.nameWithoutExtension
                val newFile = File("$parentPath/$name.$extension")
                var isNameValid = false
                isNameValid = regex.matches(name)
                if (name.isEmpty()) {
                    binding.tvError.text = ""
                } else if (!isNameValid) {
                    binding.tvError.setText(R.string.name_is_special)
                } else if ((newFile.exists() && outputNameIsFile) || (oldName.equals(
                        name,
                        true
                    ))
                ) {
                    //binding.tvError.setText(R.string.file_is_exist)
                    binding.tvError.text = ""
                } else {
                    binding.tvError.text = ""
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_input_text
    }

    override fun getRootViewGroup(): ViewGroup {
        return binding.rootDialog
    }

    fun getCurrentInputText() = (binding.edtName.text?.trim() ?: "").toString()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnNegative -> {
                dismiss()
            }
            R.id.btnPositive -> {
                val name = binding.edtName.text.trim().toString()
                val parentPath = builder.destinationFolder
                val extension = currentFile.extension
                val oldName = currentFile.nameWithoutExtension
                val newFile = File("$parentPath/$name.$extension")
                var isNameValid = false
                isNameValid = regex.matches(name)
                if (name.isEmpty()) {
                    (binding.root.context as MainActivity).vibrateDevice()
                    binding.tvError.setText(R.string.name_is_empty)
                } else if (!isNameValid) {
                    (binding.root.context as MainActivity).vibrateDevice()
                    binding.tvError.setText(R.string.name_is_special)
                } else if ((newFile.exists() && outputNameIsFile) || (oldName.equals(
                        name,
                        true
                    ) && !isApplyCurrentName)
                ) {
                    rootActivity.vibrateDevice()
                    binding.tvError.setText(R.string.file_is_exist)
                } else {
                    if (listExtraDialogCondition.isNotEmpty()) {
                        for (condition in listExtraDialogCondition) {
                            if (!condition.condition.invoke()) {
                                (binding.root.context as MainActivity).vibrateDevice()
                                binding.tvError.setText(condition.textShowIfNotPass)
                                return
                            }
                        }
                    }
                    if (outputNameIsFile)
                        onSave(newFile.absolutePath)
                    else
                        onSave(name)
                    dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //if (binding.edtName.isFocused)
        binding.edtName.postDelayed({
            binding.edtName.requestFocus()
            binding.edtName.showKeyBoard()
        }, 100)
    }

    class Builder(
        var currentPath: String,
        var destinationFolder: String,
        var textTitle: String,
        @DrawableRes
        var drawableIcon: Int = R.drawable.ic_save
    ) {
        constructor(
            currentPath: String,
            destinationFolder: String,
            @StringRes
            textTitle: Int,
            @DrawableRes
            drawableIcon: Int = R.drawable.ic_save
        ) : this(
            currentPath,
            destinationFolder,
            getAppString(textTitle), drawableIcon
        )

        fun getCurrentPathNameWithoutExtension() = File(currentPath).nameWithoutExtension
    }

    class DialogCondition(var condition: () -> Boolean, @StringRes var textShowIfNotPass: Int)
}