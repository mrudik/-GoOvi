package com.mrudik.goovi.service

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import com.mrudik.goovi.Const
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mrudik.goovi.api.ApiService
import java.lang.Exception

class LoadOviStatWorker @WorkerInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val apiService: ApiService) : Worker(context, workerParams) {

    companion object {
        const val TAG = "WorkerLoadOviStat"
    }

    override fun doWork(): Result {
        Log.d(TAG, "start doWork()")
        try {
            val fullStat = apiService.loadPlayerStatsTest(Const.OVECHKIN_PLAYER_ID)
            Log.d(TAG, "success doWork()")
            return Result.success()
        } catch (ex: Exception) {
            Log.d(TAG, "failure doWork()")
            return Result.failure()
        }
    }
}