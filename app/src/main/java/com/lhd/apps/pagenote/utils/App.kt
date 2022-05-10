package com.lhd.apps.pagenote.utils

import com.base.baselibrary.utils.BaseApplication
import com.base.baselibrary.views.ext.loge
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lhd.log_module.AppLog
import com.lhd.log_module.LogEngine
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        AppLog.clearLogEngines()
        AppLog.addLogEngine(object : LogEngine {
            override fun initialize() {
                FirebaseApp.initializeApp(this@App)
            }

            override fun log(message: String?) {
                FirebaseCrashlytics.getInstance().log(message ?: "")
            }
        })
        AppLog.addLogEngine {
            loge(it)
        }
        AppLog.initialize()
    }

}