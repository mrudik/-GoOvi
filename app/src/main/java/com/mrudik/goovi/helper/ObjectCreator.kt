package com.mrudik.goovi.helper

import com.mrudik.goovi.db.entity.DBPlayerStat
import com.mrudik.goovi.ui.stats.adapter.StatPerYearItem

class ObjectCreator {
    fun createStatPerYearItemArrayList() : ArrayList<StatPerYearItem> {
        return ArrayList()
    }

    fun createStatPerYearItem() : StatPerYearItem {
        return StatPerYearItem()
    }

    fun createStatPerYearItem(dbPlayerStat: DBPlayerStat) : StatPerYearItem {
        return StatPerYearItem(dbPlayerStat)
    }
}