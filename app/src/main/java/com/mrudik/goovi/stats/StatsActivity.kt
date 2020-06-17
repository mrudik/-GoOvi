package com.mrudik.goovi.stats

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrudik.goovi.App
import com.mrudik.goovi.Const
import com.mrudik.goovi.R
import com.mrudik.goovi.stats.adapter.StatPerYearAdapter
import com.mrudik.goovi.stats.adapter.StatPerYearItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_stats.*
import javax.inject.Inject

@AndroidEntryPoint
class StatsActivity : AppCompatActivity(), StatsContract.View {
    companion object {
        const val KEY_PLAYED_ID = "playedIdKey"
    }

    @Inject
    lateinit var presenter: StatsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        val playerId = intent.getIntExtra(KEY_PLAYED_ID, Const.OVECHKIN_PLAYER_ID)
        setProperTheme(playerId)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        if (playerId == Const.GRETZKY_PLAYER_ID) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        presenter.takeView(this)
        presenter.start(playerId)
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
        setGoalsDescriptionWayneGretzkyAsLink(SpannableString(description))
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

    private fun setGoalsDescriptionWayneGretzkyAsLink(spannableString: SpannableString) {
        val spanStartIndex = spannableString.length - 13
        val spanEndIndex = spannableString.length

        // Clickable Span
        spannableString.setSpan(
            object: ClickableSpan() {
                override fun onClick(widget: View) {
                    // Open Wayne Gretzky Stat
                    val intent = Intent(this@StatsActivity, StatsActivity::class.java)
                    intent.putExtra(KEY_PLAYED_ID, Const.GRETZKY_PLAYER_ID)
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            },
            spanStartIndex,
            spanEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Color Span
        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.colorPrimaryGretzky)),
            spanStartIndex,
            spanEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textViewGoalsDescription.text = spannableString
        textViewGoalsDescription.movementMethod = LinkMovementMethod.getInstance()
    }
}