package com.mrudik.goovi.dagger.module

import com.mrudik.goovi.helper.ObjectCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideObjectCreator() : ObjectCreator {
        return ObjectCreator()
    }
}