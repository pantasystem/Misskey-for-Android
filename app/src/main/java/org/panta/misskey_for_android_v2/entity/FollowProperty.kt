package org.panta.misskey_for_android_v2.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true) data class FollowProperty(
    @JsonProperty("id") val id: String,
    @JsonProperty("createdAt") val createdAt: String,
    @JsonProperty("followeeId") val followeeId: String,
    @JsonProperty("followee") val followee: User?,
    @JsonProperty("followingId") val followingId: String,
    @JsonProperty("follower") val follower: User?

)