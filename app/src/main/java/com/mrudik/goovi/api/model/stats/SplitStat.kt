package com.mrudik.goovi.api.model.stats

import com.google.gson.annotations.SerializedName

class SplitStat {
    @SerializedName("season")
    var season: String? = ""
    @SerializedName("stat")
    var playerStat: PlayerStat? = null
    @SerializedName("league")
    var league: League? = null
}