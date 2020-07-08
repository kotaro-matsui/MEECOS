package com.example.meecos.Dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.meecos.Activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class TimeDialogFragment(val textView: TextView) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()

        return TimePickerDialog(
            activity as MainActivity,
            TimePickerDialog.OnTimeSetListener{_, hour, minute ->
                val inputTime = Calendar.getInstance()
                inputTime.set(1, 1, 1,hour, minute)
                val dfInputTime = SimpleDateFormat("HH:mm", Locale.US)
//              CalendarからStringへ変換
                val strInputTime = dfInputTime.format(inputTime.time)
//              DialogFragmentを呼び出したボタンのテキストに時刻をセット
                textView.text = strInputTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            /*DateFormat.is24HourFormat(activity)*/
            true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

    }
}
