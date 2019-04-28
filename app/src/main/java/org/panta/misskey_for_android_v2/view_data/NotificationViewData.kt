package org.panta.misskey_for_android_v2.view_data

import org.panta.misskey_for_android_v2.entity.NotificationProperty
import org.panta.misskey_for_android_v2.interfaces.ID
import java.io.Serializable

data class NotificationViewData(override val id: String,
                                override val isIgnore: Boolean, val notificationProperty: NotificationProperty, val noteViewData: NoteViewData?):Serializable, ID