package com.mrudik.goovi.dagger.module

import android.content.Context
import com.mrudik.goovi.dagger.AppScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class AppModule(private val appContext: Context) {
    @AppScope
    @Provides
    fun provideAppContext() : Context {
        return appContext
    }
}