package com.example.meecos.Fragment.Schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Dialog.EditOrDeleteFragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import com.example.meecos.RecyclerView.RecyclerAdapter
import com.example.meecos.RecyclerView.RecyclerViewHolder
import io.realm.Realm
import io.realm.RealmResults

class ScheduleFragment : BaseFragment(), RecyclerViewHolder.ItemClickListener,
    EditOrDeleteFragment.EditOrDeleteListener{
    private lateinit var realm:Realm
    private lateinit var latestPlans:RealmResults<ScheduleObject>
    private var selectObject: ScheduleObject? = null

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
            //TODO:DBを日付で検索し、該当したデータをPlanListFragmentに渡す
            (activity as MainActivity).replaceFragment(PlanListFragment())
        }

        //予定新規作成画面への遷移
        val newPlanBtn = view.findViewById<ImageButton>(R.id.newPlan)
        newPlanBtn.setOnClickListener(onCreatePlanBtn)

        //直近の予定5件表示する処理
        //Realmからレコード取得
        realm = Realm.getDefaultInstance()
        this.latestPlans = realm.where(ScheduleObject::class.java)
            .sort("startDate")
            /*.limit(5)*/
            .findAll()

        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = RecyclerAdapter(
            (activity as MainActivity),
            this,
            latestPlans)
        recyclerView.layoutManager = LinearLayoutManager(
            (activity as MainActivity),
            LinearLayoutManager.VERTICAL, false)

        return view
    }

    //各レコードを押した時の処理
    override fun onItemClick(view: View, position: Int) {
        //押したレコードの内容（ScheduleObject）を取得
        this.selectObject = latestPlans[position] as ScheduleObject
        //ダイアログを開き、編集と削除が選べるようにする
        val dialog = EditOrDeleteFragment.newInstance(this.selectObject!!, this)
        dialog.show((activity as MainActivity).supportFragmentManager, view::class.java.simpleName)
    }
    //新規作成画面への遷移
    private val onCreatePlanBtn = View.OnClickListener {
        (activity as MainActivity).replaceFragment(NewPlanFragment(null))
    }
    override fun onDialogPositiveClick() {
        if(!ScheduleObject().deleteByID(this.selectObject!!.id!!)){
            showToast("削除に失敗しました")
            return
        } else {
            replaceFragment(ScheduleFragment())
            showToast("削除に成功しました")
        }
    }

    override fun onDialogNegativeClick() {
        TODO("Not yet implemented")
    }

    fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}