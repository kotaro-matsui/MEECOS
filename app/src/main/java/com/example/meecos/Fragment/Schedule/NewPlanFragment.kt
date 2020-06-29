package com.example.meecos.Fragment.Schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R
import kotlinx.android.synthetic.main.fragment_newplan.*

class NewPlanFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_newplan, container,false)
        setTitle("予定作成・編集")

        //日付をダイアログで選択できるようにする
        val startDateBtn = view.findViewById<Button>(R.id.startDateBtn)
        startDateBtn.setOnClickListener{
            (activity as MainActivity).datePickDialog(startDateBtn)
        }
        val endDateBtn = view.findViewById<Button>(R.id.endDateBtn)
        endDateBtn.setOnClickListener{
            (activity as MainActivity).datePickDialog(endDateBtn)
        }

        //時間をダイアログで選択できるようにする
        val startTimeBtn = view.findViewById<Button>(R.id.startTimeBtn)
        startTimeBtn.setOnClickListener{
            (activity as MainActivity).timePickDialog(startTimeBtn)
        }

        val endTimeBtn = view.findViewById<Button>(R.id.endTimeBtn)
        endTimeBtn.setOnClickListener{
            (activity as MainActivity).timePickDialog(endTimeBtn)
        }
        return view
    }

}

//日付選択ダイアログを表示するメソッドを呼び出すクラス
private fun MainActivity.datePickDialog(button: Button) {
    DateDialogFragment(button).show(supportFragmentManager, button::class.java.simpleName)
}
//時間選択ダイアログを表示するメソッドを呼び出すクラス
private fun MainActivity.timePickDialog(button: Button) {
    TimeDialogFragment(button).show(supportFragmentManager, button::class.java.simpleName)
}