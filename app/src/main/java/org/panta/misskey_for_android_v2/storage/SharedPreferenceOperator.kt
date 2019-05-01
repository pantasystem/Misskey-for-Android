package org.panta.misskey_for_android_v2.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator

class SharedPreferenceOperator(private val context: Context) : ISharedPreferenceOperator{

    companion object {
        private const val APP_NAME = "misskey_for_Android_pref_version_2.0"
    }
    override fun getString(key: String, defaultValue: String?): String?{
        val sharedPref = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    override fun put(key: String, value: String) {
        val sharedPref = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }
}