package com.lhd.apps.pagenote.view

import androidx.databinding.ViewDataBinding
import com.base.baselibrary.fragment.BaseNavigationFragment
import com.lhd.apps.pagenote.MainActivity
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseMainFragment<BD : ViewDataBinding> : BaseNavigationFragment<BD, MainActivity>() {
}
