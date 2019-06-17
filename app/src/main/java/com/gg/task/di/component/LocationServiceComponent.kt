package com.gg.task.di.component

import com.gg.task.di.ServiceScope
import com.gg.task.di.module.LocationServiceModule
import com.gg.task.services.LocationService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [
    LocationServiceModule::class
])
@ServiceScope
interface LocationServiceComponent: AndroidInjector<LocationService> {
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<LocationService>()
}