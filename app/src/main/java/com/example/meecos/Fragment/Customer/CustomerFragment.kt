package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.SimpleExpandableListAdapter
import androidx.lifecycle.ViewModel
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.*
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*
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
    private val childData =
        ArrayList<ArrayList<HashMap<String, String>>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Realm.init(this.activity)
        val config = RealmConfiguration.Builder()
            // .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        //initしたインスタンスをとってくる
        realm = Realm.getDefaultInstance()
        // トランザクションして登録
        try {
            realm.executeTransaction { realm ->
                val obj1 = realm.createObject(CustomerObject::class.java, getNextUserId())
                obj1.customerName = "秋田駅"
                obj1.customerName = "アキタエキ"

                val obj2 = realm.createObject(CustomerObject::class.java, getNextUserId())
                obj2.customerName = "青森駅"
                obj2.customerName = "アオモリエキ"

                val obj3 = realm.createObject(CustomerObject::class.java, getNextUserId())
                obj3.customerName = "淡路駅"

                val obj4 = realm.createObject(CustomerObject::class.java, getNextUserId())
                obj4.customerName = "神戸駅"
                obj4.customerName = "アカシエキ"
            }
        } catch (e: Exception) {
            println("exceptionエラー:" + e.message)
        } catch (r: RuntimeException) {
            println("runtime exceptionエラー:" + r.message)
        }

        val view = inflater.inflate(R.layout.fragment_customer, container, false)

        // 親項目の作成
        val initialsList = listOf("あ", "か", "さ", "た", "な", "は", "ま", "や", "ら", "わ", "#")
        createGroupList(initialsList)

        // 子項目の作成

        // 各文字から始まるデータをソートしたlistをrealmから取り出す
        // 現状、1つの子項目ごとにcreateChildList(list)の呼び出しが必要

        val testResults = realm.where(CustomerObject::class.java).equalTo("customerHowToRead", "アキタエキ").findFirst()

        val aStartResults = realm.where(CustomerObject::class.java).beginsWith("customerHowToRead", "ア").sort("customerHowToRead", Sort.DESCENDING).findAll()

        val aStartList = RealmList<CustomerObject>()
        aStartList.addAll(aStartResults.subList(0, aStartResults.size))

        // あ行リスト
        val aList = mutableListOf<String>()
        aList.add("あああああ")
        aList.add(testResults!!.customerName)
        aList.add("ああいああ")
//        for (item in aStartList) {
//            aList.add(item.customerName)
//        }
        createChildList(aList)

        // か行リスト
        val kList = mutableListOf<String>()
        createChildList(kList)

        // さ行リスト
        val sList  = mutableListOf<String>()
        createChildList(sList)

        // た行リスト
        val tList  = mutableListOf<String>()
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

        return view

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