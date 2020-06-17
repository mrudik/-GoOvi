package com.mrudik.goovi.api.model.stats

import com.google.gson.annotations.SerializedName

class TypeAndSplitStat {
    @SerializedName("splits")
    var splitStat: ArrayList<SplitStat>? = null
}