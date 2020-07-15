package com.example.meecos.Fragment.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.SimpleExpandableListAdapter
import com.example.meecos.Config.GROUP_LIST
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Manager.DataManager
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CustomerFragment : BaseFragment()  {

    lateinit var realm : Realm

    // 親項目のリスト
    // 親項目は会社名の頭文字をまとめる
    private val groupData =
        ArrayList<HashMap<String, String>>()

    // 子項目のリスト
    // 子項目は会社名となる
    private val childData =
        ArrayList<ArrayList<HashMap<String, String>>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_customer, container, false)

        val newCustomerButton = view.findViewById<ImageButton>(R.id.new_customer)
        newCustomerButton.setOnClickListener(onButtonClick)

        // 親項目の作成
        createGroupList(GROUP_LIST)

        // 子項目の作成

        val dm = DataManager()

        for (line in dm.createChildList()) {
            if (line != null) {
                groupingChildList(line)
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
            arrayOf("name"),
            intArrayOf(R.id.customer_name)
        )

        // viewにセット
        val lv = view.findViewById(R.id.customer_list) as ExpandableListView
        lv.setAdapter(adapter)

        // 親項目をクリックした際に、子項目が閉じたり開いたりする処理について
        // true -> 閉じたり開いたりしない
        // false -> 閉じたり開いたりする
        lv.setOnGroupClickListener { _, _, _, _ -> true }

        // 子項目を最初から開いておくための処理
        for (i in 0 until lv.expandableListAdapter.groupCount) {
            lv.expandGroup(i)
        }

         // 子項目がクリックされた時の処理
        lv.setOnChildClickListener { parent, _, groupPosition, childPosition, _ ->
            val exAdapter = parent.expandableListAdapter
            // クリックされた場所の内容情報を取得
            val item = exAdapter.getChild(
                groupPosition,
                childPosition
            ) as Map<*, *>

            //initしたインスタンスをとってくる
            realm = Realm.getDefaultInstance()

            val customerObject = realm.where(CustomerObject::class.java).equalTo("name", item["name"].toString()).findFirst()
            val customerId = customerObject!!.id!!.toInt()

            replaceFragment(ShowCustomerFragment.newInstance(customerId))

            false

        }

        setTitle("客先リスト")
        return view

    }

    private val onButtonClick = View.OnClickListener {
        replaceFragment(CreateCustomerFragment())
    }

    // 引数にした文字で親項目を作成
    private fun createGroupList(list: List<String>) {

        for (s in list) {
            val group = HashMap<String, String>()
            group["group"] = s
            groupData.add(group)
        }

    }

    // 引数にしたリストで子項目を作成
    private fun groupingChildList(list: List<String>) {

        val childList =
            ArrayList<HashMap<String, String>>()

        for (s in list) {
            val child = HashMap<String, String>()
            child["name"] = s
            childList.add(child)
        }

        childData.add(childList)

    }

//
//    override fun onDestroy() {
//        super.onDestroy()
//        // Realmオブジェクトの削除
//        realm.close()
//    }

}