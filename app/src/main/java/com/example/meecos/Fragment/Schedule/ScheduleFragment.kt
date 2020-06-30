package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Toast
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment : BaseFragment() {

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
        newPlanBtn.setOnClickListener(onBtnClick)

        //予定編集画面への遷移
        val editPlanBtn1 = view.findViewById<Button>(R.id.editPlan1)
        editPlanBtn1.setOnClickListener(onEditBtnClick)

        val editPlanBtn2 = view.findViewById<Button>(R.id.editPlan2)
        editPlanBtn2.setOnClickListener(onEditBtnClick)

        val editPlanBtn3 = view.findViewById<Button>(R.id.editPlan3)
        editPlanBtn3.setOnClickListener(onEditBtnClick)

        //予定削除確認ダイアログの表示
        val deletePlanBtn1 = view.findViewById<Button>(R.id.deletePlan1)
        deletePlanBtn1.setOnClickListener(onDeleteBtnClick)

        val deletePlanBtn2 = view.findViewById<Button>(R.id.deletePlan2)
        deletePlanBtn2.setOnClickListener(onDeleteBtnClick)

        val deletePlanBtn3 = view.findViewById<Button>(R.id.deletePlan3)
        deletePlanBtn3.setOnClickListener(onDeleteBtnClick)
        return view
    }

    private val onBtnClick = View.OnClickListener {
        (activity as MainActivity).replaceFragment(NewPlanFragment())
    }

    private val onEditBtnClick = View.OnClickListener {
        (activity as MainActivity).replaceFragment(NewPlanFragment())
    }

    private val onDeleteBtnClick = View.OnClickListener {
        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
            .setTitle("確認")
            .setMessage("削除してもよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                // TODO:Yesが押された時の挙動
            }
            .setNegativeButton("いいえ") { _, _ ->
                // TODO:Noが押された時の挙動
            }
            .show()
    }

}