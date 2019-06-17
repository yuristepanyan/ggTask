package com.gg.task.arch.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gg.task.firestore.FirestoreHelper

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val firestoreHelper: FirestoreHelper
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(firestoreHelper) as T
    }
}