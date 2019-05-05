package org.panta.misskey_for_android_v2.view_presenter.note_editor

import org.panta.misskey_for_android_v2.constant.NoteType

import org.panta.misskey_for_android_v2.entity.CreateNoteProperty
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.repository.NoteRepository

class EditNotePresenter(private val mView: EditNoteContract.View, private val connectionInfo: ConnectionProperty) : EditNoteContract.Presenter{

    private val noteBuilder = CreateNoteProperty.Builder(connectionInfo.i)
    private val noteRepository = NoteRepository(connectionInfo.domain)

    private var noteType: NoteType = NoteType.CREATE

    override fun setText(text: String) {
        noteBuilder.text = if(text.isBlank()){
            null
        }else{
            text
        }
    }

    override fun setNoteType(type: Int, targetId: String?) {
        this.noteType = NoteType.getEnumFromInt(type)
        if(noteType == NoteType.RE_NOTE){
            noteBuilder.renoteId = targetId!!
        }else if(noteType == NoteType.REPLY){
            noteBuilder.replyId = targetId!!
        }
    }

    override fun postNote() {
        val property = noteBuilder.create()
        when(noteType){
            NoteType.CREATE ->{
                val check = ! property.text.isNullOrBlank() && property.text.length < 1500
                if(check) noteRepository.send(property)
                mView.onPosted()
            }
            NoteType.REPLY ->{
                val check = ! property.text.isNullOrBlank() && property.text.length < 1500
                if(check && property.replyId != null) noteRepository.send(property)
                mView.onPosted()
            }
            NoteType.RE_NOTE ->{
                if(property.renoteId != null){
                    noteRepository.send(property)
                    mView.onPosted()
                }
            }
        }
    }

    override fun setVisibility(visibility: String) {
        noteBuilder.visibility = visibility
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}