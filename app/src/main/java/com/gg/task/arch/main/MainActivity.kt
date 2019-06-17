package com.gg.task.arch.main

import android.animation.LayoutTransition
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gg.task.GgTask
import com.gg.task.R
import com.gg.task.extensions.intentTo
import com.gg.task.services.LocationService
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101
private const val CONNECTION_CODE = 102

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: MainViewModel

    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        bind()
        getLocationPermission()
        container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        enable.setOnClickListener { getLocationPermission() }
    }

    private fun bind() {
        viewModel.locationData.observe(this, Observer {
            it?.let { positionChecked ->
                if(location.visibility != VISIBLE) {
                    location.visibility = VISIBLE
                }
                location.text = getString(R.string.location, positionChecked.lat, positionChecked.lng)
            }
        })
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            enableGps()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    enableGps()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONNECTION_CODE && resultCode == Activity.RESULT_OK) {
            locationEnabled()
        }
    }

    private fun locationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intentTo(LocationService::class.java))
        } else {
            startService(intentTo(LocationService::class.java))
        }
    }

    private fun enableGps() {
        permissionMessageSection.visibility = GONE
        if (!GgTask.checkPlayServices()) {
            locationEnabled()
            return
        }
        if (!locationPermissionGranted) {
            return
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest())
        builder.setNeedBle(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
                locationEnabled()
            } catch (exception: ApiException) {
                if (exception.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, CONNECTION_CODE)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    } catch (e: ClassCastException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}
