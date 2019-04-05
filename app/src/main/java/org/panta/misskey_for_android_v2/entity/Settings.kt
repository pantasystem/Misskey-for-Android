package org.panta.misskey_for_android_v2.entity

import java.io.Serializable

data class PersonAuthKey(val i: String)
data class LocalAppTimelineSetting(var excludeNsfw: Boolean? = null, val includeMyReNotes: Boolean = true ,val includeReNotedNotes: Boolean = true, val includeLocalReNotes: Boolean = true): Serializable