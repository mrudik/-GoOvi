package com.mrudik.goovi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrudik.goovi.Const
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.db.entity.DBLeague
import com.mrudik.goovi.db.entity.DBPlayer
import com.mrudik.goovi.db.entity.DBPlayerStat

@Database(entities = [DBPlayer::class, DBLeague::class, DBPlayerStat::class], version = Const.DATABASE_VERSION, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract val dbLeagueDao: DBLeagueDao
    abstract val dbPlayerDao: DBPlayerDao
    abstract val dbPlayerStatDao: DBPlayerStatDao
}