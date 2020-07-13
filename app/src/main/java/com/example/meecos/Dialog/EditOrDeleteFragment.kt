package com.example.meecos.Dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Config.CHOOSE
import com.example.meecos.Fragment.Schedule.NewPlanFragment
import com.example.meecos.Model.ScheduleObject
import io.realm.Realm

class EditOrDeleteFragment: DialogFragment(){
    lateinit var realm :Realm
    lateinit var scheduleObj :ScheduleObject
    private lateinit var listener: EditOrDeleteListener
    var isPlanList : Boolean = false

    // コールバック用インタフェース。呼び出し元で実装する
    interface EditOrDeleteListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    companion object {
        fun newInstance(scheduleObj :ScheduleObject, listener: EditOrDeleteListener,isPlanList:Boolean): EditOrDeleteFragment {
            val dialog = EditOrDeleteFragment()
            dialog.scheduleObj = scheduleObj
            dialog.listener = listener
            dialog.isPlanList = isPlanList
            return dialog
        }
    }

    // TODO: 予定削除した時にAlarmからも削除する処理を実装する
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setItems(CHOOSE){dialog, which ->
                when(CHOOSE[which]){
                    "編集" -> (activity as MainActivity).replaceFragment(
                        NewPlanFragment(
                            scheduleObj
                        )
                    )
                    "削除" ->{
                        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                            .setTitle("確認")
                            .setMessage("削除してもよろしいですか？")
                            .setPositiveButton("はい") { _, _ ->
                                listener!!.onDialogPositiveClick()
                            }
                            .setNegativeButton("いいえ") { _, _ ->
                                listener!!.onDialogNegativeClick()
                            }
                            .show()
                    }
                }
            }
            .setNegativeButton("閉じる", null)
            .create()
    }

    fun submit(inputText: String?) {
        val target = targetFragment ?: return
        val data = Intent()
        data.putExtra(Intent.EXTRA_TEXT, inputText)
        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
    }
}
