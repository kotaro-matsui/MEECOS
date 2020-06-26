package com.example.meecos.Manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

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

                listener.onComplete(
                    true,
                    location!!.latitude, // 緯度
                    location!!.longitude // 経度
                )
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
}