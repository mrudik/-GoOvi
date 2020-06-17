package com.mrudik.goovi.api.model.stats

import com.google.gson.annotations.SerializedName

class League {
    @SerializedName("id")
    var leagueId: Int = 0
    @SerializedName("name")
    var leagueName: String? = null
}