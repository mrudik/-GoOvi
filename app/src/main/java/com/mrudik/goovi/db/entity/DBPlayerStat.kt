package com.mrudik.goovi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DBPlayerStat(var playerId: Int) {
    @PrimaryKey(autoGenerate = true)
    var statId: Int = 0
    var leagueId: Int = 0
    var season: String = ""
    var goals: Int = 0
    var gamesPlayed: Int = 0
    var assists: Int = 0
    var points: Int = 0
}