package com.mrudik.goovi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DBLeague(
    @PrimaryKey var leagueId: Long,
    var leagueName: String,
    var copyright: String?) {
}