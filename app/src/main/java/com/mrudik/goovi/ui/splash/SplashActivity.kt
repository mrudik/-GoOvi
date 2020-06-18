package com.mrudik.goovi.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mrudik.goovi.Const
import com.mrudik.goovi.R
import com.mrudik.goovi.ui.stats.StatsActivity
import com.mrudik.goovi.sync.SyncManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var handler: Handler
    lateinit var runnable: Runnable

    @Inject
    lateinit var syncManager: SyncManager

    companion object {
        const val DELAY = 1000 * 1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        syncManager.sync()
        showStatsScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun showStatsScreen() {
        handler = Handler()
        runnable = Runnable {
            val intent = Intent(this, StatsActivity::class.java)
            intent.putExtra(StatsActivity.KEY_PLAYED_ID, Const.OVECHKIN_PLAYER_ID)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

            finish()
        }
        handler.postDelayed(runnable, DELAY)
    }
}