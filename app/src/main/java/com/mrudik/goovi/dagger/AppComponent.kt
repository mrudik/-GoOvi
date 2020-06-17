package com.mrudik.goovi.dagger

import com.mrudik.goovi.dagger.module.AppModule
import com.mrudik.goovi.dagger.module.NetworkModule
import com.mrudik.goovi.stats.dagger.StatsSubComponent
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun createStatsSubComponent() : StatsSubComponent
}