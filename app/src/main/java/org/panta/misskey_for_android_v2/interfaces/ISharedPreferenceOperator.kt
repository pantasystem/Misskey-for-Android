package org.panta.misskey_for_android_v2.interfaces

interface ISharedPreferenceOperator {
    fun get(key: String, defaultValue: String?): String?
    fun put(key: String, value: String)
}