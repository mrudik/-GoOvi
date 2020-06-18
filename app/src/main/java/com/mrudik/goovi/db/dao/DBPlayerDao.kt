package com.mrudik.goovi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrudik.goovi.db.entity.DBPlayer
import io.reactivex.Flowable

@Dao
interface DBPlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(league: DBPlayer)

    @Query("SELECT * FROM DBPlayer WHERE playerId = :playerId")
    fun getPlayer(playerId: Int) : Flowable<DBPlayer>
}