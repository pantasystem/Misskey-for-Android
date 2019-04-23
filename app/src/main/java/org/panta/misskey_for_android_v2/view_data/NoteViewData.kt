package org.panta.misskey_for_android_v2.view_data

import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.repository.AbsTimeline
import org.panta.misskey_for_android_v2.repository.NoteAdjustment
import java.io.Serializable

//isReply = 返信であるか, isOriginReplay = 返信先であるか(背景を暗くする)
//isOriginReplyは途中で追加されたデータなので最新のデータにはならない
//isReplyが最新となるがViewにはisOriginReplayが先に挿入される
//通常のNote,RNの場合は両方ともがFalseとなる
data class NoteViewData(val note: Note, val type: NoteAdjustment.NoteType, val reactionCountPairList: List<ReactionCountPair>):Serializable