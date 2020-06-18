package com.mrudik.goovi.sync

import com.mrudik.goovi.Const
import com.mrudik.goovi.api.model.stats.FullStat
import com.mrudik.goovi.api.model.stats.SplitStat
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.db.entity.DBLeague
import com.mrudik.goovi.db.entity.DBPlayer
import com.mrudik.goovi.db.entity.DBPlayerStat

class SyncStatDBHelper(
    private val dbLeagueDao: DBLeagueDao,
    private val dbPlayerDao: DBPlayerDao,
    private val dbPlayerStatDao: DBPlayerStatDao) {

    var isSuccess = false

    fun parseAndInsertToDatabase(playerId: Int, fullStat: FullStat) : Boolean {
        val splitStatList = fullStat.stats?.get(0)?.splitStat
        if (splitStatList != null) {
            val nhlStatList = getNHLStatOnly(splitStatList)
            val totalGoals = getTotalNHLGoals(nhlStatList)

            insertPlayerToDatabase(playerId, totalGoals)
            insertLeagueToDatabase(fullStat.copyright)
            insertStatToDatabase(playerId, nhlStatList)

            isSuccess = true
        }

        return isSuccess
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