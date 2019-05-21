package org.panta.misskey_for_android_v2.constant

enum class ThemeType{
    STANDARD, BLACK;

    companion object{
        fun getThemeTypeFromInt(type: Int): ThemeType{
            return when(type){
                STANDARD.ordinal -> STANDARD
                BLACK.ordinal -> BLACK
                else -> STANDARD
            }
        }
    }
}