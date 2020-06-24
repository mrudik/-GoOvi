package com.mrudik.goovi.dagger.module

import android.content.Context
import com.mrudik.goovi.helper.Helper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHelper(@ApplicationContext context: Context) : Helper {
        return Helper(context)
    }
}