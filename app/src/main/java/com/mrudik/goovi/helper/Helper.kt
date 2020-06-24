package com.mrudik.goovi.helper

import android.content.Context
import android.net.ConnectivityManager

class Helper(val context: Context) {
    @Suppress("DEPRECATION")
    fun isNetworkConnected() : Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo?.isConnected ?: false
    }
}