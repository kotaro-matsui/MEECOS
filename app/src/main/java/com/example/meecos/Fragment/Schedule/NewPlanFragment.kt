package com.example.meecos.Fragment.Schedule

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Dialog.DateDialogFragment
import com.example.meecos.Dialog.TimeDialogFragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewPlanFragment(var scheduleObj : ScheduleObject?) : BaseFragment() {
    //Realmの宣言
    private lateinit var realm:Realm
    //日付フォーマット
    private val sdFormat = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
    private val sdTimeFormat = SimpleDateFormat("HH:mm",Locale.JAPAN)
    //通知テスト用
    private var am: AlarmManager? = null
    private var pending: PendingIntent? = null
    private val requestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Realmインスタンス化
        realm = Realm.getDefaultInstance()
        val view = inflater.inflate(R.layout.fragment_newplan, container,false)
        if(scheduleObj != null) setTitle("予定編集") else setTitle("予定作成")

        val mTitle = view.findViewById<EditText>(R.id.titleEdit)
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

        //編集の場合、各項目に予め値が入っているようにする。新規の場合、現在日時が入っているようにする
        if(scheduleObj != null){
            val strStartDate = sdFormat.format(scheduleObj!!.startDate!!)
            val strEndDate = sdFormat.format(scheduleObj!!.endDate!!)

            mTitle.setText(scheduleObj!!.title)
            startDateBtn.text = strStartDate
            startTimeBtn.text = scheduleObj!!.startTime
            endDateBtn.text = strEndDate
            endTimeBtn.text = scheduleObj!!.endTime
            contents.setText(scheduleObj!!.contents)
        }else{
            val strStartDate = sdFormat.format(Date())
            val strStartTime = sdTimeFormat.format(Date())
            val strEndDate = sdFormat.format(Date())
            val strEndTime = sdTimeFormat.format(Date())

            startDateBtn.text = strStartDate
            startTimeBtn.text = strStartTime
            endDateBtn.text = strEndDate
            endTimeBtn.text = strEndTime
        }

        val submitBtn = view.findViewById<Button>(R.id.plan_submit)
        submitBtn.setOnClickListener{
            onSubmitBtnClick(
                mTitle.text.toString(),
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
        DateDialogFragment(textView, view)
            .show(supportFragmentManager, textView::class.java.simpleName)
    }
    //時間選択ダイアログを表示するメソッドを呼び出すクラス
    private fun MainActivity.timePickDialog(textView: TextView) {
        TimeDialogFragment(textView)
            .show(supportFragmentManager, textView::class.java.simpleName)
    }

    //予定変更・追加処理
    private fun onSubmitBtnClick(mTitle:String?,startDate:String,startTime:String,endDate:String,endTime:String,contents:String){
        if(mTitle == "") {
            showToast("タイトルを入力してください")
            return
        }
        val sDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)
        val sTime = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME)
        val eDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE)
        val eTime = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME)
        val sCalendar = Calendar.getInstance()
        //現在時刻より前の予定を登録した場合、通知をしないようにする為の前処理
        val nowTime = Calendar.getInstance()
        nowTime.add(Calendar.HOUR,-1)
        sCalendar.set(sDate.year,sDate.monthValue - 1,sDate.dayOfMonth,sTime.hour -1 ,sTime.minute,0)
        val eCalendar = Calendar.getInstance()
        eCalendar.set(eDate.year,eDate.monthValue - 1,eDate.dayOfMonth,eTime.hour -1 ,eTime.minute,0)
        if(sCalendar.timeInMillis > eCalendar.timeInMillis){
            Toast.makeText(activity as MainActivity, "開始日時を終了時刻より後にする事はできません。", Toast.LENGTH_SHORT).show()
            return
        }
        var newId = -1
        //編集でない場合は最新のIDを取得し、+1する
        if(scheduleObj == null){
            val maxId = realm.where(ScheduleObject::class.java).max("id")
            if(maxId != null){
                newId = maxId.toInt() + 1
            }
        }else{
            newId = scheduleObj!!.id!!
        }
        try {
            val dStartDate = sdFormat.parse(startDate)
            val dEndDate = sdFormat.parse(endDate)
            realm.executeTransaction{ realm ->
                if(scheduleObj == null){
                    val target = realm.createObject(ScheduleObject::class.java, newId)
                    target.title = mTitle
                    target.startDate = dStartDate
                    target.startTime = startTime
                    target.endDate = dEndDate
                    target.endTime = endTime
                    target.contents = contents
                }else{
                    val target = realm.where(ScheduleObject::class.java)
                        .equalTo("id", scheduleObj!!.id)
                        .findFirst()
                    target?.title = mTitle
                    target?.startDate = dStartDate
                    target?.startTime = startTime
                    target?.endDate = dEndDate
                    target?.endTime = endTime
                    target?.contents = contents
                }
            }
            if(nowTime.timeInMillis < sCalendar.timeInMillis){registNotification(sCalendar,newId,contents)}
            if(scheduleObj == null){
                showToast("登録に成功しました。")
            }else{
                showToast("変更に成功しました。id:$newId")
            }
        }catch (e : Exception){
            if(scheduleObj == null){
                showToast("登録に失敗しました。id:$newId")
            }else{
                showToast("変更に失敗しました。id:$newId")
            }
            showToast(e.message.toString())
        }
    }

    //通知処理
    private fun registNotification(calendar:Calendar,notifiId:Int?,contents: String){
        val intent = Intent(context, AlarmNotification::class.java)
        intent.putExtra("RequestCode", requestCode)
        pending = PendingIntent.getBroadcast(
            context, requestCode, intent, notifiId!!
        )
        // アラームをセットする
        am = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (am != null) {
            am!!.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis, pending
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}