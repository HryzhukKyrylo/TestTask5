package com.natife.testtask5.util

import android.content.SharedPreferences
import androidx.core.content.edit

object PreferenceHelper {
    private const val PREFERENCE_CONST = "checkLogin"
    private const val PREFERENCE_NAME = "nickname"

    var SharedPreferences.checkLogin
        get() = getBoolean(PREFERENCE_CONST, false)
        set(value) {
            edit {
                this.putBoolean(PREFERENCE_CONST, value)
            }
        }
    var SharedPreferences.savedNickname
        get() = getString(PREFERENCE_NAME, "")
        set(value) {
            edit {
                this.putString(PREFERENCE_NAME, value)
            }
        }
}
