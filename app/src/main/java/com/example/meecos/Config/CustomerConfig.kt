package com.example.meecos.Config

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * キーボードを閉じる処理
 */
fun closeSoftKeyboard(view: View, context: Context) {
    val inputManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}


