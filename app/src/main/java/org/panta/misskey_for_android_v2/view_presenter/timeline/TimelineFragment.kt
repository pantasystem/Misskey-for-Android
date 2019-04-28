package org.panta.misskey_for_android_v2.view_presenter.timeline

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.adapter.TimelineAdapter
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.TimelineTypeEnum
import org.panta.misskey_for_android_v2.dialog.ReactionDialog
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.ITimeline
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.interfaces.UserClickListener
import org.panta.misskey_for_android_v2.repository.*
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import org.panta.misskey_for_android_v2.view_presenter.image_viewer.ImageViewerActivity
import org.panta.misskey_for_android_v2.view_presenter.note_description.NoteDescriptionActivity
import org.panta.misskey_for_android_v2.view_presenter.note_editor.EditNoteActivity
import org.panta.misskey_for_android_v2.view_presenter.user.UserActivity

class TimelineFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, TimelineContract.View,
    NoteClickListener, UserClickListener{


    companion object{
        private const val CONNECTION_INFOMATION = "TimelineFragmentConnectionInfomation"
        private const val TIMELINE_TYPE = "TIMELINE_FRAGMENT_TIMELINE_TYPE"
        private const val USER_ID = "TIMELINE_FRAGMENT_USER_TIMELINE"
        private const val IS_MEDIA_ONLY = "IS_MEDIA_ONLY"

        fun getInstance(info: DomainAuthKeyPair , type: TimelineTypeEnum, userId: String? = null, isMediaOnly: Boolean = false): TimelineFragment{
            return TimelineFragment().apply{
                val args = Bundle()
                args.putSerializable(CONNECTION_INFOMATION, info)
                args.putString(TIMELINE_TYPE, type.name)
                args.putString(USER_ID, userId)
                args.putBoolean(IS_MEDIA_ONLY, isMediaOnly)
                this.arguments = args
            }
        }

    }

    override lateinit var mPresenter: TimelineContract.Presenter

    private val reactionRequestCode = 23457

    //private var instanceDomain: String? = null
    //private var i: String? = null
    private var connectionInfo: DomainAuthKeyPair? = null

    private var mTimelineType: TimelineTypeEnum = TimelineTypeEnum.HOME
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: TimelineAdapter

    private var isMediaOnly: Boolean? = null
    private var userId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val args = arguments

        connectionInfo = args?.getSerializable(CONNECTION_INFOMATION) as DomainAuthKeyPair
        val timelineType = args.getString(TIMELINE_TYPE)
        isMediaOnly = args.getBoolean(IS_MEDIA_ONLY)
        userId = args.getString(USER_ID)

        if(timelineType != null){
            mTimelineType = TimelineTypeEnum.toEnum(timelineType)
        }

        val mTimeline: ITimeline = when (mTimelineType) {
            TimelineTypeEnum.GLOBAL -> GlobalTimeline(domain = connectionInfo!!.domain , authKey = connectionInfo!!.i)
            TimelineTypeEnum.HOME -> HomeTimeline(domain = connectionInfo!!.domain  , authKey = connectionInfo!!.i)
            TimelineTypeEnum.SOCIAL -> SocialTimeline(domain = connectionInfo!!.domain  , authKey = connectionInfo!!.i)
            TimelineTypeEnum.LOCAL -> LocalTimeline(domain = connectionInfo!!.domain)
            TimelineTypeEnum.USER -> UserTimeline(domain = connectionInfo!!.domain , userId = userId!!, isMediaOnly = isMediaOnly)
            else -> TODO("DESCRIPTIONを実装する")
        }
        mPresenter = TimelinePresenter(this, mTimeline, connectionInfo!!)


        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineView.addOnScrollListener(listener)
        mPresenter.initTimeline()
        mLayoutManager = LinearLayoutManager(context)


        refresh?.setOnRefreshListener(this)

    }


    override fun onRefresh() {
        mPresenter.getNewTimeline()
    }

    override fun stopRefreshing() {
        activity?.runOnUiThread{
            refresh?.isRefreshing = false
        }
    }



    override fun showInitTimeline(list: List<NoteViewData>) {
        activity?.runOnUiThread {
            load_icon?.visibility = View.GONE
            timelineView?.visibility = View.VISIBLE
            Log.d("TimelineFragment", "データの取得が完了した")
            mAdapter = TimelineAdapter(context!!, list)
            mAdapter.addNoteClickListener(this)
            mAdapter.addUserClickListener(this)


            timelineView?.layoutManager = mLayoutManager
            timelineView?.adapter = mAdapter

            stopRefreshing()

        }
    }



    override fun showNewTimeline(list: List<NoteViewData>) {
        activity?.runOnUiThread {
            stopRefreshing()
            mAdapter.addAllFirst(list)
        }

    }

    override fun showOldTimeline(list: List<NoteViewData>) {
        activity?.runOnUiThread{
            refresh?.isRefreshing = false
            mAdapter.addAllLast(list)
        }

    }

    override fun onNoteClicked(targetId: String?, note: Note?) {
        Log.d("TimelineFragment", "Noteをクリックした")
        val intent = Intent(context, NoteDescriptionActivity::class.java)
        intent.putExtra(NoteDescriptionActivity.NOTE_DESCRIPTION_NOTE_PROPERTY, note)
        startActivity(intent)
    }

    override fun onError(errorMsg: String) {
        Log.w("TimelineFragment", "エラー発生 message$errorMsg")
    }

    override fun onReplyButtonClicked(targetId: String?, note: Note?) {
        val intent = Intent(context, EditNoteActivity::class.java)
        intent.putExtra(EditNoteActivity.CREATE_NOTE_TARGET_ID, targetId)
            .putExtra(EditNoteActivity.EDIT_TYPE, EditNoteActivity.REPLY)
            .putExtra(EditNoteActivity.CONNECTION_INFO, connectionInfo)
        startActivity(intent)
    }

    override fun onReactionButtonClicked(targetId: String?, note: Note?) {
        if(targetId != null && note != null){
            Log.d("TimelineFragment", "targetId: $targetId")
            val reactionDialog = ReactionDialog.getInstance(targetId, object : ReactionDialog.CallBackListener{
                override fun callBack(noteId: String?, reactionParameter: String) {
                    if(noteId != null){
                        Log.d("TimelineFragment", "成功した")
                        mPresenter.sendReaction(noteId = noteId, reactionType = reactionParameter)
                    }
                }
            })
            reactionDialog.setTargetFragment(parentFragment, reactionRequestCode)
            reactionDialog.show(activity?.supportFragmentManager, "reaction_tag")
        }
    }


    override fun onReNoteButtonClicked(targetId: String?, note: Note?) {
        val intent = Intent(context, EditNoteActivity::class.java)
            .putExtra(EditNoteActivity.CREATE_NOTE_TARGET_ID, targetId)
            .putExtra(EditNoteActivity.EDIT_TYPE, EditNoteActivity.RE_NOTE)
            .putExtra(EditNoteActivity.CONNECTION_INFO, connectionInfo)
        startActivity(intent)
    }

    override fun onDescriptionButtonClicked(targetId: String?, note: Note?) {
        /*if(targetId != null && note != null){
            Log.d("TimelineFragment", "onDescriptionButtonが押された")
            val descriptionDialog = DescriptionDialog()
            val args = Bundle()
            args.putSerializable(DescriptionDialog.NOTE, note)
            descriptionDialog.arguments = args
            descriptionDialog.setTargetFragment(this, 5345)
            descriptionDialog.show(activity?.supportFragmentManager, "description_dialog")
        }*/
        val item = arrayOf<CharSequence>("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ（開発者向け）")

        AlertDialog.Builder(activity).apply{
            setTitle("詳細")
            setItems(item){ dialog, which->
                when(which){
                    4 ->{
                        AlertDialog.Builder(activity).apply{
                            if(note is Note){
                                val noteString = note.toString().replace(",","\n")
                                setMessage(noteString)
                            }
                            setPositiveButton(android.R.string.ok){i ,b->
                            }
                        }.show()
                    }
                }

            }
        }.show()


    }

    override fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>) {
        val intent = Intent(context, ImageViewerActivity::class.java)
        intent.putExtra(ImageViewerActivity.IMAGE_URL_LIST, clickedImageUrlCollection)
        intent.putExtra(ImageViewerActivity.CLICKED_IMAGE_URL, clickedIndex)
        startActivity(intent)

    }

    override fun onClickedUser(user: User) {
        val intent = Intent(context, UserActivity::class.java)
        intent.putExtra(UserActivity.USER_PROPERTY_TAG, user)
        intent.putExtra(UserActivity.CONNECTION_INFO, connectionInfo)
        startActivity(intent)
    }

    private val listener = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = mLayoutManager.itemCount
            val firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
            if( ! recyclerView.canScrollVertically(-1)){
                //先頭に来た場合
                refresh.isEnabled = true
                Log.d("TimelineFragment", "先頭に来た")
            }
            if( ! recyclerView.canScrollVertically(1)){
                //最後に来た場合
                refresh.isEnabled = false   //stopRefreshing関数を設けているがあえてこの形にしている
                mPresenter.getOldTimeline()
            }
        }
    }

}