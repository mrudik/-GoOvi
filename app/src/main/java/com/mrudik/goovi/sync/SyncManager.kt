package com.mrudik.goovi.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.*
import com.mrudik.goovi.Const
import com.mrudik.goovi.helper.Helper
import java.util.*

class SyncManager(
    private val context: Context,
    private val preferences: SharedPreferences,
    private val helper: Helper) {

    private lateinit var workManager: WorkManager
    private var lifecycleOwner : LifecycleOwner? = null
    private var syncStatus: SyncStatus? = null

    interface SyncStatus {
        fun syncSucceeded()
        fun syncFailed()
    }

    fun sync(lifecycleOwner : LifecycleOwner, syncStatus: SyncStatus) {
        this.workManager = WorkManager.getInstance(context)
        this.lifecycleOwner = lifecycleOwner
        this.syncStatus = syncStatus

        if (helper.isNetworkConnected()) {
            enqueue()
        } else {
            callDefaultSyncStatus()
        }
    }

    fun stopSync() {
        workManager.cancelAllWork()
        lifecycleOwner = null
        syncStatus = null
    }

    private fun enqueue() {
        val oviStatWorkRequest = getOviStatWorkRequest()
        var workIdToObserve: UUID = oviStatWorkRequest.id

        if (!isPlayerExists(Const.GRETZKY_PLAYER_ID)) {
            val gretzkyStatWorkRequest = getGretzkyStatWorkRequest()
            workIdToObserve = gretzkyStatWorkRequest.id

            workManager
                .beginWith(oviStatWorkRequest)
                .then(gretzkyStatWorkRequest)
                .enqueue()

        } else {
            workManager.enqueue(oviStatWorkRequest)
        }

        observeWorkInfo(
            lifecycleOwner!!,
            workIdToObserve,
            syncStatus!!
        )
    }

    private fun callDefaultSyncStatus() {
        if (isPlayerExists(Const.OVECHKIN_PLAYER_ID) && isPlayerExists(Const.GRETZKY_PLAYER_ID)) {
            syncStatus?.syncSucceeded()
        } else {
            syncStatus?.syncFailed()
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

    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun observeWorkInfo(
        lifecycleOwner : LifecycleOwner,
        workRequestId: UUID,
        syncStatus: SyncStatus) {

        workManager.getWorkInfoByIdLiveData(workRequestId)
            .observe(lifecycleOwner, Observer {
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> syncStatus.syncSucceeded()
                    WorkInfo.State.FAILED -> syncStatus.syncFailed()
                }
            })
    }
}