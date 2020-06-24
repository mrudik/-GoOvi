package com.mrudik.goovi.stats

import com.mrudik.goovi.Const
import com.mrudik.goovi.db.dao.DBLeagueDao
import com.mrudik.goovi.db.dao.DBPlayerDao
import com.mrudik.goovi.db.dao.DBPlayerStatDao
import com.mrudik.goovi.db.entity.DBLeague
import com.mrudik.goovi.db.entity.DBPlayer
import com.mrudik.goovi.db.entity.DBPlayerStat
import com.mrudik.goovi.helper.ObjectCreator
import com.mrudik.goovi.helper.scheduler.TrampolineSchedulerProvider
import com.mrudik.goovi.ui.stats.StatsContract
import com.mrudik.goovi.ui.stats.StatsPresenter
import com.mrudik.goovi.ui.stats.adapter.StatPerYearItem
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory

class StatsPresenterTest {
    private var schedulerProvider = TrampolineSchedulerProvider()

    @Mock
    private lateinit var mockDbPlayerDao: DBPlayerDao
    @Mock
    private lateinit var mockDbPlayerStatDao: DBPlayerStatDao
    @Mock
    private lateinit var mockDbLeagueDao: DBLeagueDao
    @Mock
    private lateinit var mockContent: StatsContract.Content
    @Mock
    private lateinit var mockView: StatsContract.View
    @Mock
    private lateinit var mockObjectCreator: ObjectCreator

    private lateinit var dbPlayerOvi: DBPlayer
    private lateinit var dbPlayerGretzky: DBPlayer
    private lateinit var dbPlayersList: ArrayList<DBPlayer>

    private lateinit var dbPlayerStatList: ArrayList<DBPlayerStat>

    private lateinit var dbLeague: DBLeague

    private lateinit var presenter: StatsPresenter

    companion object {
        const val DB_COPYRIGHT = "Copyright from DB"
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = StatsPresenter(
            mockDbPlayerDao, mockDbPlayerStatDao, mockDbLeagueDao,
            mockContent, schedulerProvider, mockObjectCreator
        )
        presenter.takeView(mockView)

        // Entity
        dbPlayerOvi = DBPlayer(Const.OVECHKIN_PLAYER_ID.toLong(), 700)
        dbPlayerGretzky = DBPlayer(Const.GRETZKY_PLAYER_ID.toLong(), 894)
        dbPlayersList = arrayListOf(dbPlayerOvi, dbPlayerGretzky)

        dbLeague = DBLeague(Const.NHL_LEAGUE_ID.toLong(), Const.NHL_LEAGUE_NAME, DB_COPYRIGHT)

        dbPlayerStatList = arrayListOf(
            DBPlayerStat(Const.OVECHKIN_PLAYER_ID),
            DBPlayerStat(Const.OVECHKIN_PLAYER_ID)
        )

        // Query
        Mockito.`when`(mockDbPlayerDao.getPlayers()).thenReturn(Flowable.fromArray(dbPlayersList))
        Mockito.`when`(mockDbPlayerStatDao.getStatByPlayerId(Mockito.anyInt())).thenReturn(Flowable.fromArray(dbPlayerStatList))
        Mockito.`when`(mockDbLeagueDao.getLeague(Mockito.anyInt())).thenReturn(Flowable.fromArray(dbLeague))
    }

    @Test
    fun start_calls_setScreenTitle_when_playerId_is_Ovi() {
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).setScreenTitle(mockContent.getAlexOvechkinTitle())
    }

    @Test
    fun start_calls_setScreenTitle_when_playerId_is_Gretzky() {
        presenter.start(Const.GRETZKY_PLAYER_ID)
        Mockito.verify(mockView).setScreenTitle(mockContent.getWayneGretzkyTitle())
    }

    @Test
    fun start_no_calls_setScreenTitle_when_playerId_is_unknown() {
        presenter.start(0)
        Mockito.verify(mockView, VerificationModeFactory.times(0)).setScreenTitle(Mockito.anyString())
    }

    @Test
    fun start_calls_showTotalGoals_when_playerId_is_present_in_playersList() {
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showTotalGoals(dbPlayerOvi.totalGoals)
    }

    @Test
    fun start_no_calls_showTotalGoals_when_playerId_is_not_present_in_playersList() {
        dbPlayersList.remove(dbPlayerOvi)

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView, VerificationModeFactory.times(0)).showTotalGoals(dbPlayerOvi.totalGoals)
    }

    @Test
    fun start_calls_showCopyright_from_database_when_present() {
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showCopyright(dbLeague.copyright!!)
    }

    @Test
    fun start_calls_showCopyright_from_content_when_copyright_from_db_not_present() {
        dbLeague.copyright = null

        Mockito.`when`(mockContent.getCopyright()).thenReturn("Copyright from content")

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showCopyright(mockContent.getCopyright())
    }

    @Test
    fun start_calls_showGoalsDescription_theSameAsGretzky_when_playerId_is_Ovi_and_goals_equal() {
        dbPlayerOvi.totalGoals = 700
        dbPlayerGretzky.totalGoals = 700

        Mockito.`when`(mockContent.getTheSameAsGretzky()).thenReturn("The same")

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showGoalsDescriptionWithGretzkySpan(mockContent.getTheSameAsGretzky())
    }

    @Test
    fun start_calls_showGoalsDescription_goalsToGretzky_when_playerId_is_Ovi_and_goals_less_than_Gretzky() {
        dbPlayerOvi.totalGoals = 600
        dbPlayerGretzky.totalGoals = 700

        val diff = dbPlayerGretzky.totalGoals - dbPlayerOvi.totalGoals
        val msg = " to Gretzky"

        Mockito.`when`(mockContent.getTemplateGoalsToGretzky()).thenReturn("%1\$s $msg")

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showGoalsDescriptionWithGretzkySpan("$diff $msg" )
    }

    @Test
    fun start_calls_showGoalsDescription_goalsMoreThanGretzky_when_playerId_is_Ovi_and_goals_more_than_Gretzky() {
        dbPlayerOvi.totalGoals = 800
        dbPlayerGretzky.totalGoals = 700

        val diff = dbPlayerOvi.totalGoals - dbPlayerGretzky.totalGoals
        val msg = " more than Gretzky"

        Mockito.`when`(mockContent.getTemplateGoalsMoreThanGretzky()).thenReturn("%1\$s $msg")

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showGoalsDescriptionWithGretzkySpan("$diff $msg" )
    }

    @Test
    fun start_calls_showGoalsDescription_theSameAsOvi_when_playerId_is_Gretzky_and_goals_equal() {
        dbPlayerOvi.totalGoals = 700
        dbPlayerGretzky.totalGoals = 700

        Mockito.`when`(mockContent.getTheSameAsOvechkin()).thenReturn("The same")

        presenter.start(Const.GRETZKY_PLAYER_ID)
        Mockito.verify(mockView).showGoalsDescriptionWithOvechkinSpan(mockContent.getTheSameAsOvechkin())
    }

    @Test
    fun start_calls_showGoalsDescription_nhlAllTimeLeader_when_playerId_is_Gretzky_and_goals_more_than_Ovi() {
        dbPlayerOvi.totalGoals = 600
        dbPlayerGretzky.totalGoals = 700

        Mockito.`when`(mockContent.getNHLAllTimeLeadingGoalScorer()).thenReturn("All-time leader")

        presenter.start(Const.GRETZKY_PLAYER_ID)
        Mockito.verify(mockView).showGoalsDescription(mockContent.getNHLAllTimeLeadingGoalScorer())
    }

    @Test
    fun start_calls_showGoalsDescription_goalsLessThanOvi_when_playerId_is_Gretzky_and_goals_less_than_Ovi() {
        dbPlayerOvi.totalGoals = 800
        dbPlayerGretzky.totalGoals = 700

        val diff = dbPlayerOvi.totalGoals - dbPlayerGretzky.totalGoals
        val msg = " less than Ovi"

        Mockito.`when`(mockContent.getTemplateGoalsLessThanOvechkin()).thenReturn("%1\$s $msg")

        presenter.start(Const.GRETZKY_PLAYER_ID)
        Mockito.verify(mockView).showGoalsDescriptionWithOvechkinSpan("$diff $msg" )
    }

    @Test
    fun start_not_calls_showCopyright_when_view_is_null() {
        presenter.clearView()
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView, VerificationModeFactory.times(0)).showCopyright(Mockito.anyString())
    }

    @Test
    fun start_not_calls_setScreenTitle_when_view_is_null() {
        presenter.clearView()
        presenter.start(Const.GRETZKY_PLAYER_ID)
        Mockito.verify(mockView, VerificationModeFactory.times(0)).setScreenTitle(Mockito.anyString())
    }

    @Test
    fun playerNameClickAction_calls_showGretzkyScreen_when_playerId_is_Ovi() {
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        presenter.playerNameClickAction()
        Mockito.verify(mockView).showGretzkyScreen()
    }

    @Test
    fun playerNameClickAction_calls_closeScreen_when_playerId_is_Gretzky() {
        presenter.start(Const.GRETZKY_PLAYER_ID)
        presenter.playerNameClickAction()
        Mockito.verify(mockView).closeScreen()
    }

    @Test
    fun playerNameClickAction_not_calls_closeScreen_when_playerId_is_unknown() {
        presenter.start(0)
        presenter.playerNameClickAction()
        Mockito.verify(mockView, VerificationModeFactory.times(0)).closeScreen()
    }

    @Test
    fun playerNameClickAction_not_calls_showGretzkyScreen_when_playerId_is_unknown() {
        presenter.start(0)
        presenter.playerNameClickAction()
        Mockito.verify(mockView, VerificationModeFactory.times(0)).showGretzkyScreen()
    }

    @Test
    fun playerNameClickAction_not_calls_showGretzkyScreen_when_playerId_is_Ovi_view_is_null() {
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        presenter.clearView()
        presenter.playerNameClickAction()
        Mockito.verify(mockView, VerificationModeFactory.times(0)).showGretzkyScreen()
    }

    @Test
    fun playerNameClickAction_not_calls_closeScreen_when_playerId_is_Gretzky_view_is_null() {
        presenter.start(Const.GRETZKY_PLAYER_ID)
        presenter.clearView()
        presenter.playerNameClickAction()
        Mockito.verify(mockView, VerificationModeFactory.times(0)).closeScreen()
    }

    @Test
    fun start_calls_showStatPerYear_when_dbPlayerStat_is_present() {
        val statPerYearList = ArrayList<StatPerYearItem>()

        Mockito.`when`(mockObjectCreator.createStatPerYearItemArrayList()).thenReturn(statPerYearList)

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showStatPerYear(statPerYearList)
    }

    @Test
    fun start_calls_showStatPerYearNotAvailableError_when_dbPlayerStat_has_only_header_stat() {
        dbPlayerStatList.clear()

        val statPerYearList = ArrayList<StatPerYearItem>()

        Mockito.`when`(mockObjectCreator.createStatPerYearItemArrayList()).thenReturn(statPerYearList)

        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView).showStatPerYearNotAvailableError()
    }

    @Test
    fun start_not_calls_showStatPerYearNotAvailableError_when_dbPlayerStat_has_only_header_stat_view_is_null() {
        dbPlayerStatList.clear()

        val statPerYearList = ArrayList<StatPerYearItem>()

        Mockito.`when`(mockObjectCreator.createStatPerYearItemArrayList()).thenReturn(statPerYearList)

        presenter.clearView()
        presenter.start(Const.OVECHKIN_PLAYER_ID)
        Mockito.verify(mockView, VerificationModeFactory.times(0)).showStatPerYearNotAvailableError()
    }
}