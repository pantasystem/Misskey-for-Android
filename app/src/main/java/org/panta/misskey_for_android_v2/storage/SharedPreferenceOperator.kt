package org.panta.misskey_for_android_v2.storage

import android.annotation.SuppressLint
import android.content.SharedPreferences
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator

class SharedPreferenceOperator(private val sharedPref: SharedPreferences) : ISharedPreferenceOperator{

    override fun get(key: String, defaultValue: String?): String?{
        return sharedPref.getString(key, null)
    }

    override fun put(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }
}