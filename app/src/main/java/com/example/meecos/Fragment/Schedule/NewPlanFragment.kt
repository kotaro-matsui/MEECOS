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
import io.realm.kotlin.where
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
        val submitBtn = view.findViewById<Button>(R.id.plan_submit)

        submitBtn.setOnClickListener{
            onSubmitBtnClick(
                startDateBtn.text.toString(),
                startTimeBtn.text.toString(),
                endDateBtn.text.toString(),
                endTimeBtn.text.toString(),
                contents.text.toString())
        }

        //Realmの中身表示テスト用

        val realmText1 = view.findViewById<TextView>(R.id.realm1)
        val realmText2 = view.findViewById<TextView>(R.id.realm2)
        val realmText3 = view.findViewById<TextView>(R.id.realm3)
        val realmText4 = view.findViewById<TextView>(R.id.realm4)
        val realmText5 = view.findViewById<TextView>(R.id.realm5)

        val realmView = view.findViewById<Button>(R.id.realmView)
        realmView.setOnClickListener{
            val realmId = view.findViewById<EditText>(R.id.realmId)
            val strId = realmId.text.toString()
            //入力されたidのレコードをとってくる
            val id1 = realm.where(ScheduleObject::class.java).equalTo("id", strId.toInt()).findFirst()
            realmText1.setText(id1?.startDate)
            realmText2.setText(id1?.startTime)
            realmText3.setText(id1?.endDate)
            realmText4.setText(id1?.endTime)
            realmText5.setText(id1?.contents)
        }

        val textReset = view.findViewById<Button>(R.id.textReset)
        textReset.setOnClickListener{

            realmText1.setText("初期化1")
            realmText2.setText("初期化2")
            realmText3.setText("初期化3")
            realmText4.setText("初期化4")
            realmText5.setText("初期化5")
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

    private fun onSubmitBtnClick(startDate:String,startTime:String,endDate:String,endTime:String,contents:String){
        var maxId = realm.where(ScheduleObject::class.java).max("id")
        var newId = 1
        if(maxId != null){
            newId = maxId.toInt() + 1
        }

        println("id : $newId")

        realm.executeTransaction{ realm ->
            val obj = realm.createObject(ScheduleObject::class.java!!, newId)
            obj.startDate = startDate
            obj.startTime = startTime
            obj.endDate = endDate
            obj.endTime = endTime
            obj.contents = contents
        }
    }
}