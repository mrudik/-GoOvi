package com.mrudik.goovi.api.model.stats

import com.google.gson.annotations.SerializedName

class FullStat {
    @SerializedName("copyright")
    var copyright: String? = ""
    @SerializedName("stats")
    var stats: ArrayList<TypeAndSplitStat>? = null
}