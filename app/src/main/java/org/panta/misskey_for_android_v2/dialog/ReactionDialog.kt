package org.panta.misskey_for_android_v2.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.GridView
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.adapter.ReactionDialogGridViewAdapter
import org.panta.misskey_for_android_v2.constant.ReactionConstData
import org.panta.misskey_for_android_v2.util.convertDp2Px

class ReactionDialog : DialogFragment(){

    companion object{
        const val SELECTED_REACTION_CODE = "REACTION_DIALOG_SELECTED_REACTION_CODE"
        const val TARGET_NOTE_ID = "REACTION_DIALOG_TARGET_NOTE_ID"
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val targetNoteId = arguments?.getString(TARGET_NOTE_ID)

        val gridView = GridView(activity)
        gridView.apply {
            columnWidth = convertDp2Px(50F, context).toInt()
            numColumns = GridView.AUTO_FIT

        }
        gridView.adapter =
            ReactionDialogGridViewAdapter(context!!, R.layout.item_reaction_dialogs_icon, ReactionConstData.getAllConstReactionList())
        gridView.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent()
            intent.putExtra(SELECTED_REACTION_CODE, ReactionConstData.getAllConstReactionList()[i])
            intent.putExtra(TARGET_NOTE_ID, targetNoteId)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }

        val builder = AlertDialog.Builder(activity).apply {
            setView(gridView)
            setNegativeButton("やめる") { _, _ ->

            }

        }
        return builder.create()
    }
}