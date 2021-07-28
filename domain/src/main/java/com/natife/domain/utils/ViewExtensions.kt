package com.natife.domain.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnack(
    message: String,
    action: String = "",
    actionListener: () -> Unit = {}
): Snackbar {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    if (action != "") {
        snackBar.duration = Snackbar.LENGTH_INDEFINITE
        snackBar.setAction(action) {
            actionListener()
            snackBar.dismiss()
        }
    }
    snackBar.show()
    return snackBar
}
