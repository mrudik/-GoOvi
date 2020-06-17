package com.mrudik.goovi.api.model.playerinfo

import com.google.gson.annotations.SerializedName

class Position {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("abbreviation")
    var abbreviation: String? = null
}