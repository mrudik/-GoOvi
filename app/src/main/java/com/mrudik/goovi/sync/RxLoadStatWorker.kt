package com.mrudik.goovi.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.mrudik.goovi.Const
import com.mrudik.goovi.api.ApiService
import io.reactivex.Single

class RxLoadStatWorker @WorkerInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: ApiService,
    private val dbHelper: SyncStatDBHelper,
    private val preferences: SharedPreferences) : RxWorker(context, workerParams) {

    companion object {
        const val KEY_PLAYER_ID = "playerIdKey"
    }

    override fun createWork(): Single<Result> {
        val playerId = inputData.getInt(KEY_PLAYER_ID, 0)

        return apiService.loadPlayerStatsSingle(playerId)
            .map {
                val isSuccess = dbHelper.parseAndInsertToDatabase(playerId, it)
                if (isSuccess) {
                    updatePreferences(playerId.toString())
                    Result.success()
                } else {
                    Result.failure()
                }
            }
    }

    private fun updatePreferences(playerId: String) {
        val editor = preferences.edit()

        // Player
        val playerSet = preferences.getStringSet(Const.SHARED_PREFERENCES_KEY_PLAYERS, HashSet<String>())
        if (!playerSet!!.contains(playerId)) {
            playerSet.add(playerId)
            editor.putStringSet(Const.SHARED_PREFERENCES_KEY_PLAYERS, playerSet)
        }

        // Sync Time
        editor.putLong(Const.SHARED_PREFERENCES_KEY_LAST_SYNC_TIME, System.currentTimeMillis())
        editor.apply()
    }
}