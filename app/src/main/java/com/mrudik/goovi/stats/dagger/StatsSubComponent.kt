package com.mrudik.goovi.stats.dagger

import com.mrudik.goovi.stats.StatsActivity
import dagger.Subcomponent

@StatsScope
@Subcomponent(modules = [StatsModule::class])
interface StatsSubComponent {
    fun inject(activity: StatsActivity)
}