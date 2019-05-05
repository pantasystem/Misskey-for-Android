package org.panta.misskey_for_android_v2.view_presenter.note_editor


import org.panta.misskey_for_android_v2.entity.CreateNoteProperty
import org.panta.misskey_for_android_v2.interfaces.BasePresenter
import org.panta.misskey_for_android_v2.interfaces.BaseView
import java.io.File

//そこまで複雑ではないのでリッチアクティビティで行く方針
interface EditNoteContract{
    interface View : BaseView<Presenter> {
        fun onError(msg: String)
        fun showFileManager()
        fun showCloudFileManager()
        fun startPost(builder: CreateNoteProperty.Builder, files: Array<String>)
    }

    interface Presenter : BasePresenter {

        fun setText(text: String)
        fun postNote()
        fun setNoteType(type: Int, targetId: String?)
        fun setVisibility(visibility: String)
        fun setFile(file: File)
    }
}