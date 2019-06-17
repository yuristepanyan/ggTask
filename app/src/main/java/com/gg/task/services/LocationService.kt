package com.gg.task.services

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gg.task.GgTask
import com.gg.task.firestore.FirestoreHelper
import com.google.android.gms.location.*
import dagger.android.DaggerService
import javax.inject.Inject

private const val NOTIFICATION_ID = 12345
private const val CHANNEL_ID = "ggTest"

class LocationService : DaggerService(), android.location.LocationListener {
    @Inject
    lateinit var firestoreHelper: FirestoreHelper
    @Inject
    lateinit var locationRequest: LocationRequest

    private var lastLocation: Location? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground()
        }
        if (GgTask.checkPlayServices()) {
            initFusedLocation()
        } else {
            initNativeLocationService()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "ggTest", NotificationManager.IMPORTANCE_DEFAULT
        )
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("")
            .setContentText("").build()
        startForeground(1, notification)
        startForeground(NOTIFICATION_ID, notification)
    }

    @SuppressLint("MissingPermission")
    private fun initNativeLocationService() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 800.toLong(), 5f, this as android.location.LocationListener
        )
    }

    private fun initFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initLocationCallback()
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun initLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    onLocationChanged(location)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            if((lastLocation?.distanceTo(location) ?: 1f) >= 1f) {
                lastLocation = location
                firestoreHelper.saveLocation(it)
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
}
