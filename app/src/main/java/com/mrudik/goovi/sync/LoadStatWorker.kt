package com.mrudik.goovi.sync

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mrudik.goovi.sync.repository.SyncStatRepository

class LoadStatWorker @WorkerInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val syncStatRepository: SyncStatRepository
) : Worker(context, workerParams) {

    companion object {
        const val TAG = "WorkerLoadOviStat"
        const val KEY_PLAYER_ID = "playerIdKey"
    }

    override fun doWork(): Result {
        Log.d(TAG, "start doWork()")
        try {
            val playerId = inputData.getInt(KEY_PLAYER_ID, 0)
            if (playerId != 0) {
                syncStatRepository.loadFullStat(playerId)
                return Result.success()
            }
            return Result.failure()
        } catch (ex: Exception) {
            Log.d(TAG, "failure doWork()")
            return Result.failure()
        }
    }
}