package org.panta.misskey_for_android_v2.view_presenter.note_editor

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_edit_note.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.NoteType
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.entity.CreateNoteProperty
import org.panta.misskey_for_android_v2.repository.SecretRepository
import org.panta.misskey_for_android_v2.service.NotePostService
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import org.panta.misskey_for_android_v2.view_presenter.user_auth.AuthActivity
import java.io.File

class EditNoteActivity : AppCompatActivity(), EditNoteContract.View {

    companion object{
        const val EDIT_TYPE = "EDIT_NOTE_ACTIVITY_EDIT_TYPE"
        //const val CREATE = 0
        //const val RE_NOTE = 1
        //const val REPLY = 2
        //const val CONNECTION_INFO = "EditNoteActivityConnectionInfo"

        const val CREATE_NOTE_TARGET_ID = "EDIT_NOTE_ACTIVITY_CREATE_NOTE_ID"

        private const val REQUEST_PERMISSION_CODE = 238
        private const val FILE_MANAGER_RESULT_CODE = 852

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

        val visibilitySelection = arrayOf<CharSequence>("public", "home", "followers", "specified")
        visibility_button.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("公開範囲")
                .setSingleChoiceItems(visibilitySelection, 0){ _, which ->
                    mPresenter.setVisibility(visibilitySelection[which].toString())
                }
                .setPositiveButton(android.R.string.ok){ _, _->

                }
                .show()
        }

        select_image_button.setOnClickListener{
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)


            if(permissionCheck == PackageManager.PERMISSION_GRANTED && writePermissionCheck == PackageManager.PERMISSION_GRANTED){
                showFileManager()
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    , REQUEST_PERMISSION_CODE)
            }


        }
    }



    override fun onError(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCloudFileManager() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, FILE_MANAGER_RESULT_CODE)
    }

    override fun startPost(builder: CreateNoteProperty.Builder, files: Array<String>) {
        val intent = Intent(applicationContext, NotePostService::class.java)
        if(files.isNotEmpty()){
            intent.putExtra(NotePostService.FILE_NAME_ARRAY_CODE, files)
        }
        intent.putExtra(NotePostService.NOTE_BUILDER_CODE, builder)
        startService(intent)
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == FILE_MANAGER_RESULT_CODE){
                Log.d("EditNoteActivity", "contentPath ${intent.data}")

                //FIXME ギャラリーから選択した場合NULLが返ってくる
                val file = getFilePath(data!!)
                Log.d("EditNoteActivity", "getExtras ${intent?.extras}")

                mPresenter.setFile(file)
            }
        }

    }

    private fun getFilePath(data: Intent): File{
        val strDocId = DocumentsContract.getDocumentId(data.data)

        val strSplittedDocId = strDocId.split(":")
        val strId = strSplittedDocId[strSplittedDocId.size - 1]

        val crsCursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.MediaColumns.DATA),
            "_id=?",
            arrayOf(strId),
            null
        )
        crsCursor?.moveToFirst()
        val filePath = crsCursor?.getString(0)
        crsCursor?.close()
        Log.d("EditNoteActivity", "filePath $filePath")
        return File(filePath)
    }




}
