package com.mrudik.goovi.sync.repository

import com.mrudik.goovi.Const
import com.mrudik.goovi.api.ApiService
import com.mrudik.goovi.api.model.stats.SplitStat
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.db.entity.DBLeague
import com.mrudik.goovi.db.entity.DBPlayer
import com.mrudik.goovi.db.entity.DBPlayerStat
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SyncStatRepository(
    val apiService: ApiService,
    val dbLeagueDao: DBLeagueDao,
    val dbPlayerDao: DBPlayerDao,
    val dbPlayerStatDao: DBPlayerStatDao) {

    private val compositeDisposable = CompositeDisposable()

    fun loadFullStat(playerId: Int) {
        val disposable: Disposable = apiService.loadPlayerStats(playerId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    val splitStatList = it.stats?.get(0)?.splitStat
                    if (splitStatList != null) {
                        val nhlStatList = getNHLStatOnly(splitStatList)
                        val totalGoals = getTotalNHLGoals(nhlStatList)

                        insertPlayerToDatabase(playerId, totalGoals)
                        insertLeagueToDatabase(it.copyright)
                        insertStatToDatabase(playerId, nhlStatList)
                    }
                },
                {
                    compositeDisposable.dispose()
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

    private fun getSlashedSeasonString(season: String?) : String {
        return season?.let {
            val firstPart = season.substring(0, 4)
            val secondPart = season.substring(4, 8)
            "$firstPart/$secondPart"
        } ?: ""
    }

    private fun insertStatToDatabase(playerId: Int, nhlStatList: ArrayList<SplitStat>) {
        val dbPlayerStatList = ArrayList<DBPlayerStat>()

        // Stat Items
        for (i in nhlStatList.size - 1 downTo 0 step 1) {
            val splitStat = nhlStatList[i]

            val statItem = DBPlayerStat(playerId)
            statItem.season = getSlashedSeasonString(splitStat.season)
            statItem.leagueId = Const.NHL_LEAGUE_ID

            splitStat.playerStat?.let {
                statItem.gamesPlayed = it.games
                statItem.goals = it.goals
                statItem.assists = it.assists
                statItem.points = it.points
            }
            dbPlayerStatList.add(statItem)
        }

        if (dbPlayerStatList.size > 0) {
            dbPlayerStatDao.deleteByPlayerId(playerId)
            dbPlayerStatDao.insert(dbPlayerStatList)
        }
    }

    private fun insertLeagueToDatabase(copyright: String?) {
        dbLeagueDao.insert(
            DBLeague(
                Const.NHL_LEAGUE_ID.toLong(),
                Const.NHL_LEAGUE_NAME,
                copyright
            )
        )
    }

    private fun insertPlayerToDatabase(playerId: Int, totalGoals: Int) {
        dbPlayerDao.insert(
            DBPlayer(
                playerId.toLong(),
                totalGoals
            )
        )
    }
}