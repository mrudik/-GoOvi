package com.mrudik.goovi.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.work.*
import com.mrudik.goovi.Const

class SyncManager(
    private val context: Context,
    private val preferences: SharedPreferences) {

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
            .putInt(RxLoadStatWorker.KEY_PLAYER_ID, playerId)
            .build()
    }

    private fun getConstraints() : Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    private fun getOviStatWorkRequest() : OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(RxLoadStatWorker::class.java)
            .setConstraints(getConstraints())
            .setInputData(getInputData(Const.OVECHKIN_PLAYER_ID))
            .build()
    }

    private fun getGretzkyStatWorkRequest() : OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(RxLoadStatWorker::class.java)
            .setConstraints(getConstraints())
            .setInputData(getInputData(Const.GRETZKY_PLAYER_ID))
            .build()
    }

    private fun isPlayerExists(playerId: Int) : Boolean {
        val usersSet = preferences.getStringSet(Const.SHARED_PREFERENCES_KEY_PLAYERS, null)
        return usersSet?.contains(playerId.toString()) ?: false
    }
}