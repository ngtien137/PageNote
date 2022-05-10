package com.lhd.apps.pagenote.view.screen.home

import android.graphics.Color
import androidx.fragment.app.viewModels
import com.lhd.apps.pagenote.R
import com.lhd.apps.pagenote.databinding.FragmentHomeBinding
import com.lhd.apps.pagenote.view.BaseMainFragment
import com.lhd.apps.pagenote.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseMainFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
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

    override fun onBackPressed() {
        rootActivity.finish()
    }

    override fun initView() {

    }

    override fun initBinding() {

    }

    override fun onViewClick(vId: Int) {
        when (vId) {
            R.id.btnCreateNoteFromEmpty -> {
                navigateTo(R.id.action_homeFragment_to_inputNoteFragment)
            }
        }
    }

}