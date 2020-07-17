package com.example.meecos.Dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R
import kotlinx.android.synthetic.main.fragment_newplan.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_MONTH

/* 日付を入力する時に使用するinnerクラス
   DialogFragmentで日付を入力できるカレンダーを表示する
   DialogFragmentを呼び出したボタンを取得したいので、コンストラクタの引数にButtonを定義する */
class DateDialogFragment(val textView: TextView,val view1: View) : DialogFragment() {
    /*  DatePickerDialogを返却するメソッド
        このメソッドで、日付を選択した後の処理や、日付範囲、Dialogのタイトルを設定する */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//      現在日付を取得
        val calendar = Calendar.getInstance()
        val dateDefault = context?.getString(R.string.schedule_date)

        return DatePickerDialog(
            activity as MainActivity,
            theme,
//              DialogFragmentで日付を選択し、OKを押したときの処理
            DatePickerDialog.OnDateSetListener{_, year, month, dayOfMonth ->
                val inputDate = Calendar.getInstance()
//              選択された日付を取得
                inputDate.set(year, month, dayOfMonth)
                val dfInputDate = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
//              CalendarからStringへ変換
                val strInputDate = dfInputDate.format(inputDate.time)
//              DialogFragmentを呼び出したボタンのテキストに日付をセット
                textView.text = strInputDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(DAY_OF_MONTH))
            .also {
                     /*選択可能な日付の上限を設定
                       終了日付が指定されている場合、開始日付は終了日付より後を選べないようにする */
                    if(textView === view1.startDateBtn && view1.endDateBtn.text != dateDefault){
                        val maxDate = Calendar.getInstance()
                        val dfMaxDate = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
                        //指定されている終了日付を取得
                        val endInputDate = dfMaxDate.parse(view1.endDateBtn.text.toString())
                        maxDate.time = endInputDate
                        //選択可能な開始日付の上限に指定されている終了日付を設定
                        it.datePicker.maxDate = maxDate.timeInMillis
                    }else{
                        //選択可能な開始日付の上限に現在日付を設定
                        /*it.datePicker.maxDate = calendar.timeInMillis*/
                    }
                    //選択可能な日付の下限を設定
                    val minDate = Calendar.getInstance()
                    //開始日付が指定されている場合、終了日付は開始日付より前を選べないようにする
                    if(textView === view1.endDateBtn && view1.startDateBtn.text != dateDefault){
                        val dfMinDate = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
                        val beginInputDate = dfMinDate.parse(view1.startDateBtn.text.toString())
                        minDate.time = beginInputDate
                        //選択可能な終了日付の下限に指定されている開始日付を設定
                        it.datePicker.minDate = minDate.timeInMillis
                    }else{
                        //選択可能な終了日付の下限を設定。今回は2018/1/1とする
                        minDate.set(2018, 1, 1)
                        it.datePicker.minDate = minDate.timeInMillis
                    }
                }

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        //cancelを押した際はセットされていた日付を削除し、"指定なし"をセット
        textView.text = context?.getString(R.string.schedule_date)
    }

}