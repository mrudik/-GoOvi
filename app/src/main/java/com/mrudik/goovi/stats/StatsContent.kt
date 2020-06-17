package com.mrudik.goovi.stats

import android.content.Context
import com.mrudik.goovi.R
import com.mrudik.goovi.stats.StatsContract

class StatsContent(private val context: Context) : StatsContract.Content {
    override fun getAlexOvechkinTitle() : String {
        return context.getString(R.string.alex_ovechkin_number_8)
    }

    override fun getWayneGretzkyTitle() : String {
        return context.getString(R.string.wayne_gretzky_number_99)
    }

    override fun getNHLAllTimeLeadingGoalScorer(): String {
        return context.getString(R.string.nhl_all_time_leading_goal_scorer)
    }

    override fun getTemplateGoalsToGretzky(): String {
        return context.getString(R.string.template_goals_to_gretzky)
    }

    override fun getTemplateGoalsMoreThanGretzky(): String {
        return context.getString(R.string.template_goals_more_than_gretzky)
    }

    override fun getTheSameAsGretzky(): String {
        return context.getString(R.string.the_same_as_gretzky)
    }

    override fun getNetworkLoadingError(): String {
        return context.getString(R.string.network_loading_error)
    }
}