package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class PlanListFragment : BaseFragment(), RecyclerViewHolder.ItemClickListener ,
    EditOrDeleteFragment.EditOrDeleteListener{
    private lateinit var realm:Realm
    private lateinit var latestPlans: RealmResults<ScheduleObject>
    private var selectObject: ScheduleObject? = null
    private var date: Date? = null
    private var strDate:String? = null

    companion object {
        fun newInstance(strDate:String): PlanListFragment{
            val page = PlanListFragment()
            val sdFormat = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
            val date = sdFormat.parse(strDate)
            page.date = date
            page.strDate = strDate
            return page
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_planlist,container,false)
        setTitle(this.strDate + "予定一覧")

        //予定表示する処理
        //Realmからレコード取得
        realm = Realm.getDefaultInstance()
        this.latestPlans = realm.where(ScheduleObject::class.java)
            .greaterThanOrEqualTo("endDate",date)
            .lessThanOrEqualTo("startDate",date)
            .sort("startDate")
            .findAll()

        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = RecyclerAdapter(
            (activity as MainActivity),
            this,
            latestPlans)
        recyclerView.layoutManager = LinearLayoutManager(
            (activity as MainActivity),
            LinearLayoutManager.VERTICAL, false)

        //RecyclerViewの各アイテムに罫線を付ける処理
        val divider =
            androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context,LinearLayoutManager(activity).orientation)
        ContextCompat.getDrawable(activity as MainActivity, R.drawable.divider)?.let { divider.setDrawable(it) };
        recyclerView.addItemDecoration(divider)

        return view
    }

    override fun onItemClick(view: View, position: Int) {
        //押したレコードの内容（ScheduleObject）を取得
        this.selectObject = latestPlans[position] as ScheduleObject
        //ダイアログを開き、編集と削除が選べるようにする
        val dialog = EditOrDeleteFragment.newInstance(this.selectObject!!, this, true)
        dialog.show((activity as MainActivity).supportFragmentManager, view::class.java.simpleName)
    }

    override fun onDialogPositiveClick() {
        if(!ScheduleObject().deleteByID(this.selectObject!!.id!!,context)){
            showToast("削除に失敗しました")
            return
        } else {
            val rpf = newInstance(this.strDate!!)
            replaceFragment(rpf)
            showToast("削除に成功しました")
        }
    }

    override fun onDialogNegativeClick() {
    }

    fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
