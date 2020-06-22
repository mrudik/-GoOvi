package com.mrudik.goovi.ui.helper

import android.os.SystemClock
import android.text.style.ClickableSpan
import android.view.View

abstract class SingleClickSpan : ClickableSpan() {
    private var lastClickTime: Long = 0

    companion object {
        const val debounceTime = 1000L
    }

    override fun onClick(widget: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()

        singleClick(widget)
    }

    abstract fun singleClick(widget: View)
}