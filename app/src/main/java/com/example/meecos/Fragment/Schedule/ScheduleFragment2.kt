package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment2 : BaseFragment() {
    private lateinit var realm:Realm
    private lateinit var recordId:String
    private lateinit var strRecordId:Array<String?>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_schedule2, container, false)
        setTitle("スケジュール")
        val calendarView = view.findViewById<CalendarView>(R.id.calender)

        //日付を選択した時にその日付の予定を表示する
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val trueMonth = month + 1
            val date = "$year/$trueMonth/$dayOfMonth"
            Toast.makeText(activity as MainActivity, date, Toast.LENGTH_SHORT).show()
            (activity as MainActivity).replaceFragment(PlanListFragment())
        }

        //予定新規作成画面への遷移
        val newPlanBtn = view.findViewById<ImageButton>(R.id.newPlan)
        newPlanBtn.setOnClickListener(onEditBtnClick)

        //直近の予定5件表示する処理 TODO:もっと簡潔に書けないか？
        realm = Realm.getDefaultInstance()
        strRecordId = arrayOfNulls<String>(5)
        val latest5plan = realm.where(ScheduleObject::class.java)
                            .sort("startDate")
                            .limit(5)
                            .findAll()

        recordId = "-1"
        //予定編集画面への遷移 TODO:もっと簡潔に書けないか？②

        //予定削除確認ダイアログの表示
        return view
        }

    //新規作成ボタン・編集ボタンを押したときの処理
    private val onEditBtnClick = View.OnClickListener{
        /*  TODO:
            予定作成ページに遷移する際、新規作成での遷移なら渡す値は特になし
            編集で遷移する際はrecordIdを渡す
            予定作成ページでrecordIdの中身を確認し、初期値（-1)の場合、realm処理はせず、
            0以上の場合はrealmで検索し、結果を各ダイアログのテキストに代入する*/
        (activity as MainActivity).replaceFragment(NewPlanFragment())
    }

    //削除ボタンを押したときの処理
    private val onDeleteBtnClick = View.OnClickListener {
        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
            .setTitle("確認")
            .setMessage("削除してもよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
            }
            .setNegativeButton("いいえ") { _, _ ->
                // TODO:Noが押された時の挙動
            }
            .show()
    }
}