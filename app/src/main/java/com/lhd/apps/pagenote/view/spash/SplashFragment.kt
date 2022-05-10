package com.lhd.apps.pagenote.view.spash

import com.base.baselibrary.utils.getAppColor
import com.base.baselibrary.views.ext.doInMainThread
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.databinding.FragmentSplashBinding
import com.lhd.apps.pagenote.view.BaseMainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseMainFragment<FragmentSplashBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun setHandleBack(): Boolean {
        return true
    }

    override fun isSetCustomColorStatusBar(): Boolean {
        return true
    }

    override fun getStatusBarColor(): Int {
        return getAppColor(R.color.color_bg_splash)
    }

    override fun isDarkText(): Boolean {
        return true
    }

    override fun initView() {
        doInMainThread({
            rootActivity.doSafeAction {
                navigateTo(R.id.action_splashFragment_to_homeFragment)
            }
        }, 3000)
    }

}