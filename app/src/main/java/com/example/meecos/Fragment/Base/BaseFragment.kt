package com.example.meecos.Fragment.Base

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Config.REQUEST_CODE
import java.util.*

open class BaseFragment: Fragment() {

    fun setTitle (title: String) {
        val activity = activity as MainActivity
        activity.setTitle(title)
    }

    fun  actionRecognizeSpeech() {
        var activity = activity as MainActivity
        activity.speech()
    }

    fun  setCallbackFragment(fragmnet: Fragment) {
        var activity = activity as MainActivity
        activity.setCallbackFragment(fragmnet)
    }
}