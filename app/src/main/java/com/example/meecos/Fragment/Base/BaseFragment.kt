package com.example.meecos.Fragment.Base

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.example.meecos.Activity.MainActivity

open class BaseFragment: Fragment() {

    fun setTitle (title: String) {
        val activity = activity as MainActivity
        activity.setTitle(title)
    }

    fun replaceFragment (fragment: Fragment) {
        val activity = activity as MainActivity
        activity.replaceFragment(fragment)
    }


    interface BackEventListener {
        fun onBackClick()
    }

    fun View.setBackEvent(listener: BackEventListener) {
        this.setOnKeyListener { _, keyCode, event ->
            (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN).apply {
                listener.onBackClick()
            }
        }
        this.isFocusableInTouchMode = true
        this.requestFocus()
    }
}