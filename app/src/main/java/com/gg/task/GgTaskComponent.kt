package com.gg.task

import android.app.Application
import android.content.Context
import com.gg.task.auth.Authentication
import com.gg.task.di.module.ActivityBuilder
import com.gg.task.di.module.ServiceBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        GgTaskModule::class,
        ServiceBuilder::class,
        ActivityBuilder::class
    ]
)
interface GgTaskComponent : AndroidInjector<DaggerApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): GgTaskComponent
    }

    fun getContext(): Context

    fun getAuthentication(): Authentication
}