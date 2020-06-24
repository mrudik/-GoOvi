package com.mrudik.goovi.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.helper.Helper
import com.mrudik.goovi.sync.SyncManager
import com.mrudik.goovi.sync.SyncStatDBHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SyncModule {

    @Singleton
    @Provides
    fun provideSyncManager(
        @ApplicationContext context: Context,
        preferences: SharedPreferences,
        helper: Helper) : SyncManager {

        return SyncManager(context, preferences, helper)
    }

    @Singleton
    @Provides
    fun provideSyncStatDBHelper(
        dbLeagueDao: DBLeagueDao,
        dbPlayerDao: DBPlayerDao,
        dbPlayerStatDao: DBPlayerStatDao) : SyncStatDBHelper {

        return SyncStatDBHelper(
            dbLeagueDao,
            dbPlayerDao,
            dbPlayerStatDao
        )
    }
}