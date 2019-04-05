package org.panta.misskey_for_android_v2.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.entity.Note


@Suppress("UNREACHABLE_CODE")
class DescriptionDialog : DialogFragment(){

    companion object{
        const val NOTE = "DescriptionDialog_NOTE"
    }



    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

        val note = arguments?.getSerializable(NOTE)


        val item = arrayOf<CharSequence>("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ（開発者向け）")
        val dialog = AlertDialog.Builder(activity).apply{
            setTitle("詳細")
            setItems(item){ dialog, which->
                when(which){
                    4 ->{
                        AlertDialog.Builder(activity).apply{
                            if(note is Note){
                                setMessage(note.toString())
                            }
                            setPositiveButton(android.R.string.ok){i ,b->
                                dismiss()
                            }
                        }.show()
                    }
                }
                dismiss()

            }
            setMessage("nazekahyoujisarenai")
        }
        return dialog.create()

    }
}