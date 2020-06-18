package com.mrudik.goovi.ui.stats

import com.mrudik.goovi.Const
import com.mrudik.goovi.api.ApiService
import com.mrudik.goovi.api.model.stats.SplitStat
import com.mrudik.goovi.ui.stats.adapter.StatPerYearItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class StatsPresenter(
    private val apiService: ApiService,
    private val content: StatsContract.Content) : StatsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private var view: StatsContract.View? = null
    private var playerId = 0

    override fun takeView(view: StatsContract.View) {
        this.view = view
    }

    override fun clearView() {
        this.view = null
        this.compositeDisposable.dispose()
    }

    override fun start(playerId: Int) {
        this.playerId = playerId

        setTitle()
        loadFullStat()
    }

    private fun setTitle() {
        when (playerId) {
            Const.OVECHKIN_PLAYER_ID -> view?.setScreenTitle(content.getAlexOvechkinTitle())
            Const.GRETZKY_PLAYER_ID -> view?.setScreenTitle(content.getWayneGretzkyTitle())
        }
    }

    override fun loadFullStat() {
        val disposable: Disposable = apiService.loadPlayerStats(playerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val splitStatList = it.stats?.get(0)?.splitStat
                    if (splitStatList != null) {
                        val nhlStatList = getNHLStatOnly(splitStatList)
                        val totalGoals = getTotalNHLGoals(nhlStatList)
                        view?.showTotalGoals(totalGoals)

                        showGoalsDescription(totalGoals)
                        showYearByYearStat(nhlStatList)
                    }
                    // Copyright
                    view?.showCopyright(it.copyright ?: "")
                },
                {
                    view?.showError(content.getNetworkLoadingError())
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun getNHLStatOnly(splitStatList: ArrayList<SplitStat>) : ArrayList<SplitStat> {
        val nhlStatList = ArrayList<SplitStat>()
        for (stat in splitStatList) {
            if (stat.league?.leagueId == Const.NHL_LEAGUE_ID) {
                nhlStatList.add(stat)
            }
        }
        return nhlStatList
    }

    private fun getTotalNHLGoals(nhlStatList: ArrayList<SplitStat>) : Int {
        var totalGoals = 0
        for (stat in nhlStatList) {
            totalGoals += stat.playerStat?.goals!!
        }
        return totalGoals
    }

    private fun showGoalsDescription(totalGoals: Int) {
        if (playerId == Const.OVECHKIN_PLAYER_ID) {
            compareToGretzky(totalGoals)
        } else if (playerId == Const.GRETZKY_PLAYER_ID) {
            compareToOvechkin(totalGoals)
        }
    }

    private fun compareToGretzky(totalGoals: Int) {
        when {
            Const.GRETZKY_TOTAL_GOALS > totalGoals -> {
                view?.showGoalsDescriptionWithGretzkySpan(
                    String.format(
                        content.getTemplateGoalsToGretzky(),
                        Const.GRETZKY_TOTAL_GOALS - totalGoals
                    )
                )
            }
            Const.GRETZKY_TOTAL_GOALS == totalGoals -> {
                view?.showGoalsDescriptionWithGretzkySpan(
                    content.getTheSameAsGretzky()
                )
            }
            else -> {
                view?.showGoalsDescriptionWithGretzkySpan(
                    String.format(
                        content.getTemplateGoalsMoreThanGretzky(),
                        totalGoals - Const.GRETZKY_TOTAL_GOALS
                    )
                )
            }
        }
    }

    private fun compareToOvechkin(totalGoals: Int) {
        view?.showGoalsDescription(content.getNHLAllTimeLeadingGoalScorer())
        // TODO: To be implemented
    }

    private fun showYearByYearStat(nhlStatList: ArrayList<SplitStat>) {
        val statsPerYearList = ArrayList<StatPerYearItem>()

        // Header Item
        statsPerYearList.add(StatPerYearItem())

        // Stat Items
        for (i in nhlStatList.size - 1 downTo 0 step 1) {
            val splitStat = nhlStatList[i]
            val item = StatPerYearItem()
            item.season = getSlashedSeasonString(splitStat.season)
            splitStat.playerStat?.let {
                item.gamesPlayed = it.games.toString()
                item.goals = it.goals.toString()
                item.assists = it.assists.toString()
                item.points = it.points.toString()
            }
            statsPerYearList.add(item)
        }

        if (statsPerYearList.size > 1) {
            view?.showStatPerYear(statsPerYearList)
        } else {
            view?.showStatPerYearNotAvailableError()
        }
    }

    private fun getSlashedSeasonString(season: String?) : String {
        return season?.let {
            val firstPart = season.substring(0, 4)
            val secondPart = season.substring(4, 8)
            "$firstPart/$secondPart"
        } ?: ""
    }
}