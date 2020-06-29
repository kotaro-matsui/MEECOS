package com.example.meecos.Fragment.Base

import androidx.fragment.app.Fragment
import com.example.meecos.Activity.MainActivity

open class BaseFragment: Fragment() {

    fun setTitle (title: String) {
        val activity = activity as MainActivity
        activity.setTitle(title)
    }
}