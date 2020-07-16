package com.example.meecos.Fragment.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
import com.example.meecos.Config.GROUP_LIST
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Manager.CustomerInfo
import com.example.meecos.Manager.DataManager
import com.example.meecos.R
import io.realm.*
import kotlinx.android.synthetic.main.parent_list.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CustomerFragment : BaseFragment()  {

    lateinit var realm : Realm
    private val dm = DataManager()

    // 親項目のリスト
    private val groupData =
        ArrayList<HashMap<String, String>>()

    // 子項目のリスト
    private val childData =
        ArrayList<ArrayList<HashMap<String, String>>>()

    var mSortList: ExpandableListView? = null

    private var mNewButton: ImageButton? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_customer, container, false)

        //ExpandableListViewの作成

        // 親項目の作成
        createGroupList(GROUP_LIST)

        // 子項目の作成
        for (ciList in dm.createChildList()) {
            if (ciList != null) {
                groupingChildList(ciList)
            }
        }

        // 親リスト、子リストを含んだAdapterを生成
        val adapter = SimpleExpandableListAdapter(
            requireActivity().applicationContext,
            groupData,
            R.layout.parent_list,
            arrayOf("group"),
            intArrayOf(R.id.first_letter),
            childData,
            R.layout.child_list,
            arrayOf("name", "id"),
            intArrayOf(R.id.customer_name)
        )

        // viewにセット
        this.mSortList = view.findViewById(R.id.customer_list) as ExpandableListView
        this.mSortList!!.setAdapter(adapter)

        // 親項目をクリックした際に、子項目が閉じたり開いたりする処理について
        // true -> 閉じたり開いたりしない
        // false -> 閉じたり開いたりする
        this.mSortList!!.setOnGroupClickListener { _, _, _, _ -> true }

        // 子項目を最初から開いておくための処理
        for (i in 0 until this.mSortList!!.expandableListAdapter.groupCount) {
            this.mSortList!!.expandGroup(i)
        }

         // 子項目がクリックされた時の処理
        this.mSortList!!.setOnChildClickListener { parent, _, groupPosition, childPosition, _ ->
            val exAdapter = parent.expandableListAdapter
            // クリックされた場所の内容情報を取得
            val item = exAdapter.getChild(
                groupPosition,
                childPosition
            ) as Map<*, *>

            realm = Realm.getDefaultInstance()
            replaceFragment(ShowCustomerFragment.newInstance(item["id"].toString().toInt()))
            false
        }

        this.mNewButton = view.findViewById<ImageButton>(R.id.new_customer)
        this.mNewButton!!.setOnClickListener(newButtonClickListener)

        setTitle("客先リスト")
        return view

    }

    private val newButtonClickListener = View.OnClickListener {
        replaceFragment(NewCustomerFragment())
    }

    // 引数にした文字で親項目を作成
    private fun createGroupList(list: List<String>) {
        for (s in list) {
            val group = HashMap<String, String>()
            group["group"] = s
            groupData.add(group)
        }
    }

    // realmからとってきたcustomerInfoを親項目に振り分け
    private fun groupingChildList(list: List<CustomerInfo>) {
        val childList =
            ArrayList<HashMap<String, String>>()
        for (ci in list) {
            val child = HashMap<String, String>()
            child["name"] = ci.name
            child["id"] = ci.id
            childList.add(child)
        }
        childData.add(childList)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // Realmオブジェクトの削除
//        realm.close()
//    }

}