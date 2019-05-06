package org.panta.misskey_for_android_v2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image_preview.view.*
import org.panta.misskey_for_android_v2.R
import java.io.File

class ImagePreviewAdapter(fileList: List<File>) : RecyclerView.Adapter<ImagePreviewAdapter.ImagePreviewHolder>(){
    class ImagePreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imagePreview: ImageView? = itemView.image_preview
    }

    private val fileArrayList = ArrayList<File>(fileList)

    override fun getItemCount(): Int {
        return fileArrayList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImagePreviewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_image_preview, p0, false)
        return ImagePreviewHolder(inflater)
    }

    override fun onBindViewHolder(p0: ImagePreviewHolder, p1: Int) {
        val file = fileArrayList[p1]
        Picasso
            .get()
            .load(file)
            .into(p0.imagePreview)

        p0.imagePreview?.setOnClickListener{

        }

    }

    fun addFile(file: File){
        synchronized(fileArrayList){
            fileArrayList.add(file)
        }

        notifyItemInserted(fileArrayList.size - 1)
    }

    fun removeFile(index: Int){
        synchronized(fileArrayList){
            fileArrayList.removeAt(index)
        }

        notifyItemRemoved(index)
    }



}