package com.example.meecos.Fragment.Schedule

import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
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

        //日付を選択した時にその日付を取得する
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = "$year/$month/$dayOfMonth"
            Toast.makeText(activity as MainActivity, date, Toast.LENGTH_SHORT).show()
        }

        //新規作成画面への遷移
        val newPlanBtn = view.findViewById<Button>(R.id.newPlan)
        newPlanBtn.setOnClickListener(onBtnClick)

        //背景を赤にするテストボタン
        val redBtn = view.findViewById<Button>(R.id.backRed)
        redBtn.setOnClickListener(onRedBtnClick)
        return view
    }

    private val onBtnClick = View.OnClickListener {
        (activity as MainActivity).replaceFragment(NewPlanFragment())
    }

    //背景色切り替えメソッド
    private val onRedBtnClick = View.OnClickListener {
        val a = view?.findViewById<View>(R.id.layout)
        var t = a!!.tag
        if(t == 1) {
            a?.setBackgroundColor(Color.RED)
            a.tag = 2
        }
        else{
            a?.setBackgroundColor(Color.BLACK)
            a.tag = 1
        }
    }
}