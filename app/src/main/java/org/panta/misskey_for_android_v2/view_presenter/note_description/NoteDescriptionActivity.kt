package org.panta.misskey_for_android_v2.view_presenter.note_description

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import org.panta.misskey_for_android_v2.R

class NoteDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_description)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
