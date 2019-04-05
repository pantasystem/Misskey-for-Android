package org.panta.misskey_for_android_v2.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown=true) data class User(
    @JsonProperty("id") val id:String,
    @JsonProperty("username") val userName: String,
    @JsonProperty("name") val name: String?,
    @JsonProperty("host") val host: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("createdAt") val createdAt: String?,
    @JsonProperty("followersCount") val followersCount: Int,
    @JsonProperty("followingCount") val followingCount: Int,
    @JsonProperty("notesCount") val notesCount: Int,
    @JsonProperty("isBot") val isBot: Boolean,
    @JsonProperty("isCat") val isCat: Boolean,
    @JsonProperty("isAdmin") val isAdmin: Boolean,
    @JsonProperty("avatarUrl") val avatarUrl: String?,
    @JsonProperty("avatarColor") val avatarColor: List<Int>?,
    @JsonProperty("emojis") val emojis: List<Any?>?,
    @JsonProperty("isVerified") val isVerified: Boolean,
    @JsonProperty("isLocked") val isLocked: Boolean
    ): Serializable