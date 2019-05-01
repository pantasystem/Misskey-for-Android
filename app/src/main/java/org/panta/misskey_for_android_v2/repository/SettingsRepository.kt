package org.panta.misskey_for_android_v2.repository

import android.content.Context

class SettingsRepository(context: Context){
    companion object {
        private const val DEFAULT_APP_TAG = "default_app_tag"
    }
    private val pref = context.getSharedPreferences(DEFAULT_APP_TAG, Context.MODE_PRIVATE)

    var isNotificationEnabled: Boolean
        get() = pref.getBoolean("is_notification_enabled", true)
        set(value){
            val e = pref.edit()
            e.putBoolean("is_notification_enabled", value)
            e.apply()
        }



}