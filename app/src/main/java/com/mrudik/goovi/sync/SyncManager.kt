package com.mrudik.goovi.sync

import android.content.Context
import androidx.work.*
import com.mrudik.goovi.Const

class SyncManager(private val context: Context) {
    fun sync() {
        val loadOviStatWorkRequest = OneTimeWorkRequest.Builder(LoadStatWorker::class.java)
            .setConstraints(getConstraints())
            .setInputData(getInputData(Const.OVECHKIN_PLAYER_ID))
            .build()

        val loadGretzkyStatWorkRequest = OneTimeWorkRequest.Builder(LoadStatWorker::class.java)
            .setConstraints(getConstraints())
            .setInputData(getInputData(Const.GRETZKY_PLAYER_ID))
            .build()

        WorkManager
            .getInstance(context)
            .beginWith(loadOviStatWorkRequest)
            .then(loadGretzkyStatWorkRequest)
            .enqueue()
    }

    private fun getInputData(playerId: Int) : Data {
        return Data.Builder()
            .putInt(LoadStatWorker.KEY_PLAYER_ID, playerId)
            .build()
    }

    private fun getConstraints() : Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}