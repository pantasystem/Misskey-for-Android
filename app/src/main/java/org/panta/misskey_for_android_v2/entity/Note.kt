package org.panta.misskey_for_android_v2.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown=true) data class Note(
    @JsonProperty("id") val id: String,
    @JsonProperty("createdAt") val createdAt: String,
    @JsonProperty("text") val text: String?,
    @JsonProperty("cw") val cw: String?,
    @JsonProperty("userId") val userId: String?,

    @JsonProperty("replyId") val replyId: String?,
    @JsonProperty("renoteId") val reNoteId: String?,
    @JsonProperty("viaMobile") val viaMobile: Boolean?,
    @JsonProperty("visibility") val visibility: String?,
    @JsonProperty("visibilityUserIds") val visibilityUserIds: List<String?>?,
    @JsonProperty("url") val url: String?,
    @JsonProperty("renoteCount") val reNoteCount: Int,
    @JsonProperty("reactionCounts") val reactionCounts: ReactionCount?,
    @JsonProperty("repliesCount") val replyCount: Int,
    @JsonProperty("user") val user: User?,
    @JsonProperty("files") val files: List<FileProperty?>?,
    @JsonProperty("mediaIds") val mediaIds: List<Any?>?,
    @JsonProperty("renote") val renote: Note?,
    @JsonProperty("reply") val reply: Note?,
    @JsonProperty("myReaction") val myReaction: String?
): Serializable