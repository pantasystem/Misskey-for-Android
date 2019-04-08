package org.panta.misskey_for_android_v2.view_presenter.timeline

import android.app.Activity
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
import org.panta.misskey_for_android_v2.constant.TimelineTypeEnum
import org.panta.misskey_for_android_v2.dialog.DescriptionDialog
import org.panta.misskey_for_android_v2.dialog.ReactionDialog
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import org.panta.misskey_for_android_v2.view_presenter.note_editor.EditNoteActivity

class TimelineFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, TimelineContract.View, NoteClickListener{


    companion object{
        private const val TIMELINE_TYPE = "TIMELINE_FRAGMENT_TIMELINE_TYPE"
        fun getInstance(type: TimelineTypeEnum): TimelineFragment{
            return TimelineFragment().apply{
                val args = Bundle()
                args.putString(TIMELINE_TYPE, type.name)
                this.arguments = args
            }
        }
    }

    private var mTimelineType: TimelineTypeEnum = TimelineTypeEnum.HOME
    lateinit var mLayoutManager: LinearLayoutManager
    override lateinit var mPresenter: TimelineContract.Presenter
    private lateinit var mAdapter: TimelineAdapter

    private val reactionRequestCode = 23457

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mLayoutManager = LinearLayoutManager(context)
        val args = arguments
        val timelineType = args?.getString(TIMELINE_TYPE)
        if(timelineType != null){
            mTimelineType = TimelineTypeEnum.toEnum(timelineType)
        }
        mPresenter = TimelinePresenter(this, mTimelineType)

        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineView.addOnScrollListener(listener)
        mPresenter.initTimeline()

        refresh.setOnRefreshListener(this)

    }

    override fun onRefresh() {
        mPresenter.getNewTimeline()
    }

    override fun stopRefreshing() {
        activity?.runOnUiThread{
            refresh.isRefreshing = false
        }
    }

    private val listener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

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

    override fun showInitTimeline(list: List<NoteViewData>) {
        activity?.runOnUiThread {
            load_icon.visibility = View.GONE
            timelineView.visibility = View.VISIBLE
            Log.d("TimelineFragment", "データの取得が完了した")
            mAdapter = TimelineAdapter(context!!, list)
            mAdapter.addNoteClickListener(this)


            timelineView.layoutManager = mLayoutManager
            timelineView.adapter = mAdapter

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
            refresh.isRefreshing = false
            mAdapter.addAllLast(list)
        }

    }

    override fun onNoteClicked(targetId: String?, note: Note?) {
        Log.d("TimelineFragment", "Noteをクリックした")
    }

    override fun onError(errorMsg: String) {

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
            val reactionDialog = ReactionDialog()
            val args = Bundle()
            args.putString(ReactionDialog.TARGET_NOTE_ID, targetId)
            reactionDialog.arguments = args
            reactionDialog.setTargetFragment(this, reactionRequestCode)
            reactionDialog.show(activity?.supportFragmentManager, "reaction_tag")
        }

    }

    override fun onReNoteButtonClicked(targetId: String?, note: Note?) {
        val intent = Intent(context, EditNoteActivity::class.java)
        intent.putExtra(EditNoteActivity.CREATE_NOTE_TARGET_ID, targetId)
        intent.putExtra(EditNoteActivity.EDIT_TYPE, EditNoteActivity.RE_NOTE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == reactionRequestCode && data != null){
            if(resultCode != Activity.RESULT_OK){
               return
            }
            val targetNoteId: String? = data.getStringExtra(ReactionDialog.TARGET_NOTE_ID)
            val reaction = data.getStringExtra(ReactionDialog.SELECTED_REACTION_CODE)
            Log.d("TimelineView", "選択しましたid:$targetNoteId, reaction:$reaction")

            if(targetNoteId != null){
                mPresenter.sendReaction(noteId = targetNoteId, reactionType = reaction)

            }
        }
    }

}