package com.gg.task.di.module

import androidx.lifecycle.ViewModelProviders
import com.gg.task.arch.main.MainActivity
import com.gg.task.arch.main.MainViewModel
import com.gg.task.arch.main.MainViewModelFactory
import com.gg.task.di.ActivityScope
import com.gg.task.firestore.FirestoreHelper
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideViewModel(activity: MainActivity, firestoreHelper: FirestoreHelper): MainViewModel {
        return ViewModelProviders.of(activity, MainViewModelFactory(firestoreHelper)).get(MainViewModel::class.java)
    }
}