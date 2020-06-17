package com.mrudik.goovi.dagger.module

import android.content.Context
import com.mrudik.goovi.dagger.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val appContext: Context) {
    @AppScope
    @Provides
    fun provideAppContext() : Context {
        return appContext
    }
}