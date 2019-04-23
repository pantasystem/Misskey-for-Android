package org.panta.misskey_for_android_v2.util

import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.panta.misskey_for_android_v2.interfaces.IImageViewInjection

class ImageViewInjection : IImageViewInjection{
    override fun setImageFromOnline(url: String?, imageView: ImageView) {
        Picasso
            .get()
            .load(url)
            .into(imageView)
    }
}