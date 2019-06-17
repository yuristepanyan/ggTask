package com.gg.task.di.module

import com.gg.task.di.ServiceScope
import com.google.android.gms.location.LocationRequest
import dagger.Module
import dagger.Provides

@Module
class LocationServiceModule {

    @Provides
    @ServiceScope
    fun provideLocationRequest() = LocationRequest().apply {
        interval = 800
        fastestInterval = 500
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}