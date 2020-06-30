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
import androidx.lifecycle.ViewModel
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_newplan.view.*
import java.util.*

class NewPlanFragment : BaseFragment() {
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
        val startDateBtn = view.findViewById<Button>(R.id.startDateBtn)
        startDateBtn.setOnClickListener{
            (activity as MainActivity).datePickDialog(startDateBtn)
        }
        val endDateBtn = view.findViewById<Button>(R.id.endDateBtn)
        endDateBtn.setOnClickListener{
            (activity as MainActivity).datePickDialog(endDateBtn)
        }

        //時間をダイアログで選択できるようにする
        val startTimeBtn = view.findViewById<Button>(R.id.startTimeBtn)
        startTimeBtn.setOnClickListener{
            (activity as MainActivity).timePickDialog(startTimeBtn)
        }

        val endTimeBtn = view.findViewById<Button>(R.id.endTimeBtn)
        endTimeBtn.setOnClickListener{
            (activity as MainActivity).timePickDialog(endTimeBtn)
        }

        val contents = view.findViewById<EditText>(R.id.contents)
        val submitBtn = view.findViewById<Button>(R.id.plan_submit)
        val sb1 : SpannableStringBuilder = startDateBtn.text as SpannableStringBuilder
        val strStartDate = sb1.toString()
        val sb2 : SpannableStringBuilder = startDateBtn.text as SpannableStringBuilder
        val strStartTime = sb2.toString()
        val sb3 : SpannableStringBuilder = startDateBtn.text as SpannableStringBuilder
        val strEndDate = sb3.toString()
        val sb4 : SpannableStringBuilder = startDateBtn.text as SpannableStringBuilder
        val strEndTime = sb4.toString()
        val sb5 : SpannableStringBuilder = startDateBtn.text as SpannableStringBuilder
        val strContents = sb5.toString()
        
        submitBtn.setOnClickListener{
            onSubmitBtnClick(
                strStartDate,
                strStartTime,
                strEndDate,
                strEndTime,
                strContents)
        }

        //Realmの中身表示テスト用

        val realmText1 = view.findViewById<TextView>(R.id.realm1)
        val realmText2 = view.findViewById<TextView>(R.id.realm2)
        val realmText3 = view.findViewById<TextView>(R.id.realm3)
        val realmText4 = view.findViewById<TextView>(R.id.realm4)

        val realmView = view.findViewById<Button>(R.id.realmView)
        realmView.setOnClickListener{
            //idがXのレコードをとってくる
            //Realmに登録はされている気がするが、各フィールドに入力内容が登録されていない。全部初期値（日付を選択
            val id1 = realm.where(ScheduleObject::class.java).equalTo("id", "5").findFirst()
            realmText1.setText(id1?.startDate)
            realmText2.setText(id1?.startTime)
            realmText3.setText(id1?.endDate)
            realmText4.setText(id1?.contents)
        }

        val textReset = view.findViewById<Button>(R.id.textReset)
        textReset.setOnClickListener{

            realmText1.setText("初期化1")
            realmText2.setText("初期化2")
            realmText3.setText("初期化3")
            realmText4.setText("初期化4")

        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // Realmオブジェクトの削除
        realm.close()
    }


    //日付選択ダイアログを表示するメソッドを呼び出すクラス
    private fun MainActivity.datePickDialog(button: Button) {
        DateDialogFragment(button).show(supportFragmentManager, button::class.java.simpleName)
    }
    //時間選択ダイアログを表示するメソッドを呼び出すクラス
    private fun MainActivity.timePickDialog(button: Button) {
        TimeDialogFragment(button).show(supportFragmentManager, button::class.java.simpleName)
    }

    private fun onSubmitBtnClick(startDate:String,startTime:String,endDate:String,endTime:String,contents:String){
        val id = "3"
        realm.executeTransaction{ realm ->
            val obj = realm.createObject(ScheduleObject::class.java!!, id)
            obj.startDate = startDate
            obj.startTime = startTime
            obj.endDate = endDate
            obj.endTime = endTime
            obj.contents = contents
        }
    }
}