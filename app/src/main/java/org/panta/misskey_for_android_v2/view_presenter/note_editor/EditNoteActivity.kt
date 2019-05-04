package org.panta.misskey_for_android_v2.view_presenter.note_editor

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_edit_note.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.NoteType
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.repository.SecretRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import org.panta.misskey_for_android_v2.view_presenter.user_auth.AuthActivity

class EditNoteActivity : AppCompatActivity(), EditNoteContract.View {

    companion object{
        const val EDIT_TYPE = "EDIT_NOTE_ACTIVITY_EDIT_TYPE"
        //const val CREATE = 0
        //const val RE_NOTE = 1
        //const val REPLY = 2
        //const val CONNECTION_INFO = "EditNoteActivityConnectionInfo"

        const val CREATE_NOTE_TARGET_ID = "EDIT_NOTE_ACTIVITY_CREATE_NOTE_ID"

        fun startActivity(context:Context, targetId: String?, type: NoteType?){
            val intent = Intent(context, EditNoteActivity::class.java)
            //intent.putExtra(EditNoteActivity.CONNECTION_INFO, info)
            if(type != null)  intent.putExtra(EDIT_TYPE, type.ordinal)
            if(targetId != null) intent.putExtra(CREATE_NOTE_TARGET_ID, targetId)
            context.startActivity(intent)
        }
    }

    //private lateinit var builder:CreateNoteProperty.Builder
    //private lateinit var noteRepository: NoteRepository
    override lateinit var mPresenter: EditNoteContract.Presenter

    private lateinit var connectionInfo: ConnectionProperty

    //private var mEditType = 0
    //private var mTargetId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(TwitterEmojiProvider())

        setContentView(R.layout.activity_edit_note)
        title = "投稿"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val editType = intent.getIntExtra(EDIT_TYPE, 0)
        val targetId = intent.getStringExtra(CREATE_NOTE_TARGET_ID)
        val info = SecretRepository(SharedPreferenceOperator(this)).getConnectionInfo()
        connectionInfo = if(info == null){
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }else{
            info
        }

        mPresenter = EditNotePresenter(this, connectionInfo)

        mPresenter.setNoteType(editType, targetId)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){

            }
            return@setOnNavigationItemSelectedListener false
        }

    }

    override fun onPosted() {
        runOnUiThread{
            finish()
        }
    }

    override fun onError(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCloudFileManager() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFileManager() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            R.id.post_button ->{
                val text = editText.text.toString()

                mPresenter.setText(text)
                mPresenter.postNote()
            }
        }
        return super.onOptionsItemSelected(item)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_edit_note_drawer, menu)
        return true
    }


}
