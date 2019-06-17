package com.gg.task.extensions

import android.app.Service
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.intentTo(cls: Class<out Service>) = Intent(this, cls)
