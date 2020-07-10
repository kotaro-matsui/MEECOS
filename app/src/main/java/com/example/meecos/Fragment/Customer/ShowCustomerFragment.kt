package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm

class ShowCustomerFragment : BaseFragment() {

    // idという変数名で宣言すると、自動で生成されるgetterやsetterが既に存在するメソッドと混同してエラーが起きる…？
    var mId: Int = 0

    companion object {
        fun newInstance(id: Int): ShowCustomerFragment {
            val fragment = ShowCustomerFragment()
            fragment.mId = id
            return fragment
        }
    }

    lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_customer, container, false)

        //initしたインスタンスをとってくる
        realm = Realm.getDefaultInstance()
        val customerObject =
            realm.where(CustomerObject::class.java).equalTo("id", this.mId).findFirst()

        val customerHowToRead = view.findViewById<TextView>(R.id.customer_how_to_read)
        customerHowToRead.text = customerObject!!.howToRead

        val customerName = view.findViewById<TextView>(R.id.customer_name)
        val list: MutableList<String>? = null
        customerName.text = customerObject.name

        val customerAddressNumber = view.findViewById<TextView>(R.id.customer_address_number)
        customerAddressNumber.text = customerObject.addressNumber

        val customerAddress = view.findViewById<TextView>(R.id.customer_address)

        val address = customerObject.topAddress + customerObject.bottomAddress
        // 1. ファクトリーにおまかせ
        val t = Spannable.Factory.getInstance().newSpannable(address.toString())
        // 2. 下線オブジェクト(他にも種類ある)
        val us = UnderlineSpan()
        // 3. 装飾セット(装飾オブジェクト、開始位置、終了位置、装飾オブジェクト用？フラグ)
        t.setSpan(us, 0, address.toString().length, t.getSpanFlags(us))
        //　下線を引いた文字列をセット
        customerAddress.text = t
        customerAddress.setOnClickListener {
            // アラート表示
            AlertDialog.Builder(this.activity)
                .setMessage("経路を検索しますか？")
                .setNegativeButton("cancel") { _, _ ->
                }
                .setPositiveButton("OK") { _, _ ->
                    connectGoogleMap(address)
                }
                .show()
        }

        val customerPhoneNumber = view.findViewById<TextView>(R.id.customer_phone_number)
        customerPhoneNumber.text = customerObject.phoneNumber

        // 削除ボタン
        val deleteCustomerButton = view.findViewById<Button>(R.id.customer_delete)
        deleteCustomerButton.setOnClickListener {
            realm.executeTransaction {
                customerObject.deleteFromRealm()
            }
            replaceFragment(CustomerFragment())
        }

        setTitle("客先詳細")
        return view
    }

    //大阪駅から、引数に指定した場所への経路検索をGoogleMapで表示
    private fun connectGoogleMap(place: String) {

        val uri = Uri.parse("https://www.google.com/maps/dir/大阪駅/$place")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

    }
}









