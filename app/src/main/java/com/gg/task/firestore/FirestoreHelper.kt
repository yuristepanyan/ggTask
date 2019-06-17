package com.gg.task.firestore

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.gg.task.model.Position

interface FirestoreHelper {

    fun saveLocation(location: Location)

    fun getLocation(location: MutableLiveData<Position?>)
}