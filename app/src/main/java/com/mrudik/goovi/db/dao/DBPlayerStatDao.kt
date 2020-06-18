package com.mrudik.goovi.db.dao

import androidx.room.*
import com.mrudik.goovi.db.entity.DBPlayerStat

@Dao
interface DBPlayerStatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(statList: List<DBPlayerStat>)

    @Query("Delete from DBPlayerStat where playerId = :playerId")
    fun deleteByPlayerId(playerId: Int)
}