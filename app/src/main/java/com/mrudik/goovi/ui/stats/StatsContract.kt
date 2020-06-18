package com.mrudik.goovi.ui.stats

import com.mrudik.goovi.ui.stats.adapter.StatPerYearItem

interface StatsContract {
    interface Presenter {
        fun start(playerId: Int)
        fun takeView(view: View)
        fun clearView()
        fun loadFullStat()
    }

    interface View {
        fun setScreenTitle(title: String)
        fun showError(message: String)
        fun showTotalGoals(goals: Int)
        fun showGoalsDescription(description: String)
        fun showGoalsDescriptionWithGretzkySpan(description: String)
        fun showStatPerYear(statPerYearList: ArrayList<StatPerYearItem>)
        fun showStatPerYearNotAvailableError()
        fun showCopyright(copyright: String)
    }

    interface Content {
        fun getAlexOvechkinTitle() : String
        fun getWayneGretzkyTitle() : String
        fun getNHLAllTimeLeadingGoalScorer() : String
        fun getTemplateGoalsToGretzky() : String
        fun getTemplateGoalsMoreThanGretzky() : String
        fun getTheSameAsGretzky() : String
        fun getNetworkLoadingError() : String
    }
}