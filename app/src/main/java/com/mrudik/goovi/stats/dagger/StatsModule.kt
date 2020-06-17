package com.mrudik.goovi.stats.dagger

import android.content.Context
import com.mrudik.goovi.api.ApiService
import com.mrudik.goovi.stats.StatsContract
import com.mrudik.goovi.stats.StatsPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class StatsModule {
    @StatsScope
    @Provides
    fun provideStatsContent(context: Context) : StatsContract.Content {
        return StatsContent(context)
    }

    @StatsScope
    @Provides
    fun provideStatsPresenter(apiService: ApiService, content: StatsContract.Content) : StatsContract.Presenter {
        return StatsPresenter(apiService, content)
    }
}