package org.panta.misskey_for_android_v2.interfaces

import android.widget.ImageView
import java.net.URL

interface IImageViewInjection {
    fun setImageFromOnline(url: String?, imageView: ImageView)
}