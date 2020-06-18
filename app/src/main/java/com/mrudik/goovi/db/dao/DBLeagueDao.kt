package com.mrudik.goovi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mrudik.goovi.db.entity.DBLeague

@Dao
interface DBLeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(league: DBLeague)
}