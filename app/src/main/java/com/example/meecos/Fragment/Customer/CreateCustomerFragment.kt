package com.example.meecos.Fragment.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm

class CreateCustomerFragment : BaseFragment() {

    lateinit var realm : Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_customer, container, false)

        val cName = view.findViewById<EditText>(R.id.customer_name)

        val cHowToRead = view.findViewById<EditText>(R.id.customer_how_to_read)

        val createButton = view.findViewById<Button>(R.id.customer_submit)
        createButton.setOnClickListener{onButtonClick(cName.text.toString(), cHowToRead.text.toString())}

        setTitle("客先登録")
        return view
    }

    private fun onButtonClick(cName: String, cHowToRead: String) {

//        Realm.init(requireActivity().applicationContext)
//        val config = RealmConfiguration.Builder()
//            .deleteRealmIfMigrationNeeded()
//            .build()
//        Realm.setDefaultConfiguration(config)

        //initしたインスタンスをとってくる
        realm = Realm.getDefaultInstance()

        // トランザクションして登録
        try {

            realm.executeTransaction { realm ->

                val obj = realm.createObject(CustomerObject::class.java, getNextUserId())
                obj.name = cName
                obj.howToRead = cHowToRead

                Toast.makeText(activity as MainActivity, "登録に成功しました。", Toast.LENGTH_SHORT).show()

            }

        } catch (e: Exception) {
            println("exceptionエラー:" + e.message)
        } catch (r: RuntimeException) {
            println("runtime exceptionエラー:" + r.message)
        }
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

}