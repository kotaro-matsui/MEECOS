package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import com.example.meecos.RecyclerView.RecyclerAdapter
import com.example.meecos.RecyclerView.RecyclerViewHolder
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment : BaseFragment(), RecyclerViewHolder.ItemClickListener {
    private lateinit var realm:Realm

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
        newPlanBtn.setOnClickListener(onCreatePlanBtn)

    //直近の予定5件表示する処理
        //Realmからレコード取得
        realm = Realm.getDefaultInstance()
        val latest5plan = realm.where(ScheduleObject::class.java)
            .sort("startDate")
            /*.limit(5)*/
            .findAll()

        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = RecyclerAdapter(
            (activity as MainActivity),
            this,
            latest5plan)
        recyclerView.layoutManager = LinearLayoutManager(
            (activity as MainActivity),
            LinearLayoutManager.VERTICAL, false)

        return view
    }

    //各レコードを押した時の処理
    override fun onItemClick(view: View, position: Int) {
        //ダイアログを開き、編集と削除が選べるようにする
    }

    //新規作成画面への遷移
    private val onCreatePlanBtn = View.OnClickListener {
        (activity as MainActivity).replaceFragment(NewPlanFragment())
    }
    //削除ボタンを押したときの処理
    private val onDeleteBtnClick = View.OnClickListener {
        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
            .setTitle("確認")
            .setMessage("削除してもよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                realm = Realm.getDefaultInstance()
                val result = realm.where(ScheduleObject::class.java).equalTo("id","1".toInt()).findAll()
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