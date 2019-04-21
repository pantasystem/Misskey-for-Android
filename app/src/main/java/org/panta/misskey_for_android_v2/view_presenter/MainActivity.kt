package org.panta.misskey_for_android_v2.view_presenter

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.TimelineTypeEnum
import org.panta.misskey_for_android_v2.repository.MyInfo
import org.panta.misskey_for_android_v2.view_presenter.note_editor.EditNoteActivity
import org.panta.misskey_for_android_v2.view_presenter.timeline.TimelineFragment
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val myFragmentTagsStack = Stack<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener {
            val intent = Intent(applicationContext, EditNoteActivity::class.java)
            startActivity(intent)
        }


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        /*val pageAdapter = PagerAdapter(supportFragmentManager)
        view_pager.offscreenPageLimit = 2
        view_pager.adapter = pageAdapter

        tab_menu.setupWithViewPager(view_pager)*/

        val sf = supportFragmentManager
        val ft = sf.beginTransaction()
        ft.replace(R.id.main_container, TimelineFragment.getInstance(TimelineTypeEnum.HOME))
        ft.commit()

        bottom_navigation.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when(it.itemId){
                R.id.home_timeline ->{
                    setFragment(TimelineFragment.getInstance(TimelineTypeEnum.HOME))
                    true
                }
                R.id.global_timeline ->{
                    setFragment(TimelineFragment.getInstance(TimelineTypeEnum.GLOBAL))
                    true
                }
                R.id.notification_item ->{
                    false
                }
                R.id.message_item ->{
                    false
                }
                else -> false

            }
        }

        updateHeaderProfile()



    }

    private fun setFragment(fragment: Fragment){
        val sf = supportFragmentManager
        val ft = sf.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.main_container, fragment)
        ft.commit()
    }

    private fun updateHeaderProfile(){
        MyInfo(domain = ApplicationConstant.domain, authKey =ApplicationConstant.authKey)
            .getMyInfo {
                runOnUiThread {
                    if(it.avatarUrl != null){
                        Picasso
                            .get()
                            .load(it.avatarUrl)
                            .into(my_account_icon)
                    }

                    if(it.name != null){
                        my_name.text = it.name
                    }
                    val userName = if(it.host != null){
                        "@${it.userName}@${it.host}"
                    }else{
                        "@${it.userName}"
                    }
                    my_user_name.text = userName

                    follower_count.text = it.followersCount.toString()
                    following_count.text= it.followingCount.toString()
                }
            }
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        }
        val size = supportFragmentManager.fragments.size
        if(size > 0){
            supportFragmentManager.popBackStack()
            val fragments = supportFragmentManager.fragments

        }else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(keyCode == KeyEvent.KEYCODE_BACK){
            supportFragmentManager.popBackStack()
            super.onKeyDown(keyCode, event)
        }else{
            false
        }
    }*/
}
