package org.panta.misskey_for_android_v2.view_presenter.user

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_user.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.view_presenter.PagerAdapter

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val pageAdapter = PagerAdapter(supportFragmentManager)
        user_view_pager.offscreenPageLimit = 2
        user_view_pager.adapter = pageAdapter

        user_tab_menu.setupWithViewPager(view_pager)


    }
}
