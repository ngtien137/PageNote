package com.lhd.apps.pagenote

import android.Manifest
import com.base.baselibrary.activity.BaseActivity
import com.lhd.apps.pagenote.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    val listPermissionWriteRead = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    fun grantPermission(permissions: Array<String>, onAllow: () -> Unit) {
        doRequestPermission(permissions, onAllow, {
            grantPermission(permissions, onAllow)
        })
    }

}