package com.mrudik.goovi.ui.stats

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrudik.goovi.Const
import com.mrudik.goovi.R
import com.mrudik.goovi.getThemeColor
import com.mrudik.goovi.helper.ui.Screenshot
import com.mrudik.goovi.helper.ui.SingleClickSpan
import com.mrudik.goovi.ui.stats.adapter.StatPerYearAdapter
import com.mrudik.goovi.ui.stats.adapter.StatPerYearItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_stats.*
import javax.inject.Inject

@AndroidEntryPoint
class StatsActivity : AppCompatActivity(), StatsContract.View {
    companion object {
        const val KEY_PLAYED_ID = "playedIdKey"
        const val REQUEST_PERMISSION_STORAGE = 1000
    }

    @Inject
    lateinit var presenter: StatsContract.Presenter
    @Inject
    lateinit var screenshot: Screenshot

    override fun onCreate(savedInstanceState: Bundle?) {
        val playerId = intent.getIntExtra(KEY_PLAYED_ID, Const.OVECHKIN_PLAYER_ID)
        setProperTheme(playerId)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        if (playerId == Const.GRETZKY_PLAYER_ID) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        imageViewPhoto.setOnClickListener {
            if (isStoragePermissionGranted()) {
                takeScreenshot()
            }
        }

        presenter.takeView(this)
        presenter.start(playerId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clearView()
    }

    private fun isStoragePermissionGranted() : Boolean {
        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        val isPermissionGranted = checkCallingPermission(permission) == PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_PERMISSION_STORAGE
            )
        }
        return isPermissionGranted
    }

    private fun takeScreenshot() {
        screenshot.take(nestedScrollView, window, object : Screenshot.Callback {
            override fun onImageReady(uri: Uri) {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                }
                startActivity(
                    Intent.createChooser(
                        shareIntent,
                        "Share image"
                    )
                )
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_PERMISSION_STORAGE) {
                takeScreenshot()
            }
        }
    }

    private fun setProperTheme(playerId: Int) {
        when (playerId) {
            Const.OVECHKIN_PLAYER_ID -> setTheme(R.style.OviTheme)
            Const.GRETZKY_PLAYER_ID -> setTheme(R.style.GretzkyTheme)
        }
    }

    override fun setScreenTitle(title: String) {
        setTitle(title)
    }

    override fun showError(message: String) {
        Snackbar.make(nestedScrollView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showTotalGoals(goals: Int) {
        textViewTotalGoals.text = goals.toString()
    }

    override fun showGoalsDescription(description: String) {
        textViewGoalsDescription.text = description
    }

    override fun showGoalsDescriptionWithGretzkySpan(description: String) {
        val spannableString = SpannableString(description)
        setGoalsDescriptionWithSpan(
            spannableString,
            spannableString.length - 13,
            spannableString.length
        )
    }

    override fun showGoalsDescriptionWithOvechkinSpan(description: String) {
        val spannableString = SpannableString(description)
        setGoalsDescriptionWithSpan(
            spannableString,
            spannableString.length - 13,
            spannableString.length
        )
    }

    override fun showStatPerYear(statPerYearList: ArrayList<StatPerYearItem>) {
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this@StatsActivity)

            val itemDecoration = DividerItemDecoration(this@StatsActivity, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)
            addItemDecoration(itemDecoration)
            adapter = StatPerYearAdapter(this@StatsActivity, statPerYearList)
        }
    }

    override fun showCopyright(copyright: String) {
        textViewCopyright.text = copyright
    }

    override fun showStatPerYearNotAvailableError() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setGoalsDescriptionWithSpan(
        spannableString: SpannableString,
        startIndex: Int,
        endIndex: Int) {

        // Clickable Span
        spannableString.setSpan(
            object: SingleClickSpan() {
                override fun singleClick(widget: View) {
                    presenter.playerNameClickAction()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            },
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Color Span
        spannableString.setSpan(
            ForegroundColorSpan(getThemeColor(R.attr.colorOppositeTheme)),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textViewGoalsDescription.text = spannableString
        textViewGoalsDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun showGretzkyScreen() {
        val intent = Intent(this@StatsActivity, StatsActivity::class.java)
        intent.putExtra(KEY_PLAYED_ID, Const.GRETZKY_PLAYER_ID)
        startActivity(intent)
    }

    override fun closeScreen() {
        finish()
    }
}