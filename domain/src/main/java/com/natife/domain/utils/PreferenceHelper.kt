package com.natife.domain.utils

import android.content.SharedPreferences
import androidx.core.content.edit

object PreferenceHelper {
    private const val PREFERENCE_SAVE_LOG_IN = "checkLogin"
    private const val PREFERENCE_SAVE_NAME = "nickname"

    var SharedPreferences.savedLogIn
        get() = getBoolean(PREFERENCE_SAVE_LOG_IN, false)
        set(value) {
            edit {
                this.putBoolean(PREFERENCE_SAVE_LOG_IN, value)
            }
        }
    var SharedPreferences.savedNickname
        get() = getString(PREFERENCE_SAVE_NAME, "")
        set(value) {
            edit {
                this.putString(PREFERENCE_SAVE_NAME, value)
            }
        }
}
