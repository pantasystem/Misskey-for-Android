package org.panta.misskey_for_android_v2.view_presenter.note_editor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.panta.misskey_for_android_v2.entity.CreateNoteProperty

/*class EditNotePresenter(private val mView: EditNoteContract.View, private val authKey: String) : EditNoteContract.Presenter{

    //private val noteApi = NotesAPIAdapter()
    val noteBuilder = CreateNoteProperty.Builder(authKey)

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //FIXME 完全にビジネスロジックが混じってしまっているので修正する
    override fun normalPost(text: String) {
        if(text.isEmpty() || text.length > 1500){
            mView.onError("文字数が多すぎる又は少なすぎます")
            return
        }

        GlobalScope.launch {
            noteApi.create(CreateNote(
                i = authKey,
                text = text
            ))
            mView.onPosted()

        }
    }

    override fun reNote(id: String, text: String?) {
        val data = if(text != null && text.isEmpty()){
            CreateNote(i = authKey, renoteId = id)
        }else{
            CreateNote(i = authKey, text = text, renoteId = id)
        }

        GlobalScope.launch{
            noteApi.create(data)
            mView.onPosted()
        }

    }

    override fun reply(id: String, text: String) {
        GlobalScope.launch{
            noteApi.create(CreateNote(
                i = authKey,
                text = text,
                replyId = id
            ))
            mView.onPosted()
        }
    }
}*/