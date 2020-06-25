package com.mrudik.goovi.helper.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import com.mrudik.goovi.R
import com.mrudik.goovi.getThemeColor
import java.io.OutputStream

class Screenshot(val context: Context) {

    interface Callback {
        fun onImageReady(uri: Uri)
    }

    fun take(view: View, window: Window, callback: Callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createBitmap(view, window, callback)
        } else {
            createBitmap(view, callback)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createBitmap(view: View, window: Window, callback: Callback) {
        window.let {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)

            try {
                PixelCopy.request(
                    window,
                    Rect(
                        locationOfViewInWindow[0],
                        locationOfViewInWindow[1],
                        locationOfViewInWindow[0] + view.width,
                        locationOfViewInWindow[1] + view.height
                    ),
                    bitmap,
                    {
                        if (it == PixelCopy.SUCCESS) {
                            saveToFile(bitmap, callback)
                        }
                    },
                    Handler()
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun createBitmap(view: View, callback: Callback) {
        val screenView = view.rootView
        screenView.buildDrawingCache(true)
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false

        saveToFile(bitmap, callback)
    }

    private fun saveToFile(bitmap: Bitmap, callback: Callback) {
        addWatermark(bitmap)

        val cv = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "GoOvi_" + System.currentTimeMillis() + ".png")
            put(MediaStore.Images.Media.TITLE, "title")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        var out: OutputStream? = null
        try {
            out = context.contentResolver.openOutputStream(uri!!)
            val result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            if (result) {
                callback.onImageReady(uri)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            out?.close()
        }
    }

    private fun addWatermark(bitmap: Bitmap) {
        val watermark = "#GoOvi"
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            color = 0xFFC8102E.toInt()
            alpha = 255
            textSize = context.resources.getDimensionPixelSize(R.dimen.watermark_text_size).toFloat()
            isAntiAlias = true
        }

        val rect = Rect()
        paint.getTextBounds(watermark, 0, watermark.length, rect)

        canvas.drawText(
            watermark,
            bitmap.width - rect.width() * 1F - 100,
            rect.height() * 1F + 100,
            paint
        )
    }
}