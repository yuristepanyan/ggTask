package com.gg.task.di.module

import android.app.Service
import com.gg.task.di.component.LocationServiceComponent
import com.gg.task.services.LocationService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module
abstract class ServiceBuilder {

    @Binds
    @IntoMap
    @ServiceKey(LocationService::class)
    abstract fun bindLocationService(builder: LocationServiceComponent.Builder): AndroidInjector.Factory<out Service>
}