package org.panta.misskey_for_android_v2.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true) data class ReactionCount(
    @JsonProperty("like") var like: Int? = null,
    @JsonProperty("love") var love:Int? = null,
    @JsonProperty("laugh") var laugh:Int? = null,
    @JsonProperty("hmm") var hmm: Int? = null,
    @JsonProperty("surprise") var surprise: Int? = null,
    @JsonProperty("congrats") var congrats: Int? = null,
    @JsonProperty("angry") var angry: Int? = null,
    @JsonProperty("confused") var confused: Int? = null,
    @JsonProperty("rip") var rip: Int? = null,
    @JsonProperty("pudding") var pudding: Int? = null
): Serializable