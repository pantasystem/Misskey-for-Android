package org.panta.misskey_for_android_v2.view_presenter.note_editor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_edit_note.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.authKey
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.domain
import org.panta.misskey_for_android_v2.entity.CreateNoteProperty
import org.panta.misskey_for_android_v2.repository.NoteRepository

class EditNoteActivity : AppCompatActivity() {

    companion object{
        const val EDIT_TYPE = "EDIT_NOTE_ACTIVITY_EDIT_TYPE"
        const val CREATE = 0
        const val RE_NOTE = 1
        const val REPLY = 2

        const val CREATE_NOTE_TARGET_ID = "EDIT_NOTE_ACTIVITY_CREATE_NOTE_ID"
    }

    private var builder = CreateNoteProperty.Builder(authKey)
    private val noteRepository = NoteRepository(domain)

    private var mEditType = 0
    private var mTargetId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        mEditType = intent.getIntExtra(EDIT_TYPE, 0)
        mTargetId = intent.getStringExtra(CREATE_NOTE_TARGET_ID)

        when(mEditType){
            RE_NOTE -> builder.renoteId = mTargetId
            REPLY -> builder.replyId = mTargetId
        }

        post_note.setOnClickListener{
            val text = editText.text.toString()

            send(text)

        }
    }

    private fun send(text: String){
        val tmpText = if(text.isBlank()){
            null
        }else{
            text
        }
        if(mEditType == RE_NOTE && tmpText == null){
            noteRepository.send(builder.create())
        }else if(tmpText != null){

            builder.text = tmpText
            val obj = builder.create()
            Log.d("EditNoteActivity", "input ${obj.text}")
            noteRepository.send(obj)
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)


    }
}
