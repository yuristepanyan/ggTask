package com.gg.task

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import java.util.*

class GgTask : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerGgTaskComponent.builder()
            .application(this)
            .build()
        component.inject(this)

        setId()

        return component
    }

    private fun setId() {
        if(component.getAuthentication().getId() == null) {
            component.getAuthentication().setId(UUID.randomUUID().toString())
        }
    }

    companion object {
        lateinit var component: GgTaskComponent

        fun checkPlayServices(): Boolean {
            val apiAvailability = GoogleApiAvailability.getInstance()
            val resultCode = apiAvailability.isGooglePlayServicesAvailable(component.getContext())

            if (resultCode != ConnectionResult.SUCCESS) {
                return false
            }

            return true
        }
    }
}