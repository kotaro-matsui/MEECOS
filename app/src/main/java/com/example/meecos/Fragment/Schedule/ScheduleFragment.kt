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

class ScheduleFragment : BaseFragment() {
    private lateinit var realm:Realm
    private lateinit var recordId:String
    private lateinit var strRecordId:Array<String?>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_schedule, container, false)
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

        //直近の予定5件表示する処理 TODO:もっと簡潔に書けないか？①
        var recordIds = arrayOf(
            view.findViewById<TextView>(R.id.recordId1),
            view.findViewById<TextView>(R.id.recordId2),
            view.findViewById<TextView>(R.id.recordId3),
            view.findViewById<TextView>(R.id.recordId4),
            view.findViewById<TextView>(R.id.recordId5))
        var startDate = arrayOf(
            view.findViewById<TextView>(R.id.planStartTime1),
            view.findViewById<TextView>(R.id.planStartDate2),
            view.findViewById<TextView>(R.id.planStartDate3),
            view.findViewById<TextView>(R.id.planStartDate4),
            view.findViewById<TextView>(R.id.planStartDate5))
        var startTime = arrayOf(
            view.findViewById<TextView>(R.id.planStartTime1),
            view.findViewById<TextView>(R.id.planStartTime2),
            view.findViewById<TextView>(R.id.planStartTime3),
            view.findViewById<TextView>(R.id.planStartTime4),
            view.findViewById<TextView>(R.id.planStartTime5))
        var endDate = arrayOf(
            view.findViewById<TextView>(R.id.planEndDate1),
            view.findViewById<TextView>(R.id.planEndDate2),
            view.findViewById<TextView>(R.id.planEndDate3),
            view.findViewById<TextView>(R.id.planEndDate4),
            view.findViewById<TextView>(R.id.planEndDate5))
        var endTime = arrayOf(
            view.findViewById<TextView>(R.id.planEndTime1),
            view.findViewById<TextView>(R.id.planEndTime2),
            view.findViewById<TextView>(R.id.planEndTime3),
            view.findViewById<TextView>(R.id.planEndTime4),
            view.findViewById<TextView>(R.id.planEndTime5))
        var contents = arrayOf(
            view.findViewById<TextView>(R.id.planDetail1),
            view.findViewById<TextView>(R.id.planDetail2),
            view.findViewById<TextView>(R.id.planDetail3),
            view.findViewById<TextView>(R.id.planDetail4),
            view.findViewById<TextView>(R.id.planDetail5))

        realm = Realm.getDefaultInstance()
        strRecordId = arrayOfNulls<String>(5)
        val latest5plan = realm.where(ScheduleObject::class.java)
                            .sort("startDate")
                            .limit(5)
                            .findAll()
        var i = 0
        latest5plan.forEach{
            recordIds[i].text = it.id.toString()
            strRecordId[i] = it.id.toString()
            startDate[i].text = it.startDate
            startTime[i].text = it.startTime
            endDate[i].text = it.endDate
            endTime[i].text = it.endTime
            contents[i].text = it.contents
            i += 1
        }

        recordId = "-1"
        //予定編集画面への遷移 TODO:もっと簡潔に書けないか？②
        val editPlanBtn1 = view.findViewById<Button>(R.id.editPlan1)
        editPlanBtn1.setOnClickListener{
            recordId = recordIds[0].id.toString()
            (onEditBtnClick)
        }
        val editPlanBtn2 = view.findViewById<Button>(R.id.editPlan2)
        editPlanBtn2.setOnClickListener{
            recordId = recordIds[1].id.toString()
            onEditBtnClick
        }
        val editPlanBtn3 = view.findViewById<Button>(R.id.editPlan3)
        editPlanBtn3.setOnClickListener{
            recordId = recordIds[2].id.toString()
            onEditBtnClick
        }
        val editPlanBtn4 = view.findViewById<Button>(R.id.editPlan4)
        editPlanBtn4.setOnClickListener{
            recordId = recordIds[3].id.toString()
            onEditBtnClick
        }
        val editPlanBtn5 = view.findViewById<Button>(R.id.editPlan5)
        editPlanBtn5.setOnClickListener{
            recordId = recordIds[4].id.toString()
            onEditBtnClick
        }

        //予定削除確認ダイアログの表示
        val deletePlanBtn1 = view.findViewById<Button>(R.id.deletePlan1)
        deletePlanBtn1.setOnClickListener(onDeleteBtnClick)
        val deletePlanBtn2 = view.findViewById<Button>(R.id.deletePlan2)
        deletePlanBtn2.setOnClickListener(onDeleteBtnClick)
        val deletePlanBtn3 = view.findViewById<Button>(R.id.deletePlan3)
        deletePlanBtn3.setOnClickListener(onDeleteBtnClick)
        val deletePlanBtn4 = view.findViewById<Button>(R.id.deletePlan4)
        deletePlanBtn4.setOnClickListener(onDeleteBtnClick)
        val deletePlanBtn5 = view.findViewById<Button>(R.id.deletePlan5)
        deletePlanBtn5.setOnClickListener(onDeleteBtnClick)
        return view
        }

    //新規作成ボタン・編集ボタンを押したときの処理
    private val onEditBtnClick = View.OnClickListener{
        /*  TODO:
            予定作成ページに遷移する際、新規作成での遷移なら渡す値は特になし
            編集で遷移する際はrecordIdを渡す
            予定作成ページでrecordIdの中身を確認し、初期値（-1)の場合、realm処理はせず、
            0以上の場合はrealmで検索し、結果を各ダイアログのテキストに代入する*/
        (activity as MainActivity).replaceFragment(NewPlanFragment(recordId))
    }

    //削除ボタンを押したときの処理
    private val onDeleteBtnClick = View.OnClickListener {
        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
            .setTitle("確認")
            .setMessage("削除してもよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                realm = Realm.getDefaultInstance()
                val result = realm.where(ScheduleObject::class.java).equalTo("id",recordId.toInt()).findAll()
                realm.executeTransaction{realm ->
                    result.deleteFromRealm(0)
                    (activity as MainActivity).replaceFragment(this)
                }
            }
            .setNegativeButton("いいえ") { _, _ ->
                // TODO:Noが押された時の挙動
            }
            .show()
    }

    private val buttonClick =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.deletePlan1 -> recordId = strRecordId[0]!!
                R.id.deletePlan2 -> recordId = strRecordId[1]!!
                R.id.deletePlan3 -> recordId = strRecordId[2]!!
                R.id.deletePlan4 -> recordId = strRecordId[3]!!
                R.id.deletePlan5 -> recordId = strRecordId[4]!!
            }
            AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                .setTitle("確認")
                .setMessage("削除してもよろしいですか？")
                .setPositiveButton("はい") { _, _ ->
                    realm = Realm.getDefaultInstance()
                    val result = realm.where(ScheduleObject::class.java).equalTo("id",recordId.toInt()).findAll()
                    realm.executeTransaction{realm ->
                        result.deleteFromRealm(0)
                        (activity as MainActivity).replaceFragment(this)
                    }
                }
                .setNegativeButton("いいえ") { _, _ ->
                    // TODO:Noが押された時の挙動
                }
                .show()
        }
}