package com.mrudik.goovi.api

import com.mrudik.goovi.api.model.stats.FullStat
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("people/{playerId}/stats?stats=yearByYear")
    fun loadPlayerStat(@Path("playerId") playerId: Int) : Single<FullStat>
}