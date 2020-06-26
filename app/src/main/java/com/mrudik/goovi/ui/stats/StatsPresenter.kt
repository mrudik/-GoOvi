package com.mrudik.goovi.ui.stats

import com.mrudik.goovi.Const
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.db.entity.DBLeague
import com.mrudik.goovi.db.entity.DBPlayer
import com.mrudik.goovi.db.entity.DBPlayerStat
import com.mrudik.goovi.helper.ObjectCreator
import com.mrudik.goovi.helper.scheduler.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class StatsPresenter(
    private val dbPlayerDao: DBPlayerDao,
    private val dbPlayerStatDao: DBPlayerStatDao,
    private val dbLeagueDao: DBLeagueDao,
    private val content: StatsContract.Content,
    private val schedulerProvider: BaseSchedulerProvider,
    private val objectCreator: ObjectCreator) : StatsContract.Presenter {

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
        setBackButton()
        loadTotalGoals()
        loadYearByYearStat()
        loadCopyright()
    }

    override fun playerNameClickAction() {
        when (playerId) {
            Const.OVECHKIN_PLAYER_ID -> view?.showGretzkyScreen()
            Const.GRETZKY_PLAYER_ID -> view?.closeScreen()
        }
    }

    private fun setTitle() {
        when (playerId) {
            Const.OVECHKIN_PLAYER_ID -> view?.setScreenTitle(content.getAlexOvechkinTitle())
            Const.GRETZKY_PLAYER_ID -> view?.setScreenTitle(content.getWayneGretzkyTitle())
        }
    }

    private fun setBackButton() {
        if (playerId == Const.GRETZKY_PLAYER_ID) {
            view?.showBackButton()
        }
    }

    private fun loadTotalGoals() {
        val disposable = dbPlayerDao.getPlayers()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                {
                    showTotalGoals(it)
                    compareTotalGoalsToOpponent(it)
                },
                {
                    TODO("Show error")
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun loadYearByYearStat() {
        val disposable = dbPlayerStatDao.getStatByPlayerId(playerId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                {
                    showYearByYearStat(it)
                },
                {
                    TODO("Show error")
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun loadCopyright() {
        val disposable = dbLeagueDao.getLeague(Const.NHL_LEAGUE_ID)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                {
                    showCopyright(it)
                },
                {
                    TODO("Show error")
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun showTotalGoals(dbPlayersList: List<DBPlayer>) {
        for (dbPlayer in dbPlayersList) {
            if (dbPlayer.playerId == playerId.toLong()) {
                view?.showTotalGoals(dbPlayer.totalGoals)
                break
            }
        }
    }

    private fun compareTotalGoalsToOpponent(dbPlayersList: List<DBPlayer>) {
        var currentPlayerGoals = 0
        var opponentPlayerGoals = 0

        for (dbPlayer in dbPlayersList) {
            if (dbPlayer.playerId == playerId.toLong()) {
                currentPlayerGoals = dbPlayer.totalGoals
            } else {
                opponentPlayerGoals = dbPlayer.totalGoals
            }
        }

        if (playerId == Const.OVECHKIN_PLAYER_ID) {
            compareToGretzky(currentPlayerGoals, opponentPlayerGoals)
        } else {
            compareToOvechkin(currentPlayerGoals, opponentPlayerGoals)
        }
    }

    private fun compareToGretzky(oviTotalGoals: Int, gretzkyTotalGoals: Int) {
        when {
            gretzkyTotalGoals > oviTotalGoals -> {
                view?.showGoalsDescriptionWithGretzkySpan(
                    String.format(
                        content.getTemplateGoalsToGretzky(),
                        gretzkyTotalGoals - oviTotalGoals
                    )
                )
            }
            gretzkyTotalGoals == oviTotalGoals -> {
                view?.showGoalsDescriptionWithGretzkySpan(
                    content.getTheSameAsGretzky()
                )
            }
            else -> {
                view?.showGoalsDescriptionWithGretzkySpan(
                    String.format(
                        content.getTemplateGoalsMoreThanGretzky(),
                        oviTotalGoals - gretzkyTotalGoals
                    )
                )
            }
        }
    }

    private fun compareToOvechkin(gretzkyTotalGoals: Int, oviTotalGoals: Int) {
        when {
            gretzkyTotalGoals > oviTotalGoals -> {
                view?.showGoalsDescription(content.getNHLAllTimeLeadingGoalScorer())
            }
            gretzkyTotalGoals == oviTotalGoals -> {
                view?.showGoalsDescriptionWithOvechkinSpan(
                    content.getTheSameAsOvechkin()
                )
            }
            else -> {
                view?.showGoalsDescriptionWithOvechkinSpan(
                    String.format(
                        content.getTemplateGoalsLessThanOvechkin(),
                        oviTotalGoals - gretzkyTotalGoals
                    )
                )
            }
        }
    }

    private fun showYearByYearStat(dbPlayerStatList: List<DBPlayerStat>) {
        val statsPerYearList = objectCreator.createStatPerYearItemArrayList()

        // Header
        statsPerYearList.add(objectCreator.createStatPerYearItem())

        // Items
        for (dbPlayerStat in dbPlayerStatList) {
            statsPerYearList.add(
                objectCreator.createStatPerYearItem(dbPlayerStat)
            )
        }

        if (statsPerYearList.size > 1) {
            view?.showStatPerYear(statsPerYearList)
        } else {
            view?.showStatPerYearNotAvailableError()
        }
    }

    private fun showCopyright(dbLeague: DBLeague) {
        view?.showCopyright(dbLeague.copyright ?: content.getCopyright())
    }
}