package com.gg.task

import android.app.Application
import android.content.Context
import com.gg.task.auth.AuthManager
import com.gg.task.auth.Authentication
import com.gg.task.di.component.LocationServiceComponent
import com.gg.task.di.component.MainComponent
import com.gg.task.firestore.FirestoreHelper
import com.gg.task.firestore.FirestoreHelperImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    subcomponents = [
        LocationServiceComponent::class,
        MainComponent::class
    ]
)
abstract class GgTaskModule {
    @Binds
    @Singleton
    abstract fun bindContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun bindAuthentication(application: AuthManager): Authentication

    @Binds
    @Singleton
    abstract fun bindFirestoreHelper(helper: FirestoreHelperImpl): FirestoreHelper
}