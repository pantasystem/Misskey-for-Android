package org.panta.misskey_for_android_v2.adapter

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true) data class StreamingProperty(
    @JsonProperty("type") val type: String,
    @JsonProperty("body") val body: Body
){
    @JsonIgnoreProperties(ignoreUnknown = true) data class Body(
        @JsonProperty("id") val id: String,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("body") val body: Body
    ){
        @JsonIgnoreProperties(ignoreUnknown = true) data class Body(
            @JsonProperty("reaction") val reaction: String?,
            @JsonProperty("userId") val userId: String?,
            @JsonProperty("deletedAt") val deletedAt: String?
        )
    }

}