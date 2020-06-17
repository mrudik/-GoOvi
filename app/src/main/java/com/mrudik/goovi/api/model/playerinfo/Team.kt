package com.mrudik.goovi.api.model.playerinfo

import com.google.gson.annotations.SerializedName

class Team {
    @SerializedName("id")
    var teamId: Int? = null
    @SerializedName("name")
    var name: String? = null
}