package org.panta.misskey_for_android_v2.view_data

import org.panta.misskey_for_android_v2.entity.NotificationProperty
import java.io.Serializable

data class NotificationViewData(val notificationProperty: NotificationProperty, val noteViewData: NoteViewData?):Serializable