package com.example.meecos.Fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Dialog.ProgressBarFragment
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Manager.WeatherManager
import org.json.JSONObject

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        setTitle("MEECOS")
        sssss()
        return view
    }

    // TODO: プログレス　GPS　試験用メソッド
    fun sssss () {
        val activity = activity as MainActivity
        val weatherManager = WeatherManager(activity)
        if (weatherManager.checkPermission()) {
            var progress = ProgressBarFragment().newInstance(activity)
            progress.showDialog()

            weatherManager.getLocation(activity, object : WeatherManager.OnCompleteListener {
                override fun onCompleteWeather(success: Boolean, json: JSONObject) {
                    progress.dismissDialog()
                    if (success) {

                    }
                }
            })
        }
    }
}