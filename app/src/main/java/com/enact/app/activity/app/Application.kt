package com.enact.app.activity.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.enact.app.dependencyinjection.networkModule
import com.enact.app.dependencyinjection.repos
import com.enact.app.dependencyinjection.viewModels
import io.paperdb.Paper
import org.koin.android.ext.android.startKoin

class Application: MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(viewModels, networkModule, repos))
        Paper.init(this)


    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
      //  MultiDex.install(this)
    }

    override fun onTerminate() {

        super.onTerminate()
    }
}