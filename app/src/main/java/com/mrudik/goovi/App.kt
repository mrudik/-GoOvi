package com.mrudik.goovi

import android.app.Application
import com.facebook.stetho.Stetho
import com.mrudik.goovi.dagger.AppComponent
import com.mrudik.goovi.dagger.DaggerAppComponent
import com.mrudik.goovi.dagger.module.AppModule
import com.squareup.leakcanary.LeakCanary

class App : Application() {
    private val appComponent: AppComponent =
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        Stetho.initializeWithDefaults(this)
    }

    fun getAppComponent() : AppComponent {
        return appComponent
    }
}