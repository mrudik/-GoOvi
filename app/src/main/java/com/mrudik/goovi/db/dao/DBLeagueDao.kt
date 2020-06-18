package com.mrudik.goovi.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrudik.goovi.db.entity.DBLeague
import io.reactivex.Flowable

@Dao
interface DBLeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(league: DBLeague)

    @Query("SELECT * FROM DBLeague WHERE leagueId = :leagueId")
    fun getLeague(leagueId: Int) : Flowable<DBLeague>
}