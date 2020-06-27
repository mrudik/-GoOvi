package com.mrudik.goovi.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mrudik.goovi.Const
import com.mrudik.goovi.R
import com.mrudik.goovi.sync.SyncManager
import com.mrudik.goovi.ui.stats.StatsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(), SyncManager.SyncStatus {
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    @Inject
    lateinit var syncManager: SyncManager

    companion object {
        const val DELAY = 500 * 1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        syncManager.sync(this, this)
    }

    override fun onDestroy() {
        syncManager.stopSync()
        handler?.removeCallbacks(runnable!!)
        runnable = null
        handler = null
        super.onDestroy()
    }

    override fun syncSucceeded() {
        handler = Handler()
        runnable = Runnable {
            if (!isFinishing) {
                val intent = Intent(this, StatsActivity::class.java)
                intent.putExtra(StatsActivity.KEY_PLAYER_ID, Const.OVECHKIN_PLAYER_ID)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)

                finish()
            }

        }
        handler!!.postDelayed(runnable!!, DELAY)
    }

    override fun syncFailed() {
        Snackbar
            .make(
                splashLayout,
                getString(R.string.network_loading_error),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(getString(R.string.try_again)) {
                syncManager.stopSync()
                syncManager.sync(this, this)
            }
            .show()
    }
}