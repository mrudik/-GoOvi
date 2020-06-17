package com.mrudik.goovi.api

import com.mrudik.goovi.api.model.playerinfo.FullPlayerInfo
import com.mrudik.goovi.api.model.stats.FullStat
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("people/{playerId}/stats?stats=yearByYear")
    fun loadPlayerStats(@Path("playerId") playerId: Int) : Observable<FullStat>

    @GET("people/{playerId}")
    fun loadPlayerInfo(@Path("playerId") playerId: Int) : Observable<FullPlayerInfo>
}