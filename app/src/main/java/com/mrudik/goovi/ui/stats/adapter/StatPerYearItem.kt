package com.mrudik.goovi.ui.stats.adapter

import com.mrudik.goovi.db.entity.DBPlayerStat

open class StatPerYearItem {
    var season = "Season"
    var gamesPlayed = "GP"
    var goals = "G"
    var assists = "A"
    var points = "P"

    constructor()

    constructor(dbPlayerStat: DBPlayerStat) {
        this.season = dbPlayerStat.season
        this.gamesPlayed = dbPlayerStat.gamesPlayed.toString()
        this.goals = dbPlayerStat.goals.toString()
        this.assists = dbPlayerStat.assists.toString()
        this.points = dbPlayerStat.points.toString()
    }
}