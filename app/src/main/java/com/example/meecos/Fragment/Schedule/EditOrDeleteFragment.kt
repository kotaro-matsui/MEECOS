package com.example.meecos.Fragment.Schedule

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Model.ScheduleObject
import io.realm.Realm


class EditOrDeleteFragment(var scheduleObj :ScheduleObject?):DialogFragment(){
    lateinit var realm :Realm
    // 呼び出し元のActivityを保持する
    private lateinit var listener: EditOrDeleteListener

    // コールバック用インタフェース。呼び出し元で実装する
    interface EditOrDeleteListener {
        fun onDialogPositiveClick(dialog: DialogFragment,isError:Boolean)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
    
    companion object{
        // リストに表示する値を配列として定義
        val CHOOSE = arrayOf("編集", "削除")
    }

    //TODO:ここを変更してActivityではなくFragmentを呼び出し元と認識するようにする
/*    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // 呼び出し元のActivityを変数listenerで保持する
            listener = activity as EditOrDeleteListener
        } catch (e: ClassCastException) {
            // 呼び出し元のActivityでコールバックインタフェースを実装していない場合
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }*/

    //TODO:予定削除した時にAlarmからも削除する処理を実装する
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //削除に失敗したかどうかの変数
        var isError = false

        try {
            // 呼び出し元のActivityを変数listenerで保持する
            listener = ScheduleFragment() as EditOrDeleteListener
        } catch (e: ClassCastException) {
            // 呼び出し元のActivityでコールバックインタフェースを実装していない場合
            throw ClassCastException((activity.toString() +
                    " must implement EditOrDeleteListener"))
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setItems(CHOOSE){dialog, which ->
                when(CHOOSE[which]){
                    "編集" -> (activity as MainActivity).replaceFragment(NewPlanFragment(scheduleObj))
                    "削除" ->{
                        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                            .setTitle("確認")
                            .setMessage("削除してもよろしいですか？")
                            .setPositiveButton("はい") { _, _ ->
                                realm = Realm.getDefaultInstance()
                                val target = realm.where(ScheduleObject::class.java)
                                    .equalTo("id", scheduleObj?.id)
                                    .findAll()
                                println("schduleObj.id =" + scheduleObj?.id)
                                try {
                                    // トランザクションして削除
                                    realm.executeTransaction{
                                        target.deleteFromRealm(0)
                                    }
                                }catch (e:Exception){
                                    println("e.message = " + e.message)
                                    isError = true
                                }
                                listener.onDialogPositiveClick(this,isError)
                            }
                            .setNegativeButton("いいえ") { _, _ ->
                                listener.onDialogNegativeClick(this)
                            }
                            .show()
                    }
                }
            }
            .setNegativeButton("閉じる", null)
            .create()
            return dialog
    }

    fun submit(inputText: String?) {
        val target = targetFragment ?: return
        val data = Intent()
        data.putExtra(Intent.EXTRA_TEXT, inputText)
        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
    }
}
