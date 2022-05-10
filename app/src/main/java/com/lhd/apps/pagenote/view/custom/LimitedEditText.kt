package com.lhd.apps.pagenote.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.base.baselibrary.views.ext.loge
import kotlin.math.roundToInt


@SuppressLint("ClickableViewAccessibility")
class LimitedEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var limitedLine = -1
        private set
    var lastText = ""
        private set

    //Khi nhập text mà quá số lượng với trường hợp là enter ở cuối ô edittext
    //Hoặc không có text truyền sang ô mới
    var onLimitWithEmptyData: () -> Unit = {}

    ////Khi nhập text mà quá số lượng với trường hợp là có text truyền sang
    var onLimitWithLastLineData: (overflowText: String, currentDataCursor: DataCursor) -> Unit =
        { overflowText, currentDataCursor -> }
    var onClickDown: () -> Unit = {}

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastText = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (lineCount > limitedLine && lastText.isNotEmpty() && limitedLine != -1) {
                    //setText(lastText)
                    clearFocus()
                    var overflowText = getOverflowText()
                    if (overflowText.isEmpty() && !text.toString().endsWith("\n")) {
                        onLimitWithEmptyData.invoke()
                    } else {
                        val listCharacter = text.toString().toMutableList()
                        val lastChar = listCharacter.last().toInt()
                        val charEnter = '\n'.toInt()
                        var hasEnterAtEnd = false
                        if (lastChar == charEnter) {
                            //Đoan này là có enter ở cuối
                            overflowText = "\n"
                            hasEnterAtEnd = true
                        }
                        val lastIndexOfOverflow = text.toString().lastIndexOf(overflowText)
                        val currentDataCursor = getCurrentCursorData()
                        if (hasEnterAtEnd) {
                            setText(listCharacter.also { it.removeAt(listCharacter.size - 1) }
                                .joinToString(""))
                        } else {
                            setText(text.toString().substring(0, lastIndexOfOverflow - 1))
                        }
                        onLimitWithLastLineData.invoke(overflowText, currentDataCursor)
                    }
                }
            }

        })
        setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_UP -> {
                    onClickDown.invoke()
                }
                MotionEvent.ACTION_CANCEL -> {
                }
            }
            onTouchEvent(event)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculatorLimitedLine()
    }

    fun getCurrentCursorData(editText: EditText = this): DataCursor {
        val selectionStart = Selection.getSelectionStart(editText.text)
        val layout = editText.layout
        return if (selectionStart != -1) {
            DataCursor(selectionStart, layout.getLineForOffset(selectionStart))
        } else DataCursor(-1, -1)
    }

    fun calculatorLimitedLine() {
        limitedLine = height / lineHeight
    }

    fun calculateTextHeight(text: String): Int {
        return getStaticLayoutWithText(text).height
    }

    private fun getStaticLayoutWithText(text:String):StaticLayout{
        val alignment = Layout.Alignment.ALIGN_NORMAL
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setAlignment(alignment).setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setIncludePad(includeFontPadding)
                .build()
        } else {
            StaticLayout(
                text,
                paint,
                width,
                alignment,
                lineSpacingMultiplier,
                lineSpacingExtra,
                includeFontPadding
            )
        }
    }

    private fun getOverflowLineCount(text: String): Int {
        val totalLine = (calculateTextHeight(text).toFloat() / lineHeight).roundToInt()
        loge("Total Line: $totalLine")
        return if (totalLine <= limitedLine) {
            0
        } else totalLine - limitedLine
    }

    fun getOverflowText(): String {
        val overflowLineCount = getOverflowLineCount(text.toString())
        var textOverflow = ""
        if (overflowLineCount > 0) {
            val limitedIndex = limitedLine + overflowLineCount - 1
            val overLineStartIndex = layout.getLineStart(limitedIndex)
            textOverflow = text?.substring(overLineStartIndex, length()) ?: ""
//            loge("===============")
//            loge("Over flowLine TextLength: ${length()}")
//            loge("Over flowLine Index: $overflowLineCount")
//            loge("Over flowLine LimitedIndex: $limitedIndex")
//            loge("Over flowLine StartIndex: $overLineStartIndex")
//            loge("Over flowLine Text: ${textOverflow}")
//            loge("===============")
        }
        return textOverflow
    }

    fun applyLastText() {
        setText(lastText)
    }

    fun refreshText() {
        post{ //Nếu không post thì lineCount sẽ luôn = 0 vì nó chưa tính toán được là bao dòng
            setText(text.toString())
        }
    }

    class DataCursor(var currentCursorIndex: Int, var currentCursorLineIndex: Int)

}