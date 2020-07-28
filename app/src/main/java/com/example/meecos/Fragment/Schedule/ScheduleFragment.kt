package com.example.meecos.Fragment.Schedule

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Dialog.DateDialogFragment
import com.example.meecos.Dialog.EditOrDeleteFragment
import com.example.meecos.Dialog.YearMonthPickerDialogFragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Fragment.Schedule.compactcalendar.CompactCalendarView
import com.example.meecos.Fragment.Schedule.compactcalendar.Event
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import com.example.meecos.RecyclerView.RecyclerAdapter
import com.example.meecos.RecyclerView.RecyclerViewHolder
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*


class ScheduleFragment : BaseFragment(), RecyclerViewHolder.ItemClickListener,
    EditOrDeleteFragment.EditOrDeleteListener{
    private lateinit var realm:Realm
    private lateinit var latestPlans:RealmResults<ScheduleObject>
    private var selectObject: ScheduleObject? = null
    private lateinit var compactCalendarView:CompactCalendarView
//通知用
    private var am: AlarmManager? = null
    private var pending: PendingIntent? = null
    private val requestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        setTitle("スケジュール")


        val dateFormatForMonth = SimpleDateFormat("yyyy - MMM", Locale.getDefault())
        val mYearMonth = view.findViewById<TextView>(R.id.selectedYearMonth)
        mYearMonth.text = dateFormatForMonth.format(Date())
        //カレンダー上のyyyy年 - MM月を押下した時の処理（未完成）
        mYearMonth.setOnClickListener {
            (activity as MainActivity).datePickDialog(mYearMonth,view)
        }
        /*val calendarView = view.findViewById<CalendarView>(R.id.calender)*/

        //外部ライブラリのカレンダー使用
        compactCalendarView = view.findViewById(R.id.compactcalendar_view)
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY)
        compactCalendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                /*val events: List<Event> =
                    compactCalendarView.getEvents(dateClicked)*/
                val sdFormat = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
                val date = sdFormat.format(dateClicked)
                val plf:PlanListFragment = PlanListFragment.newInstance(date)
                (activity as MainActivity).replaceFragment(plf)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                mYearMonth.text = dateFormatForMonth.format(firstDayOfNewMonth)
            }
        })

        //予定表示する処理
        //Realmから終了日が本日以降のレコード取得
        realm = Realm.getDefaultInstance()
        //startDate,startTime,endDate,endTimeで昇順でSortする為の設定
        val names = arrayOf("startDate","startTime","endDate","endTime")
        val sorts = arrayOf(Sort.ASCENDING,Sort.ASCENDING,Sort.ASCENDING,Sort.ASCENDING)
        this.latestPlans = realm.where(ScheduleObject::class.java)
            .greaterThanOrEqualTo("endDate",Date(System.currentTimeMillis()-86400000))
            .sort(names,sorts)
            .findAll()

        //カレンダー上の日付に、予定がある日は白丸がつく処理
        var ev: Event
        for(item in latestPlans){
            val sDate = item.startDate!!.time
            val eDate = item.endDate!!.time
            val dates = (eDate - sDate)/86400000
            for(i in 1..dates){
                ev =
                    Event(
                        Color.WHITE,
                        item.startDate?.time!! + i*86400000,
                        item.contents
                    )
                compactCalendarView.addEvent(ev)
            }
        }

        //RecyclerViewの表示
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = RecyclerAdapter(
            (activity as MainActivity),
            this,
            latestPlans)
        recyclerView.layoutManager = LinearLayoutManager(
            (activity as MainActivity),
            LinearLayoutManager.VERTICAL, false)

        //RecyclerViewに枠線を付ける為の処理
        val divider =
            androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context,LinearLayoutManager(activity).orientation)
        ContextCompat.getDrawable(activity as MainActivity, R.drawable.divider)?.let { divider.setDrawable(it) };
        recyclerView.addItemDecoration(divider)
        return view
    }

    //ツールバー右側に＋ボタンを追加する処理
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }
    //アイコン押した時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.newPlan){
            (activity as MainActivity).replaceFragment(NewPlanFragment(null))
        }
        return super.onOptionsItemSelected(item)
    }

    //各レコードを押した時の処理
    override fun onItemClick(view: View, position: Int) {
        //押したレコードの内容（ScheduleObject）を取得
        this.selectObject = latestPlans[position] as ScheduleObject
        //ダイアログを開き、編集と削除が選べるようにする
        val dialog = EditOrDeleteFragment.newInstance(this.selectObject!!, this,false)
        dialog.show((activity as MainActivity).supportFragmentManager, view::class.java.simpleName)
    }

    override fun onDialogPositiveClick() {
        if(!ScheduleObject().deleteByID(this.selectObject!!.id!!,context)){
            showToast("削除に失敗しました")
            return
        } else {
            replaceFragment(ScheduleFragment())
            showToast("削除に成功しました")
        }
    }

    override fun onDialogNegativeClick() {
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    open fun cancelNotification(notifiId: Int, context: Context?) {
        val intent = Intent(context, AlarmNotification::class.java)
        pending = PendingIntent.getBroadcast(
            context, requestCode, intent, notifiId
        )
        // アラームを解除する
        am =
            activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        if (am != null) {
            am!!.cancel(pending)
            Toast.makeText(
                activity,
                "alarm cancel", Toast.LENGTH_SHORT
            ).show()
            Log.d("debug", "cancel")
        } else {
            Log.d("debug", "null")
        }
    }

    //日付選択ダイアログを表示するメソッドを呼び出すクラス
    private fun MainActivity.datePickDialog(textView: TextView,view:View) {
        DateDialogFragment(textView, view)
            .show(supportFragmentManager, textView::class.java.simpleName)

    }
}