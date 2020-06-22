package com.mrudik.goovi

open class Const {
    companion object {
        const val BASE_URL = "https://statsapi.web.nhl.com/api/v1/"
        const val OVECHKIN_PLAYER_ID = 8471214
        const val GRETZKY_PLAYER_ID = 8447400
        const val NHL_LEAGUE_ID = 133
        const val NHL_LEAGUE_NAME = "NHL"

        const val DATABASE_NAME = "Database"
        const val DATABASE_VERSION = 1

        const val SHARED_PREFERENCES_NAME = "Preferences"
        const val SHARED_PREFERENCES_KEY_PLAYERS = "Players"
        const val SHARED_PREFERENCES_KEY_LAST_SYNC_TIME = "LastSyncTime"
    }
}