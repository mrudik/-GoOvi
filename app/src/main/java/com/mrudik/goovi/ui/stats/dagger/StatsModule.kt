package com.mrudik.goovi.ui.stats.dagger

import android.content.Context
import com.mrudik.goovi.api.ApiService
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
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
    fun provideStatsPresenter(
        dbPlayerDao: DBPlayerDao,
        dbPlayerStatDao: DBPlayerStatDao,
        dbLeagueDao: DBLeagueDao,
        content: StatsContract.Content) : StatsContract.Presenter {

        return StatsPresenter(dbPlayerDao, dbPlayerStatDao, dbLeagueDao, content)
    }
}