package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Model.ScheduleObject
import io.realm.Realm
import io.realm.RealmResults
import java.lang.Exception

class EditOrDeleteFragment(var scheduleObj :ScheduleObject?):DialogFragment() {
    lateinit var realm :Realm
    companion object{
        // リストに表示する値を配列として定義
        val CHOOSE = arrayOf("編集", "削除")
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            /*.setTitle("タイトル")*/
            .setItems(CHOOSE){dialog, which ->
                when(CHOOSE[which]){
                    "編集" -> (activity as MainActivity).replaceFragment(NewPlanFragment(scheduleObj))
                    "削除" ->{
                        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                            .setTitle("確認")
                            .setMessage("削除してもよろしいですか？")
                            .setPositiveButton("はい") { _, _ ->
                                Handler(Looper.getMainLooper()).post {
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
                                        //コールバックする→(activity as MainActivity).replaceFragment(ScheduleFragment())
                                        //Toast.makeText(activity, "削除に成功しました。", Toast.LENGTH_SHORT).show()
                                    }catch (e:Exception){
                                        println("e.message = " + e.message)
                                        //Toast.makeText(activity, "削除に失敗しました。", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .setNegativeButton("いいえ") { _, _ ->
                                // TODO:Noが押された時の挙動
                            }
                            .show()
                    }
                }
            }
            .setNegativeButton("閉じる", null)
            .create()
            return dialog
    }

    private fun deleteRecord(){

    }
}
