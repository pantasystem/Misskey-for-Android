package org.panta.misskey_for_android_v2.adapter

import com.fasterxml.jackson.annotation.JsonProperty

data class StreamingProperty(
    @JsonProperty("type") val type: String,
    @JsonProperty("body") val body: Body
){
    data class Body(
        @JsonProperty("id") val id: String,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("body") val body: Body
    ){
        data class Body(
            @JsonProperty("reaction") val reaction: String?,
            @JsonProperty("userId") val userId: String?,
            @JsonProperty("deletedAt") val deletedAt: String?
        )
    }

}