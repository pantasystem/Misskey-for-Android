package org.panta.misskey_for_android_v2.view_presenter.user

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
import org.panta.misskey_for_android_v2.dialog.ReactionDialog
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.ITimeline
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.interfaces.UserClickListener
import org.panta.misskey_for_android_v2.repository.HomeTimeline
import org.panta.misskey_for_android_v2.repository.UserTimeline
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import org.panta.misskey_for_android_v2.view_presenter.image_viewer.ImageViewerActivity
import org.panta.misskey_for_android_v2.view_presenter.note_description.NoteDescriptionActivity
import org.panta.misskey_for_android_v2.view_presenter.note_editor.EditNoteActivity
import org.panta.misskey_for_android_v2.view_presenter.timeline.TimelineContract
import org.panta.misskey_for_android_v2.view_presenter.timeline.TimelineFragment
import org.panta.misskey_for_android_v2.view_presenter.timeline.TimelinePresenter


//FIXME クソコード　解決策はないのか？
class UserTimelineFragment : Fragment(), TimelineContract.View,SwipeRefreshLayout.OnRefreshListener, NoteClickListener, UserClickListener {

    companion object {
        const val USER_ID_TAG = "UserTimelineFragmentUserIdTag"

        fun getInstance(userId: String): UserTimelineFragment{
            val bundle = Bundle()
            bundle.putString(USER_ID_TAG, userId)
            val fragment = UserTimelineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    //private var userTimeline:ITimeline = HomeTimeline(ApplicationConstant.domain, ApplicationConstant.authKey)
    override var mPresenter: TimelineContract.Presenter = TimelinePresenter(this, HomeTimeline(ApplicationConstant.domain,ApplicationConstant.authKey))
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: TimelineAdapter
    private val reactionRequestCode = 23457


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mLayoutManager = LinearLayoutManager(context)


        val args = arguments
        val userId = args?.getString(USER_ID_TAG)!!

        mPresenter = TimelinePresenter(this, UserTimeline(ApplicationConstant.domain, userId))

        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineView.addOnScrollListener(listener)
        mPresenter.initTimeline()
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

    override fun onRefresh() {
        mPresenter.getNewTimeline()
    }

    override fun stopRefreshing() {
        activity?.runOnUiThread{
            refresh?.isRefreshing = false
        }
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