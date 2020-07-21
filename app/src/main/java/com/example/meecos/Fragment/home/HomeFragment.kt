package com.example.meecos.Fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Config.WEATHER_IMAGE_URL
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Manager.WeatherManager
import com.example.meecos.Manager.ImageGetTask
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import com.example.meecos.Fragment.Customer.CustomerFragment
import com.example.meecos.Fragment.Meeting.MeetingRecordFragment

class HomeFragment : BaseFragment() {

    //　戻るボタンを制御するための真偽地
    // trueなら戻る、falseなら戻らない
    var backSwitch: Boolean = true

    companion object {
        fun newInstance(bs: Boolean): HomeFragment {
            val fragment = HomeFragment()
            fragment.backSwitch = bs
            return fragment
        }
    }

    var mZipText: TextView? = null
    var mWeatherImage: ImageView? = null
    var mTemperatureText: TextView? = null
    var mProgress: ProgressBar? = null

    private var mRecordingButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.setBackEvent(onBackListener)
        setTitle("MEECOS")

        this.mZipText = view.findViewById(R.id.zip_text)
        this.mWeatherImage = view.findViewById(R.id.weather_image)
        this.mTemperatureText = view.findViewById(R.id.temperature_text)
        this.mProgress = view.findViewById(R.id.progress)
        this.mProgress!!.visibility = View.VISIBLE

        this.mRecordingButton = view.findViewById(R.id.recording_button)
        this.mRecordingButton!!.setOnClickListener(recordingButtonClickListener)

        setWeatherDetails()
        return view
    }

    /**
     * 天気情報をセット
     */
    private fun setWeatherDetails () {
        val activity = activity as MainActivity
        val weatherManager = WeatherManager(activity)

        if (weatherManager.checkPermission()) {
            weatherManager.getLocation(activity, object : WeatherManager.OnCompleteListener {
                override fun onCompleteWeather(
                    success: Boolean,
                    zipStr: String,
                    imageStr: String,
                    temperatureStr: String
                ) {
                    if (success) {
                        Handler(Looper.getMainLooper()).post {
                            this@HomeFragment.mZipText!!.text = zipStr
                            this@HomeFragment.mTemperatureText!!.text = temperatureStr
                            this@HomeFragment.mProgress!!.visibility = View.GONE
                        }
                        val task = ImageGetTask(this@HomeFragment.mWeatherImage!!)
                        task.execute(WEATHER_IMAGE_URL + imageStr)
                    }
                }
            })
        }
    }

    /**
     * 議事録画面遷移
     */
    private val recordingButtonClickListener = View.OnClickListener {
        replaceFragment(MeetingRecordFragment())
    }

    // 現状、前画面での戻るボタンのイベントを、遷移先であるここで拾ってしまうため、登録画面→HOME画面のような遷移が起こる
    //　実機でこの問題が起こらない場合は『replaceFragment(HomeFragment())』の1文でよい
    private val onBackListener = object : BackEventListener {

        override fun onBackClick() {
            if (backSwitch) {
                activity!!.finish()
            } else {
                backSwitch = true
            }
        }
    }
}