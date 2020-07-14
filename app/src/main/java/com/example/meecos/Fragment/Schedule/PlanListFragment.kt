package com.example.meecos.Fragment.Schedule

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class PlanListFragment : BaseFragment(), RecyclerViewHolder.ItemClickListener ,
    EditOrDeleteFragment.EditOrDeleteListener{
    private lateinit var realm:Realm
    private lateinit var latestPlans: RealmResults<ScheduleObject>
    private var selectObject: ScheduleObject? = null
    private var date:String? = null

    companion object {
        fun newInstance(date:String): PlanListFragment{
            val page = PlanListFragment()
            page.date = date
            return page
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_planlist,container,false)
        setTitle(date +"予定一覧")

        //予定表示する処理
        //Realmからレコード取得
        realm = Realm.getDefaultInstance()
        this.latestPlans = realm.where(ScheduleObject::class.java)
            .equalTo("startDate", date)
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
            val rpf = newInstance(this.date!!)
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
