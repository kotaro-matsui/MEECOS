package com.example.meecos.Fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Config.WEATHER_IMAGE_URL
import com.example.meecos.Dialog.ProgressBarFragment
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Manager.WeatherManager
import com.example.meecos.Manager.ImageGetTask
import android.os.Handler
import android.os.Looper

class HomeFragment : BaseFragment() {

    var mZipText: TextView? = null
    var mWeatherImage: ImageView? = null
    var mTemperatureText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        setTitle("MEECOS")

        mZipText = view.findViewById(R.id.zip_text)
        mWeatherImage = view.findViewById(R.id.weather_image)
        mTemperatureText = view.findViewById(R.id.temperature_text)

        setWeatherDetails()
        return view
    }

    /**
     * 天気情報をセット
     */
    fun setWeatherDetails () {
        val activity = activity as MainActivity
        val weatherManager = WeatherManager(activity)
        if (weatherManager.checkPermission()) {
            var progress = ProgressBarFragment().newInstance(activity)
            progress.showDialog()

            weatherManager.getLocation(activity, object : WeatherManager.OnCompleteListener {
                override fun onCompleteWeather(
                    success: Boolean,
                    zipStr: String,
                    imageStr: String,
                    temperatureStr: String
                ) {
                    progress.dismissDialog()
                    if (success) {
                        Handler(Looper.getMainLooper()).post {
                            this@HomeFragment.mZipText!!.text = zipStr
                            this@HomeFragment.mTemperatureText!!.text = temperatureStr
                        }
                        val task = ImageGetTask(this@HomeFragment.mWeatherImage!!)
                        task.execute(WEATHER_IMAGE_URL + imageStr)
                    }
                }
            })
        }
    }
}