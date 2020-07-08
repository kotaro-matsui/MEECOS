package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R

class PlanListFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_planlist,container,false)
        setTitle("2020年○月○日 予定一覧")

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

    private val onEditBtnClick = View.OnClickListener {
        (activity as MainActivity).replaceFragment(NewPlanFragment(null))
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