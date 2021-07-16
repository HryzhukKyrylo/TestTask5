package com.natife.testtask5.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 *@author admin
 */
fun Activity.hideSoftKeyboard(){
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
