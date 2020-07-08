package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.SimpleExpandableListAdapter
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import io.realm.RealmList as RealmList

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

//        Realm.init(requireActivity().applicationContext)
//        val config = RealmConfiguration.Builder()
//            .deleteRealmIfMigrationNeeded()
//            .build()
//        Realm.setDefaultConfiguration(config)

        //initしたインスタンスをとってくる
        realm = Realm.getDefaultInstance()

        // 親項目の作成
        val groupList = listOf("あ", "か", "さ", "た", "な", "は", "ま", "や", "ら", "わ", "#")
        createGroupList(groupList)

        // 子項目の作成

        /**
         * 各文字から始まるデータをソートした結果をrealmから取り出す
         * 各行ごとに取り出したデータを並べ、listにする
         * 行ごとに子項目をセットするための createChildList(list)を呼び出す　
         */

        // あ行リスト
        val aLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "ア")
                .or().beginsWith("howToRead", "イ")
                .or().beginsWith("howToRead", "ウ")
                .or().beginsWith("howToRead", "エ")
                .or().beginsWith("howToRead", "オ")
                .sort("howToRead")
                .findAll()

        val aLineStartRealmList = RealmList<CustomerObject>()
        aLineStartRealmList.addAll(aLineStartResults.subList(0, aLineStartResults.size))

        val aLineStartList = mutableListOf<String>()
        for (customer in aLineStartRealmList) {
            aLineStartList.add(customer.name)
        }
        createChildList(aLineStartList)

        // か行リスト
        val kLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "カ")
                .or().beginsWith("howToRead", "ガ")
                .or().beginsWith("howToRead", "キ")
                .or().beginsWith("howToRead", "ギ")
                .or().beginsWith("howToRead", "ク")
                .or().beginsWith("howToRead", "グ")
                .or().beginsWith("howToRead", "ケ")
                .or().beginsWith("howToRead", "ゲ")
                .or().beginsWith("howToRead", "コ")
                .or().beginsWith("howToRead", "ゴ")
                .sort("howToRead")
                .findAll()

        val kLineStartRealmList = RealmList<CustomerObject>()
        kLineStartRealmList.addAll(kLineStartResults.subList(0, kLineStartResults.size))

        val kLineStartList = mutableListOf<String>()
        for (customer in kLineStartRealmList) {
            kLineStartList.add(customer.name)
        }
        createChildList(kLineStartList)

        // さ行リスト
        val sLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "サ")
                .or().beginsWith("howToRead", "ザ")
                .or().beginsWith("howToRead", "シ")
                .or().beginsWith("howToRead", "ジ")
                .or().beginsWith("howToRead", "ス")
                .or().beginsWith("howToRead", "ズ")
                .or().beginsWith("howToRead", "セ")
                .or().beginsWith("howToRead", "ゼ")
                .or().beginsWith("howToRead", "ソ")
                .or().beginsWith("howToRead", "ゾ")
                .sort("howToRead")
                .findAll()

        val sLineStartRealmList = RealmList<CustomerObject>()
        sLineStartRealmList.addAll(sLineStartResults.subList(0, sLineStartResults.size))

        val sLineStartList = mutableListOf<String>()
        for (customer in sLineStartRealmList) {
            sLineStartList.add(customer.name)
        }
        createChildList(sLineStartList)

        // た行リスト
        val tLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "タ")
                .or().beginsWith("howToRead", "ダ")
                .or().beginsWith("howToRead", "チ")
                .or().beginsWith("howToRead", "ヂ")
                .or().beginsWith("howToRead", "ツ")
                .or().beginsWith("howToRead", "ヅ")
                .or().beginsWith("howToRead", "テ")
                .or().beginsWith("howToRead", "デ")
                .or().beginsWith("howToRead", "ト")
                .or().beginsWith("howToRead", "ド")
                .sort("howToRead")
                .findAll()

        val tLineStartRealmList = RealmList<CustomerObject>()
        tLineStartRealmList.addAll(tLineStartResults.subList(0, tLineStartResults.size))

        val tLineStartList = mutableListOf<String>()
        for (customer in tLineStartRealmList) {
            tLineStartList.add(customer.name)
        }
        createChildList(tLineStartList)

        // な行リスト
        val nLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "ナ")
                .or().beginsWith("howToRead", "ニ")
                .or().beginsWith("howToRead", "ヌ")
                .or().beginsWith("howToRead", "ネ")
                .or().beginsWith("howToRead", "ノ")
                .sort("howToRead")
                .findAll()

        val nLineStartRealmList = RealmList<CustomerObject>()
        nLineStartRealmList.addAll(nLineStartResults.subList(0, nLineStartResults.size))

        val nLineStartList = mutableListOf<String>()
        for (customer in nLineStartRealmList) {
            nLineStartList.add(customer.name)
        }
        createChildList(nLineStartList)

        // は行リスト
        val hLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "ハ")
                .or().beginsWith("howToRead", "バ")
                .or().beginsWith("howToRead", "パ")
                .or().beginsWith("howToRead", "ヒ")
                .or().beginsWith("howToRead", "ビ")
                .or().beginsWith("howToRead", "ピ")
                .or().beginsWith("howToRead", "フ")
                .or().beginsWith("howToRead", "ブ")
                .or().beginsWith("howToRead", "プ")
                .or().beginsWith("howToRead", "ヘ")
                .or().beginsWith("howToRead", "ベ")
                .or().beginsWith("howToRead", "ペ")
                .or().beginsWith("howToRead", "ホ")
                .or().beginsWith("howToRead", "ボ")
                .or().beginsWith("howToRead", "ポ")
                .sort("howToRead")
                .findAll()

        val hLineStartRealmList = RealmList<CustomerObject>()
        hLineStartRealmList.addAll(hLineStartResults.subList(0, hLineStartResults.size))

        val hLineStartList = mutableListOf<String>()
        for (customer in hLineStartRealmList) {
            hLineStartList.add(customer.name)
        }
        createChildList(hLineStartList)

        // ま行リスト
        val mLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "マ")
                .or().beginsWith("howToRead", "ミ")
                .or().beginsWith("howToRead", "ム")
                .or().beginsWith("howToRead", "メ")
                .or().beginsWith("howToRead", "モ")
                .sort("howToRead")
                .findAll()

        val mLineStartRealmList = RealmList<CustomerObject>()
        mLineStartRealmList.addAll(mLineStartResults.subList(0, mLineStartResults.size))

        val mLineStartList = mutableListOf<String>()
        for (customer in mLineStartRealmList) {
            mLineStartList.add(customer.name)
        }
        createChildList(mLineStartList)

        /// や行リスト
        val yLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "ヤ")
                .or().beginsWith("howToRead", "ユ")
                .or().beginsWith("howToRead", "ヨ")
                .sort("howToRead")
                .findAll()

        val yLineStartRealmList = RealmList<CustomerObject>()
        yLineStartRealmList.addAll(yLineStartResults.subList(0, yLineStartResults.size))

        val yLineStartList = mutableListOf<String>()
        for (customer in yLineStartRealmList) {
            yLineStartList.add(customer.name)
        }
        createChildList(yLineStartList)

        // ら行リスト
        val rLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "ラ")
                .or().beginsWith("howToRead", "リ")
                .or().beginsWith("howToRead", "ル")
                .or().beginsWith("howToRead", "レ")
                .or().beginsWith("howToRead", "ロ")
                .sort("howToRead")
                .findAll()

        val rLineStartRealmList = RealmList<CustomerObject>()
        rLineStartRealmList.addAll(rLineStartResults.subList(0, rLineStartResults.size))

        val rLineStartList = mutableListOf<String>()
        for (customer in rLineStartRealmList) {
            rLineStartList.add(customer.name)
        }
        createChildList(rLineStartList)

        // わ行リスト
        val wLineStartResults =
            realm.where(CustomerObject::class.java)
                .beginsWith("howToRead", "ワ")
                .or().beginsWith("howToRead", "ヲ")
                .or().beginsWith("howToRead", "ン")
                .sort("howToRead")
                .findAll()

        val wLineStartRealmList = RealmList<CustomerObject>()
        wLineStartRealmList.addAll(wLineStartResults.subList(0, wLineStartResults.size))

        val wLineStartList = mutableListOf<String>()
        for (customer in wLineStartRealmList) {
            wLineStartList.add(customer.name)
        }
        createChildList(wLineStartList)

        // その他(#)リスト
        val isNotReadableResults =
            realm.where(CustomerObject::class.java)
                .isEmpty("howToRead")
                .sort("id")
                .findAll()

        val isNotReadableRealmList = RealmList<CustomerObject>()
        isNotReadableRealmList.addAll(isNotReadableResults.subList(0, isNotReadableResults.size))

        val isNotReadableList = mutableListOf<String>()
        for (customer in isNotReadableRealmList) {
            isNotReadableList.add(customer.name)
        }
        createChildList(isNotReadableList)

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

             // アラート表示
            AlertDialog.Builder(this.activity)
                .setTitle(item["name"].toString())
                .setMessage("経路を検索しますか？")
                .setNegativeButton("cancel")  {_, _ ->
                }
                .setPositiveButton("OK") { _, _ ->
                    connectGoogleMap(item["name"].toString())
                }
                .show()

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

        for (initials in list) {
            val group = HashMap<String, String>()
            group["group"] = initials
            groupData.add(group)
        }

    }

    // 引数にしたリストで子項目を作成
    private fun createChildList(list: List<String>) {

        val childList =
            ArrayList<HashMap<String, String>>()

        for (place in list) {
            val child = HashMap<String, String>()
            child["name"] = place
            childList.add(child)
        }

        childData.add(childList)

    }

    //大阪駅から、引数に指定した場所への経路検索をGoogleMapで表示
    private fun connectGoogleMap(place: String){

        val uri = Uri.parse("https://www.google.com/maps/dir/大阪駅/$place")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        // Realmオブジェクトの削除
        realm.close()
    }

}