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
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
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

        val createCustomerButton = view.findViewById<Button>(R.id.create_customer)
        createCustomerButton.setOnClickListener(onButtonClick)

        Realm.init(requireActivity().applicationContext)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        //initしたインスタンスをとってくる
        realm = Realm.getDefaultInstance()

        val customerCount = view.findViewById<TextView>(R.id.customer_count)
        customerCount.text = getNextUserId().toString()

//        // トランザクションして登録
//        try {
//            realm.executeTransaction { realm ->
//
//                realm.deleteAll()
//
//                val obj1 = realm.createObject(CustomerObject::class.java, 1)
//                obj1.name = "秋田駅"
//                obj1.howToRead = "アキタエキ"
//
//                val obj2 = realm.createObject(CustomerObject::class.java, 2)
//                obj2.name = "大阪駅"
//                obj2.howToRead = "オオサカエキ"
//
//                val obj3 = realm.createObject(CustomerObject::class.java, 3)
//                obj3.name = "淡路駅"
//                obj3.howToRead = "アワジエキ"
//
//                val obj4 = realm.createObject(CustomerObject::class.java, 4)
//                obj4.name = "神戸駅"
//                obj4.howToRead = "コウベエキ"
//
//                val obj5 = realm.createObject(CustomerObject::class.java, 5)
//                obj5.name = "茨木駅"
//                obj5.howToRead = "イバラキエキ"
//
//                val obj6 = realm.createObject(CustomerObject::class.java, 6)
//                obj6.name = "香川駅"
//                obj6.howToRead = "カガワエキ"
//
//                val obj7 = realm.createObject(CustomerObject::class.java, 7)
//                obj7.name = "北九州駅"
//                obj7.howToRead = "キタキュウシュウエキ"
//
//                val obj8 = realm.createObject(CustomerObject::class.java, 8)
//                obj8.name = "愛媛駅"
//
//
//            }
//
//        } catch (e: Exception) {
//            println("exceptionエラー:" + e.message)
//        } catch (r: RuntimeException) {
//            println("runtime exceptionエラー:" + r.message)
//        }

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
        val aStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "ア").sort("howToRead", Sort.DESCENDING).findAll()
        val iStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "イ").sort("howToRead", Sort.DESCENDING).findAll()
        val uStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "ウ").sort("howToRead", Sort.DESCENDING).findAll()
        val eStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "エ").sort("howToRead", Sort.DESCENDING).findAll()
        val oStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "オ").sort("howToRead", Sort.DESCENDING).findAll()

        val aLineStartRealmList = RealmList<CustomerObject>()
        aLineStartRealmList.addAll(aStartResults.subList(0, aStartResults.size))
        aLineStartRealmList.addAll(iStartResults.subList(0, iStartResults.size))
        aLineStartRealmList.addAll(uStartResults.subList(0, uStartResults.size))
        aLineStartRealmList.addAll(eStartResults.subList(0, eStartResults.size))
        aLineStartRealmList.addAll(oStartResults.subList(0, oStartResults.size))

        val aLineStartList = mutableListOf<String>()
        for (customer in aLineStartRealmList) {
            aLineStartList.add(customer.name)
        }
        createChildList(aLineStartList)

        // か行リスト
        val kaStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "カ").sort("howToRead", Sort.DESCENDING).findAll()
        val kiStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "キ").sort("howToRead", Sort.DESCENDING).findAll()
        val kuStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "ク").sort("howToRead", Sort.DESCENDING).findAll()
        val keStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "ケ").sort("howToRead", Sort.DESCENDING).findAll()
        val koStartResults = realm.where(CustomerObject::class.java).beginsWith("howToRead", "コ").sort("howToRead", Sort.DESCENDING).findAll()

        val kLineStartRealmList = RealmList<CustomerObject>()
        kLineStartRealmList.addAll(kaStartResults.subList(0, kaStartResults.size))
        kLineStartRealmList.addAll(kiStartResults.subList(0, kiStartResults.size))
        kLineStartRealmList.addAll(kuStartResults.subList(0, kuStartResults.size))
        kLineStartRealmList.addAll(keStartResults.subList(0, keStartResults.size))
        kLineStartRealmList.addAll(koStartResults.subList(0, koStartResults.size))

        val kLineStartList = mutableListOf<String>()
        for (customer in kLineStartRealmList) {
            kLineStartList.add(customer.name)
        }
        createChildList(kLineStartList)

        val allResults = realm.where(CustomerObject::class.java).findAll()

        val allRealmList = RealmList<CustomerObject>()
        allRealmList.addAll(allResults.subList(0, allResults.size))

        // さ行リスト
        val sList  = mutableListOf<String>()
        for (customer in allRealmList) {
            sList.add(customer.name)
        }
        createChildList(sList)

        // た行リスト
        val tList  = mutableListOf<String>()
        for (customer in allRealmList) {
            tList.add(customer.howToRead)
        }
        createChildList(tList)

        // な行リスト
        val nList = mutableListOf<String>()
        createChildList(nList)

        // は行リスト
        val hList = mutableListOf<String>()
        createChildList(hList)

        // ま行リスト
        val mList = mutableListOf<String>()
        createChildList(mList)

        // や行リスト
        val yList = mutableListOf<String>()
        createChildList(yList)

        // ら行リスト
        val rList = mutableListOf<String>()
        createChildList(rList)

        // わ行リスト
        val wList = mutableListOf<String>()
        createChildList(wList)

        // その他(#)リスト
        val zList = mutableListOf<String>()
        createChildList(zList)

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

    /**
     * UserのプライマリキーuserIdの最大値をインクリメントした値を取得する。
     * Userが1度も作成されていなければ1を取得する。
     */
    private fun getNextUserId(): Int {
        // 初期化
        var nextUserId = 1
        // userIdの最大値を取得
        val maxUserId: Number? = realm.where(CustomerObject::class.java).max("id")
        // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if (maxUserId != null) {
            nextUserId = maxUserId.toInt() + 1
        }
        return nextUserId
    }

    override fun onDestroy() {
        super.onDestroy()
        // Realmオブジェクトの削除
        realm.close()
    }

}