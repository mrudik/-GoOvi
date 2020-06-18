package com.mrudik.goovi.dagger.module

import android.content.Context
import com.mrudik.goovi.api.ApiService
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.sync.repository.SyncStatRepository
import com.mrudik.goovi.sync.SyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideStatRemoteRepository(apiService: ApiService,
                                    dbLeagueDao: DBLeagueDao,
                                    dbPlayerDao: DBPlayerDao,
                                    dbPlayerStatDao: DBPlayerStatDao) : SyncStatRepository {

        return SyncStatRepository(
            apiService,
            dbLeagueDao,
            dbPlayerDao,
            dbPlayerStatDao
        )
    }

    @Singleton
    @Provides
    fun provideSyncManager(@ApplicationContext context: Context) : SyncManager {
        return SyncManager(context)
    }
}