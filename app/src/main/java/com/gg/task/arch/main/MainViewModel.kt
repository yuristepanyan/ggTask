package com.gg.task.arch.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gg.task.firestore.FirestoreHelper
import com.gg.task.model.Position

class MainViewModel(
    private val firestoreHelper: FirestoreHelper
) : ViewModel() {
    val locationData = MutableLiveData<Position?>()

    init {
        getLocation()
    }

    private fun getLocation(){
        firestoreHelper.getLocation(locationData)
    }
}