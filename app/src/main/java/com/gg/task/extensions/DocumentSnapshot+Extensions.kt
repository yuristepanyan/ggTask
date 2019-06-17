package com.gg.task.extensions

import com.gg.task.firestore.LAT_KEY
import com.gg.task.firestore.LNG_KEY
import com.gg.task.model.Position
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toPosition() = Position(this[LAT_KEY] as Double, this[LNG_KEY] as Double)