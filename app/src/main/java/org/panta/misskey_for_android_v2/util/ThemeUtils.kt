package org.panta.misskey_for_android_v2.util

import android.content.Context
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ThemeType
import org.panta.misskey_for_android_v2.repository.PersonalRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator

fun setThemeFromType(context: Context){
    val type = PersonalRepository(SharedPreferenceOperator(context)).getUserTheme()
    when(ThemeType.STANDARD){
        ThemeType.STANDARD -> context.setTheme(R.style.AppTheme)
        ThemeType.BLACK -> context.setTheme(R.style.AppThemeDark)
    }
}