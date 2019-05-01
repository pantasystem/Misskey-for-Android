package org.panta.misskey_for_android_v2.repository

import android.content.Context
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator

class SettingsRepository(val pref: ISharedPreferenceOperator){

    var isNotificationEnabled: Boolean
        get() = pref.getBoolean("is_notification_enabled", true)
        set(value){
            pref.putBoolean("is_notification_enabled", value)
        }



}