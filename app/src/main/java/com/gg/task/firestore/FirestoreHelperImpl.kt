package com.gg.task.firestore

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gg.task.auth.Authentication
import com.gg.task.extensions.toPosition
import com.gg.task.model.Position
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

private const val TAG = "FIRESTORE_TAG"
private const val DB_NAME = "USERS"
const val LAT_KEY = "lat"
const val LNG_KEY = "lng"

class FirestoreHelperImpl @Inject constructor(private val authentication: Authentication) : FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    override fun saveLocation(location: Location) {
        db.collection(DB_NAME).document(authentication.getId() ?: "")
            .set(hashMapOf(LAT_KEY to location.latitude, LNG_KEY to location.longitude))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written") }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

    }

    override fun getLocation(location: MutableLiveData<Position?>) {
        db.collection(DB_NAME).document(authentication.getId() ?: "")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    location.postValue(snapshot.toPosition())
                } else {
                    location.postValue(null)
                }
            }
    }
}