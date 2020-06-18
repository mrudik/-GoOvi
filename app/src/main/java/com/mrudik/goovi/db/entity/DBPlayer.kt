package com.mrudik.goovi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DBPlayer(
    @PrimaryKey var playerId: Long,
    var totalGoals: Int) {

    var fullName: String = ""
}