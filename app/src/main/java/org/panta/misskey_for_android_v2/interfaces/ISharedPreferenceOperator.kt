package org.panta.misskey_for_android_v2.interfaces

interface ISharedPreferenceOperator {
    fun getString(key: String, defaultValue: String?): String?
    fun putString(key: String, value: String)
}