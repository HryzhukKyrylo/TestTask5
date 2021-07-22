package com.natife.testtask5.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.showSnack(
    message: String,
    action: String = "",
    actionListener: () -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    if (action != "") {
        snackbar.duration = Snackbar.LENGTH_INDEFINITE
        snackbar.setAction(action) {
            actionListener()
            snackbar.dismiss()
        }
    }
    snackbar.show()
    return snackbar
}
