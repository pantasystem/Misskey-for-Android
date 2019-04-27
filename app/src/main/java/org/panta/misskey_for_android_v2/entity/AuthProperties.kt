package org.panta.misskey_for_android_v2.entity

data class SessionResponse(val token: String, val url: String)
data class UserKeyResponse(val accessToken: String, val user: User)