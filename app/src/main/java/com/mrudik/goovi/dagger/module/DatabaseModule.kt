package com.mrudik.goovi.dagger.module

import android.content.Context
import androidx.room.Room
import com.mrudik.goovi.Const
import com.mrudik.goovi.db.Database
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            Const.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideDBLeagueDao(database: Database) : DBLeagueDao {
        return database.dbLeagueDao
    }

    @Singleton
    @Provides
    fun provideDBPlayerDao(database: Database) : DBPlayerDao {
        return database.dbPlayerDao
    }

    @Singleton
    @Provides
    fun provideDBPlayerStatDao(database: Database) : DBPlayerStatDao {
        return database.dbPlayerStatDao
    }
}