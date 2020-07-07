package com.example.meecos.Fragment.Customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.SimpleExpandableListAdapter
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R


class CustomerListFragment : BaseFragment()  {

    // 親リスト
    private val groupData =
        ArrayList<HashMap<String, String>>()

    // 子リスト
    private val childData =
        ArrayList<ArrayList<HashMap<String, String>>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_customer_list, container, false)

        // 親リストに要素を追加

        // 親ノードのリストに内容を格納
        createParentList("あ")
        createParentList("か")
//        createParentList("さ")
//        createParentList("た")
//        createParentList("な")
//        createParentList("は")
//        createParentList("ま")
//        createParentList("や")
//        createParentList("ら")
//        createParentList("わ")
//        createParentList("#")

        // あ行の子項目を作成

        val childListA =
            ArrayList<HashMap<String, String>>()
        val childAA = HashMap<String, String>()
        childAA["group"] = "あ"
        childAA["name"] = "秋田"
        val childAB = HashMap<String, String>()
        childAB["group"] = "あ"
        childAB["name"] = "大阪"
        val childAC = HashMap<String, String>()
        childAC["group"] = "あ"
        childAC["name"] = "沖縄"

        childListA.add(childAA)
        childListA.add(childAB)
        childListA.add(childAC)
        childData.add(childListA)

        // か行の子項目を作成

        val childListK =
            ArrayList<HashMap<String, String>>()
        val childKA = HashMap<String, String>()
        childKA["group"] = "か"
        childKA["name"] = "香川"
        val childKB = HashMap<String, String>()
        childKB["group"] = "か"
        childKB["name"] = "京都"

        childListK.add(childKA)
        childListK.add(childKB)
        childData.add(childListK)



//        // あ行リスト
//        createChildAList("秋田")
//        createChildAList("大阪")
//        createChildAList("沖縄")
//
//        //か行リスト
//        createChildAList("香川")
//        createChildAList("京都")

        // 親リスト、子リストを含んだAdapterを生成
        val adapter = SimpleExpandableListAdapter(
            requireActivity().applicationContext,
            groupData,
            R.layout.parent_list,
            arrayOf("group"),
            intArrayOf(R.id.first_letter),
            childData,
            R.layout.child_list,
            arrayOf("name", "group"),
            intArrayOf(R.id.customer_name)
        )

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

         //リスト項目がクリックされた時の処理
        lv.setOnChildClickListener { parent, _, groupPosition, childPosition, _ ->
            val exAdapter = parent.expandableListAdapter
            // クリックされた場所の内容情報を取得
            val item = exAdapter.getChild(
                groupPosition,
                childPosition
            ) as Map<*, *>
            // 経路検索
            connectGoogleMap(item["name"].toString())

            false
        }

        return view

    }

    // 引数にした文字でセクション表示を作成
    private fun createParentList(firstLetter: String) {

        val group = HashMap<String, String>()
        group["group"] = firstLetter
        groupData.add(group)

    }

    // あ行の子項目を作成
    private fun createChildAList(customerName: String) {

        val childList =
            ArrayList<HashMap<String, String>>()
        val child = HashMap<String, String>()
        child["group"] = "あ"
        child["name"] = customerName

        childList.add(child)
        childData.add(childList)



    }

    // か行の子項目を作成
    private fun createChildKList(customerName: String) {

        val childList =
            ArrayList<HashMap<String, String>>()
        val child = HashMap<String, String>()
        child["group"] = "か"
        child["name"] = customerName

        childList.add(child)
        childData.add(childList)

    }

    //大阪駅から、引数に指定した場所への経路検索をGoogleMapで表示
    private fun connectGoogleMap(place: String){

        val uri = Uri.parse("https://www.google.com/maps/dir/大阪駅/$place")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

    }

}