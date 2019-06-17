package com.gg.task.di.component

import com.gg.task.arch.main.MainActivity
import com.gg.task.di.ActivityScope
import com.gg.task.di.module.MainModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(
    modules = [
        MainModule::class
    ]
)
@ActivityScope
interface MainComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}