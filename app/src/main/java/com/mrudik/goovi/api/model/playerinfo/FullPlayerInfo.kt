package com.mrudik.goovi.api.model.playerinfo

import com.google.gson.annotations.SerializedName

class FullPlayerInfo {
    @SerializedName("copyright")
    var copyright: String? = ""
    @SerializedName("people")
    var people: ArrayList<PlayerInfo>? = null
}