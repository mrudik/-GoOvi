package com.mrudik.goovi.ui.stats.dagger

import android.content.Context
import com.mrudik.goovi.api.ApiService
import com.mrudik.goovi.ui.stats.StatsContent
import com.mrudik.goovi.ui.stats.StatsContract
import com.mrudik.goovi.ui.stats.StatsPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object StatsModule {
    @ActivityScoped
    @Provides
    fun provideStatsContent(@ApplicationContext context: Context) : StatsContract.Content {
        return StatsContent(context)
    }

    @ActivityScoped
    @Provides
    fun provideStatsPresenter(apiService: ApiService, content: StatsContract.Content) : StatsContract.Presenter {
        return StatsPresenter(apiService, content)
    }
}