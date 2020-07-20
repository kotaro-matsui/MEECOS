package com.example.meecos.Dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class YearMonthPickerDialogFragment(
    private val year: Int,
    private val month: Int,
    private val onDateSetListener: OnDateSetListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            YearMonthPickerDialog(
                activity,
                onDateSetListener,
                year,
                month,
                1
            )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val datePicker = dialog.datePicker
        val dayId =
            Resources.getSystem().getIdentifier("day", "id", "android")
        datePicker.findViewById<View>(dayId).visibility = View.GONE
        dialog.onDateChanged(datePicker, year, month, 1)
        return dialog
    }

    internal class YearMonthPickerDialog : DatePickerDialog {
        constructor(
            context: Context?,
            callBack: OnDateSetListener?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) : super(context!!, callBack, year, monthOfYear, dayOfMonth) {
        }

        constructor(
            context: Context?,
            theme: Int,
            callBack: OnDateSetListener?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) : super(context!!, theme, callBack, year, monthOfYear, dayOfMonth) {
        }

        override fun onDateChanged(
            view: DatePicker,
            year: Int,
            month: Int,
            day: Int
        ) {
            val cal = Calendar.getInstance()
            cal[year, month] = day
            val title = SimpleDateFormat("yyyy-M").format(cal.time)
            setTitle(title)
        }
    }

}