package com.mrudik.goovi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mrudik.goovi.db.entity.DBPlayer

@Dao
interface DBPlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(league: DBPlayer)
}