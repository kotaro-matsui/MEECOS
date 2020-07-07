package com.example.meecos.Fragment.Schedule

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_newplan.view.*
import java.util.*

class NewPlanFragment(var scheduleObj : ScheduleObject?) : BaseFragment() {
    //Realmの宣言
    private lateinit var realm:Realm
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Realmインスタンス化
        realm = Realm.getDefaultInstance()
        val view = inflater.inflate(R.layout.fragment_newplan, container,false)
        setTitle("予定作成・編集")

        //日付をダイアログで選択できるようにする
        val startDateBtn = view.findViewById<TextView>(R.id.startDateBtn)
        val endDateBtn = view.findViewById<TextView>(R.id.endDateBtn)
        startDateBtn.setOnClickListener{
            (activity as MainActivity).datePickDialog(startDateBtn,view)
        }
        endDateBtn.setOnClickListener{
            (activity as MainActivity).datePickDialog(endDateBtn,view)
        }

        //時間をダイアログで選択できるようにする
        val startTimeBtn = view.findViewById<TextView>(R.id.startTimeBtn)
        startTimeBtn.setOnClickListener{
            (activity as MainActivity).timePickDialog(startTimeBtn)
        }
        val endTimeBtn = view.findViewById<TextView>(R.id.endTimeBtn)
        endTimeBtn.setOnClickListener{
            (activity as MainActivity).timePickDialog(endTimeBtn)
        }

        val contents = view.findViewById<EditText>(R.id.contents)

        //編集の場合、各項目に予め値が入っているようにする
        if(scheduleObj != null){
            startDateBtn.text = scheduleObj!!.startDate
            startTimeBtn.text = scheduleObj!!.startTime
            endDateBtn.text = scheduleObj!!.endDate
            endTimeBtn.text = scheduleObj!!.endTime
            contents.setText(scheduleObj!!.contents)
        }

        val submitBtn = view.findViewById<Button>(R.id.plan_submit)

        submitBtn.setOnClickListener{
            onSubmitBtnClick(
                startDateBtn.text.toString(),
                startTimeBtn.text.toString(),
                endDateBtn.text.toString(),
                endTimeBtn.text.toString(),
                contents.text.toString())
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // Realmオブジェクトの削除
        realm.close()
    }


    //日付選択ダイアログを表示するメソッドを呼び出すクラス
    private fun MainActivity.datePickDialog(textView: TextView,view:View) {
        DateDialogFragment(textView,view).show(supportFragmentManager, textView::class.java.simpleName)
    }
    //時間選択ダイアログを表示するメソッドを呼び出すクラス
    private fun MainActivity.timePickDialog(textView: TextView) {
        TimeDialogFragment(textView).show(supportFragmentManager, textView::class.java.simpleName)
    }

    /*TODO:予定の登録、または変更した時にAlarmに登録する処理を実装する
    * 削除する時の対象を判断する為に、Alarm登録時に格納する変数もScheduleObjectに必要？*/
    private fun onSubmitBtnClick(startDate:String,startTime:String,endDate:String,endTime:String,contents:String){
        var newId = -1
        //編集でない場合は最新のIDを取得し、+1する
        if(scheduleObj == null){
            var maxId = realm.where(ScheduleObject::class.java).max("id")
            if(maxId != null){
                newId = maxId.toInt() + 1
            }
        }else{
            newId = scheduleObj!!.id!!
        }
        try {
            realm.executeTransaction{ realm ->
                if(scheduleObj == null){
                    val obj = realm.createObject(ScheduleObject::class.java!!, newId)
                    obj.startDate = startDate
                    obj.startTime = startTime
                    obj.endDate = endDate
                    obj.endTime = endTime
                    obj.contents = contents
                    Toast.makeText(activity as MainActivity, "登録に成功しました。id:$newId", Toast.LENGTH_SHORT).show()
                }else{
                    val target = realm.where(ScheduleObject::class.java)
                        .equalTo("id", scheduleObj!!.id)
                        .findFirst()
                    target?.startDate = startDate
                    target?.startTime = startTime
                    target?.endDate = endDate
                    target?.endTime = endTime
                    target?.contents = contents
                    Toast.makeText(activity as MainActivity, "変更に成功しました。id:$newId", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e : Exception){
            if(scheduleObj == null){
                Toast.makeText(activity as MainActivity, "登録に失敗しました。id:$newId", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity as MainActivity, "変更に失敗しました。id:$newId", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(activity as MainActivity, e.message, Toast.LENGTH_SHORT).show()
        }

    }
}