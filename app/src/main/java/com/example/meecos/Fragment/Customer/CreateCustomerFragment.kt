package com.example.meecos.Fragment.Customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm
import io.realm.RealmConfiguration


class CreateCustomerFragment : BaseFragment(), TextWatcher {

    lateinit var realm : Realm

    private var customerName = "会社名"
    private var customerHowToRead = "読み方"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_customer, container, false)

        val createButton = view.findViewById<Button>(R.id.create_c)
        createButton.setOnClickListener(onButtonClick)

        val cName = view.findViewById<EditText>(R.id.customer_name)
        cName.addTextChangedListener(this)

        val cHowToRead = view.findViewById<EditText>(R.id.customer_how_to_read)
        cHowToRead.addTextChangedListener(this)

        setTitle("客先登録")
        return view
    }

    private val onButtonClick = View.OnClickListener {
        realmCreate()
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

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        if (requireView().id == R.id.customer_name) {
////
////        } else if (requireView().id == R.id.customer_how_to_read) {
////
////        }

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        if (requireView().id == R.id.customer_name) {
//
//        } else if (requireView().id == R.id.customer_how_to_read) {
//
//        }

    }

    override fun afterTextChanged(s: Editable?) {

        val inputStr = s.toString()

        if (requireView().id == R.id.customer_name) {
            customerName = inputStr
        } else if (requireView().id == R.id.customer_how_to_read) {
            customerHowToRead = inputStr
        }

//        when (requireView().id) {
//            R.id.customer_name -> customerName = inputStr
//            R.id.customer_how_to_read -> customerHowToRead = inputStr
//        }

    }

    private fun realmCreate(){

        Realm.init(requireActivity().applicationContext)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        //initしたインスタンスをとってくる
        realm = Realm.getDefaultInstance()

        // トランザクションして登録
        try {

            realm.executeTransaction { realm ->

                val obj = realm.createObject(CustomerObject::class.java, getNextUserId())
                obj.name = customerName
                obj.howToRead = customerHowToRead

            }

        } catch (e: Exception) {
            println("exceptionエラー:" + e.message)
        } catch (r: RuntimeException) {
            println("runtime exceptionエラー:" + r.message)
        }
    }

}