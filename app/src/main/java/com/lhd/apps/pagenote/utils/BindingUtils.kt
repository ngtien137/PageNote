package com.lhd.apps.pagenote.utils

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.base.baselibrary.adapter.BaseAdapter
import com.base.baselibrary.adapter.BaseListAdapter
import com.base.baselibrary.adapter.SuperAdapter
import com.base.baselibrary.utils.onDebouncedClick
import com.base.baselibrary.utils.onDebouncedClickGlobal
import com.base.baselibrary.views.custom.CustomViewPager
import com.base.baselibrary.views.ext.showKeyBoard
import com.base.baselibrary.views.rv_touch_helper.ItemTouchHelperExtension
import com.base.baselibrary.views.rv_touch_helper.VerticalDragTouchHelper
import com.bumptech.glide.Glide
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.models.AppFont
import com.lhd.apps.pagenote.view.custom.LimitedEditText
import java.io.File

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean): Int {
    return if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("pager_set_adapter")
fun ViewPager.setOwnAdapter(adapter: FragmentStatePagerAdapter?) {
    adapter?.let {
        setAdapter(adapter)
    }
}

@BindingAdapter("tv_apply_height")
fun TextView.applyHeightToNone(height: Float) {
    layoutParams.height = height.toInt()
}


@BindingAdapter("tv_apply_marquee")
fun TextView.applyMarquee(apply: Boolean?) {
    post {
        apply?.let {
            if (it) {
                ellipsize = TextUtils.TruncateAt.MARQUEE
                isSelected = true
            }
        }
    }
}

@BindingAdapter("tv_get_file_name_from_path")
fun TextView.setNameFromFile(path: String?) {
    path?.let {
        val file = File(it)
        text = file.name
    }
}

@BindingAdapter("sw_checked_listener")
fun CompoundButton.applyCheckedListener(checkedListener: CompoundButton.OnCheckedChangeListener?) {
    setOnCheckedChangeListener(checkedListener)
}

@BindingAdapter("translate_from_bottom")
fun View.translateFromBottom(isOpen: Boolean?) {
    post {
        if (translationY.toInt() == height && isOpen == true) {
            animate().translationY(0f)
        } else if (translationY.toInt() == 0 && isOpen != true) {
            animate().translationY(height.toFloat())
        }
    }
}

@BindingAdapter("translate_from_end")
fun View.translateFromEnd(isOpen: Boolean?) {
    post {
        if (translationX.toInt() == width && isOpen == true) {
            animate().translationY(0f)
        } else if (translationX.toInt() == 0 && isOpen != true) {
            animate().translationY(width.toFloat())
        }
    }
}

@BindingAdapter("debounceClick")
fun View.onDebouncedClick(listener: View.OnClickListener?) {
    listener?.let {
        this.onDebouncedClick {
            listener.onClick(this)
        }
    }
}

@BindingAdapter("debounceClickGlobal")
fun View.onDebouncedClickGlobalBinding(listener: View.OnClickListener?) {
    listener?.let {
        this.onDebouncedClickGlobal {
            listener.onClick(this)
        }
    }
}

@BindingAdapter("longClick")
fun View.onLongClickView(listener: View.OnLongClickListener) {
    setOnLongClickListener { v -> listener.onLongClick(v) }
}

@BindingAdapter("touchClick")
fun View.onTouchClickView(listener: View.OnTouchListener) {
    setOnTouchListener { v, ev ->
        listener.onTouch(v, ev)
    }
}

@BindingAdapter("rv_apply_item_touch_helper")
fun RecyclerView.applyItemTouchHelper(itemTouchHelperExtension: ItemTouchHelperExtension?) {
    itemTouchHelperExtension?.attachToRecyclerView(this)
}

@BindingAdapter("rv_vertical_drag")
fun RecyclerView.enableVerticalDrag(enable: Boolean?) {
    enable?.let {
        adapter?.let {
            if (adapter is SuperAdapter<*>) {
                val callback = VerticalDragTouchHelper(adapter as SuperAdapter<*>)
                ItemTouchHelper(callback).attachToRecyclerView(this)
            }
        }
    }
}

@BindingAdapter("rv_snap_linear")
fun RecyclerView.attachLinearSnapHelper(snap: Boolean? = true) {
    if (snap == true) {
        LinearSnapHelper().attachToRecyclerView(this)
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyAdapter(applyAdapter: BaseAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : RecyclerView.ViewHolder> RecyclerView.applyAdapter(applyAdapter: RecyclerView.Adapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyAdapter(applyAdapter: SuperAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyListAdapter(applyAdapter: BaseListAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_fix_size")
fun RecyclerView.setFixSize(set: Boolean?) {
    setHasFixedSize(set ?: false)
}

@BindingAdapter("img_set_drawable_id")
fun ImageView.setDrawableById(id: Int) {
    setImageResource(id)
}

@BindingAdapter("anim_visible")
fun View.startAnimVisible(visible: Int?) {
    if (visible == null) {
        return
    }
    if (visible == View.VISIBLE && this.visibility != View.VISIBLE) {
        this.visibility = visible
        alpha = 0f
        animate().alpha(1f)
    } else if ((visible == View.GONE && this.visibility != View.GONE) || (visible == View.INVISIBLE && this.visibility != View.INVISIBLE)) {
        alpha = 1f
        animate().alpha(0f)
        postDelayed({
            this.visibility = visible
        }, 300)
    }
}

@BindingAdapter("pager_swipe_able")
fun CustomViewPager.setSwipeAble(swipe: Boolean?) {
    swipe?.let {
        isAbleSwipe = it
    }
}


//Your binding write below

@BindingAdapter("glide_load_drawable")
fun ImageView.glideLoadDrawable(drawable: Drawable?) {
    drawable?.let {
        post {
            Glide.with(this).load(drawable).override(width, height).into(this)
        }
    }
}

@BindingAdapter("glide_load_path")
fun ImageView.glideLoadPath(path: String?) {
    path?.let {
        post {
            Glide.with(this).load(path).override(width, height).into(this)
        }
    }
}

@BindingAdapter("glide_load_uri")
fun ImageView.glideLoadPath(uri: Uri?) {
    uri?.let {
        post {
            Glide.with(this).load(uri).override(width, height).into(this)
        }
    }
}

@BindingAdapter("glide_load_drawable_id")
fun ImageView.glideLoadDrawable(id: Int?) {
    id?.let {
        post {
            Glide.with(this).load(id).override(width, height)
                .into(this)
        }
    }
}

@BindingAdapter("img_load_drawable_id")
fun ImageView.imgLoadDrawableById(id: Int?) {
    id?.let {
        setImageResource(id)
    }
}

@BindingAdapter("ivLoadBackgroundById")
fun ImageView.imgLoadColorById(id: Int) {
    setImageResource(id)
}

@BindingAdapter("tv_set_underline_text")
fun TextView.tvSetUnderlineText(text: String?) {
    val content = SpannableString(text ?: "")
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    setText(content)
}

@BindingAdapter("sbSetSeekBarChangeListener")
fun SeekBar.setSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener) {
    this.setOnSeekBarChangeListener(listener)
}


@BindingAdapter("img_anim_menu")
fun ImageView.animXHomeMenuButton(liveStateMenu: MutableLiveData<Int>?) {
    post {
        if (liveStateMenu?.value == AppConst.STATE_OPEN) {
            animate().setDuration(400).setInterpolator(BounceInterpolator()).rotation(135f)
        } else {
            animate().setDuration(400).setInterpolator(BounceInterpolator()).rotation(0f)
        }
    }
}

@BindingAdapter("edt_focus_index", "edt_focus_live_index")
fun EditText.focusByIndex(editTextIndex: Int?, liveIndex: MutableLiveData<Int>?) {
    liveIndex?.let {
        if (editTextIndex == liveIndex.value) {
            requestFocus()
//            post(Runnable {
//                showKeyBoard()
//            })
        } else {
            clearFocus()
        }
    }
}

@BindingAdapter("anim_menu_item")
fun View.animXMenuItem(liveStateMenu: MutableLiveData<Int>?) {
    post {
        if (liveStateMenu?.value == AppConst.STATE_OPEN) {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            clearAnimation()
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_open))
        } else {
            isClickable = false
            isFocusable = false
            clearAnimation()
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_close))
        }
    }
}

@BindingAdapter("anim_menu_bottom")
fun View.animBottomMenu(liveStateMenu: MutableLiveData<Int>?) {
    post {
        if (liveStateMenu?.value == AppConst.STATE_OPEN) {
            animate().translationY(-height.toFloat()).alpha(1f).start()
        } else {
            animate().translationY(0f).alpha(0f).start()
        }
    }
}

@BindingAdapter("edt_font")
fun LimitedEditText.setLiveFont(liveFont: MutableLiveData<Typeface>?) {
    liveFont?.value?.let {
        typeface = it
        calculatorLimitedLine()
    }
}

@BindingAdapter("edt_font_size")
fun LimitedEditText.setLiveFontSize(liveFontSize: MutableLiveData<Float>?) {
    liveFontSize?.value?.let {
        textSize = it
        calculatorLimitedLine()
        refreshText()
    }
}

@BindingAdapter("tv_set_font")
fun TextView.tvSetFontByAppFont(appFont: AppFont?) {
    appFont?.let {
        typeface = appFont.typeface
    }
}

