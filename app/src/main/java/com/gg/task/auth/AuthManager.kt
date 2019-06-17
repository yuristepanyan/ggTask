package com.gg.task.auth

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class AuthManager @Inject constructor(context: Context) : Authentication {

    private val prefs = context.getSharedPreferences(AuthKeys.DATA_KEY.key, Context.MODE_PRIVATE)

    override fun getId(): String? {
        return prefs.getString(AuthKeys.DATA_KEY.key, null)
    }

    override fun setId(id: String) {
        prefs.edit {
            putString(AuthKeys.DATA_KEY.key, id)
        }
    }

}