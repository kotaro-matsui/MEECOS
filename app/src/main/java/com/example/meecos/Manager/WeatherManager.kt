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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException


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
            zipStr: String,
            imageStr: String,
            temperatureStr: String
        ) {}
    }

    interface OnCompleteWeatherListener {
        fun onComplete(
            success: Boolean,
            zipStr: String,
            imageStr: String,
            temperatureStr: String
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
                        override fun onComplete(success: Boolean, zipStr: String, imageStr: String, temperatureStr: String) {
                            // 試験用
                            listener.onCompleteWeather(
                                true,
                                zipStr,
                                imageStr,
                                temperatureStr
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

            // 地域名
            val zipName = jsonObject.getString("name")

            // 天気アイコンファイル名
            val weatherJsonArray = jsonObject.getJSONArray("weather")
            val weatherJson = weatherJsonArray.getJSONObject(0)
            val imageName = weatherJson.get("icon").toString()


            val temperatureJson = jsonObject.getJSONObject("main")
            val temperature = temperatureJson.getDouble("temp") - 273.15

            listener.onComplete(
                true,
                zipName,
                "$imageName.png",
                temperature.toInt().toString() + "℃")
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
//        "icon":"04d"                                              // 天気画像　例)　http://openweathermap.org/img/w/04d.png
//    }
//    ],
//    "base":"stations",
//    "main"{
//        "temp":303.62,                                            // 気温 (- 273.15)
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
//        "id":******,
//        "country":"JP",
//        "sunrise":1593114394,
//        "sunset":1593166501
//    },
//    "timezone":32400,
//    "id":****,
//    "name":"Osaka",                                               // 場所(例は大阪)
//    "cod":200
//}

internal class ImageGetTask(private val image: ImageView) : AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg params: String): Bitmap? {
        val image: Bitmap
        try {
            val imageUrl = URL(params[0])
            val imageIs: InputStream
            imageIs = imageUrl.openStream()
            image = BitmapFactory.decodeStream(imageIs)
            return image
        } catch (e: MalformedURLException) {
            return null
        } catch (e: IOException) {
            return null
        }

    }

    override fun onPostExecute(result: Bitmap) {
        image.setImageBitmap(result)
    }
}