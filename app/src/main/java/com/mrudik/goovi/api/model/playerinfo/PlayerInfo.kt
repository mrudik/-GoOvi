package com.mrudik.goovi.api.model.playerinfo

import com.google.gson.annotations.SerializedName

class PlayerInfo {
    @SerializedName("id")
    var playerId: Int? = null
    @SerializedName("fullName")
    var fullName: String? = null
    @SerializedName("primaryNumber")
    var number: String? = null
    @SerializedName("birthDate")
    var birthDate: String? = null
    @SerializedName("currentAge")
    var age: String? = null
    @SerializedName("birthCity")
    var birthCity: String? = null
    @SerializedName("birthCountry")
    var birthCountry: String? = null
    @SerializedName("nationality")
    var nationality: String? = null
    @SerializedName("height")
    var height: String? = null
    @SerializedName("weight")
    var weight: Int? = null
    @SerializedName("captain")
    var isCaptain: Boolean? = false
    @SerializedName("currentTeam")
    var team: Team? = null
    @SerializedName("primaryPosition")
    var position: Position?= null
}