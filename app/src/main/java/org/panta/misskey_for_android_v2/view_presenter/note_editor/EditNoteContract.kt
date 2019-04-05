package org.panta.misskey_for_android_v2.view_presenter.note_editor


import org.panta.misskey_for_android_v2.interfaces.BasePresenter
import org.panta.misskey_for_android_v2.interfaces.BaseView

//そこまで複雑ではないのでリッチアクティビティで行く方針
interface EditNoteContract{
    interface View : BaseView<Presenter> {
        fun onPosted()
        fun onError(msg: String)
    }

    interface Presenter : BasePresenter {
        fun normalPost(text: String)
        fun reply(id:String, text: String)
        fun reNote(id: String, text: String?)
    }
}