package com.example.meecos.Fragment.Customer

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ExpandableListView
import android.widget.SimpleExpandableListAdapter
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Fragment.home.HomeFragment
import com.example.meecos.Listener.OnBackKeyPressedListener
import com.example.meecos.Manager.CustomerInfo
import com.example.meecos.Manager.DataManager
import com.example.meecos.R
import io.realm.Realm


class CustomerFragment : BaseFragment(), OnBackKeyPressedListener {

    lateinit var realm: Realm
    private val dm = DataManager()

    // 親項目のリスト
    private val groupData =
        ArrayList<HashMap<String, String>>()
    // 子項目のリスト
    private val childData =
        ArrayList<ArrayList<HashMap<String, String>>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_customer, container, false)

        //ExpandableListViewの作成

        // 親項目の作成
        setGroupList(dm.createGroupList())

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
        val sortList = view.findViewById(R.id.customer_list) as ExpandableListView
        sortList.setAdapter(adapter)
        Log.d("TAG", "fragmentのtagは" + CustomerFragment().tag)

        // 親項目をクリックした際に、子項目が閉じたり開いたりする処理について
        // true -> 閉じたり開いたりしない
        // false -> 閉じたり開いたりする
        sortList.setOnGroupClickListener { _, _, _, _ -> true }

        // 子項目を最初から開いておくための処理
        for (i in 0 until sortList.expandableListAdapter.groupCount) {
            sortList.expandGroup(i)
        }

        // 子項目がクリックされた時の処理
        sortList.setOnChildClickListener { parent, _, groupPosition, childPosition, _ ->
            val exAdapter = parent.expandableListAdapter
            // クリックされた場所の内容情報を取得
            val item = exAdapter.getChild(
                groupPosition,
                childPosition
            ) as Map<*, *>

            replaceFragment(ShowCustomerFragment.newInstance(item["id"].toString().toInt()))
            false
        }

        setTitle("客先リスト")
        return view
    }

    //ツールバー右側に＋ボタンを追加する処理
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    //アイコン押した時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.newPlan) {
            replaceFragment(NewCustomerFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    // 引数にしたListを親項目としてセット
    private fun setGroupList(list: List<String>) {
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
        if (list.isNotEmpty()) {
            for (ci in list) {
                Log.d("TAG", ci.name + "を振り分け中")
                val child = HashMap<String, String>()
                child["name"] = ci.name
                child["id"] = ci.id
                childList.add(child)
            }
            childData.add(childList)
        }
    }

    override fun onBackPressed() {
        replaceFragment(HomeFragment())
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // Realmオブジェクトの削除
//        realm.close()
//    }

}