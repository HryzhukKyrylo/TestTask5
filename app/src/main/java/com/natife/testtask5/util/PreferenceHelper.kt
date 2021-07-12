package com.natife.testtask5.util

import android.content.Context
import android.content.SharedPreferences


object PreferenceHelper {
    private const val PREFERENCE_CONST = "const"

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe( operation: (SharedPreferences.Editor)-> Unit){
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.checkLogin
        get() = getBoolean(PREFERENCE_CONST, false)
        set(value) {
            editMe {
                it.putBoolean(PREFERENCE_CONST, value)
            }
        }

}
