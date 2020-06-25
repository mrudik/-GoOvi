package com.mrudik.goovi.dagger.module

import android.content.Context
import com.mrudik.goovi.helper.Helper
import com.mrudik.goovi.helper.ObjectCreator
import com.mrudik.goovi.helper.ui.Screenshot
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

    @Singleton
    @Provides
    fun provideObjectCreator() : ObjectCreator {
        return ObjectCreator()
    }

    @Singleton
    @Provides
    fun provideScreenshot(@ApplicationContext context: Context) : Screenshot {
        return Screenshot(context)
    }
}