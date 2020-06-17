package com.mrudik.goovi.stats.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrudik.goovi.R
import com.mrudik.goovi.getThemeColor

class StatPerYearAdapter(
    private val context: Context,
    private val statPerYearItemList: ArrayList<StatPerYearItem>): RecyclerView.Adapter<StatPerYearAdapter.VHStatPerYear>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHStatPerYear {
        val inflater = LayoutInflater.from(context)
        return VHStatPerYear(inflater, parent)
    }

    override fun getItemCount(): Int {
        return statPerYearItemList.size
    }

    override fun onBindViewHolder(holder: VHStatPerYear, position: Int) {
        val item = statPerYearItemList[position]
        holder.bind(item)
        if (position == 0) {
            holder.setTypeface(Typeface.BOLD)
            holder.setTextColor(context.getThemeColor(R.attr.colorPrimaryTheme))
        } else {
            holder.setTypeface(Typeface.NORMAL)
            holder.setTextColor(context.resources.getColor(R.color.colorTextPrimary, context.theme))
        }
    }

    class VHStatPerYear(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_stat_per_year, parent,false)) {

        private var textViewSeason: TextView? = null
        private var textViewGamesPlayed: TextView? = null
        private var textViewGoals: TextView? = null
        private var textViewAssists: TextView? = null
        private var textViewPoints: TextView? = null

        init {
            textViewSeason = itemView.findViewById(R.id.textViewSeason)
            textViewGamesPlayed = itemView.findViewById(R.id.textViewGamePlayed)
            textViewGoals = itemView.findViewById(R.id.textViewGoals)
            textViewAssists = itemView.findViewById(R.id.textViewAssists)
            textViewPoints = itemView.findViewById(R.id.textViewPoints)
        }

        fun bind(item: StatPerYearItem) {
            textViewSeason?.text = item.season
            textViewGamesPlayed?.text = item.gamesPlayed
            textViewGoals?.text = item.goals
            textViewAssists?.text = item.assists
            textViewPoints?.text = item.points
        }

        fun setTypeface(typeface: Int) {
            textViewSeason?.setTypeface(textViewSeason?.typeface, typeface)
            textViewGamesPlayed?.setTypeface(textViewGamesPlayed?.typeface, typeface)
            textViewGoals?.setTypeface(textViewGoals?.typeface, typeface)
            textViewAssists?.setTypeface(textViewAssists?.typeface, typeface)
            textViewPoints?.setTypeface(textViewPoints?.typeface, typeface)
        }

        fun setTextColor(color: Int) {
            textViewSeason?.setTextColor(color)
            textViewGamesPlayed?.setTextColor(color)
            textViewGoals?.setTextColor(color)
            textViewAssists?.setTextColor(color)
            textViewPoints?.setTextColor(color)
        }
    }
}