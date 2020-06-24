package com.mrudik.goovi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrudik.goovi.db.entity.DBPlayerStat
import io.reactivex.Flowable

@Dao
interface DBPlayerStatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(statList: List<DBPlayerStat>)

    @Query("DELETE FROM DBPlayerStat WHERE playerId = :playerId")
    fun deleteByPlayerId(playerId: Int)

    @Query("SELECT * FROM DBPlayerStat WHERE playerId = :playerId")
    fun getStatByPlayerId(playerId: Int): Flowable<List<DBPlayerStat>>
}