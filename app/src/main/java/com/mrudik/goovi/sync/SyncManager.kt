package com.mrudik.goovi.sync

import android.content.Context
import androidx.work.*
import com.mrudik.goovi.Const

class SyncManager(private val context: Context) {

    fun sync() {
        if (!isPlayerExists(Const.GRETZKY_PLAYER_ID)) {
            WorkManager
                .getInstance(context)
                .beginWith(getOviStatWorkRequest())
                .then(getGretzkyStatWorkRequest())
                .enqueue()
        } else {
            WorkManager.getInstance(context).enqueue(getOviStatWorkRequest())
        }
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

    private fun getOviStatWorkRequest() : OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(LoadStatWorker::class.java)
            .setConstraints(getConstraints())
            .setInputData(getInputData(Const.OVECHKIN_PLAYER_ID))
            .build()
    }

    private fun getGretzkyStatWorkRequest() : OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(LoadStatWorker::class.java)
            .setConstraints(getConstraints())
            .setInputData(getInputData(Const.GRETZKY_PLAYER_ID))
            .build()
    }

    private fun isPlayerExists(playerId: Int) : Boolean {
        return false
        TODO("Store in SharedPreferences flag that tells that data exists or not")
    }
}