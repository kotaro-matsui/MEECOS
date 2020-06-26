package com.example.meecos.Manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.meecos.Config.BASE_WEATHER_URL
import com.example.meecos.Config.WEATHER_APPID
import com.google.android.gms.location.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class WeatherManager(val activity: Activity) {

    private val LOCATION_REQUEST_CODE = 1

    /**
     * On Complete Listener
     */
    interface OnCompleteListener {
        fun onComplete(
            success: Boolean,
            latitude: Double?,
            longitude: Double?
        ) {}

        fun onCompleteWeather(
            success: Boolean,
            json: JSONObject
        ) {}
    }

    interface OnCompleteWeatherListener {
        fun onComplete(
            success: Boolean,
            json: JSONObject
        ) {}
    }

    /**
     * GPS パーミッションチェック
     */
    fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            return true
        }

        return isGPSEnabled()
    }

    /**
     * GPS 有効チェック
     */
    private fun isGPSEnabled(): Boolean {
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 経度 緯度 取得
     */
    fun getLocation(context: Context, listener: OnCompleteListener) {
        val fusedLocationClient = FusedLocationProviderClient(context)

        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult?.lastLocation == null) {
                    listener.onComplete(false, null, null)
                }
                val location = locationResult?.lastLocation

                fusedLocationClient.removeLocationUpdates(this)

                if (location != null) {
                    getWeather(location, object : OnCompleteWeatherListener {
                        override fun onComplete(success: Boolean, json: JSONObject) {

                            // 試験用
                            listener.onCompleteWeather(
                                true,
                                json
                            )
                        }
                    })
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun getWeather(locattion: Location, listener: OnCompleteWeatherListener) {
        Thread {
            val requestURL = BASE_WEATHER_URL + "weather?lat=" +
                    locattion.latitude + "8&lon=" +
                    locattion.longitude + WEATHER_APPID

            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            val buffer: StringBuffer

            val url = URL(requestURL)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.connect()

            val stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream))
            buffer = StringBuffer()
            var line: String?
            while (true) {
                line = reader.readLine()
                if (line == null) {
                    break
                }
                buffer.append(line)
            }
            val jsonText = buffer.toString()
            val jsonObject = JSONObject(jsonText)

            // 例
            val _jsonObject = jsonObject.getJSONObject("coord")

            listener.onComplete(true, jsonObject)
        }.start()
    }
}

// TODO: 下記JSONサンプル
//{
//    "coord": {
//    "lon":135.49,
//    "lat":34.7
//    },
//    "weather":[
//    {
//        "id":****,
//        "main":"Clouds",
//        "description":"brokenclouds",
//        "icon":"04d"
//    }
//    ],
//    "base":"stations",
//    "main"{
//        "temp":303.62,
//        "feels_like":308.07,
//        "temp_min":302.15,
//        "temp_max":304.82,
//        "pressure":1004,
//        "humidity":79
//    },
//    "visibility":10000,
//    "wind":{
//    "speed":4.1,
//    "deg":220
//    },
//    "clouds":{
//    "all":75
//    },
//    "dt":1593152629,
//    "sys"{
//        "type":1,
//        "id":8032,
//        "country":"JP",
//        "sunrise":1593114394,
//        "sunset":1593166501
//    },
//    "timezone":32400,
//    "id":****,
//    "name":"Osaka",
//    "cod":200
//}