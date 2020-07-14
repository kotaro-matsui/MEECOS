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


        realm = Realm.getDefaultInstance()

        val customerObject =
            realm.where(CustomerObject::class.java).equalTo("id", this.mId).findFirst()

        val customerHowToRead = view.findViewById<TextView>(R.id.customer_how_to_read)
        customerHowToRead.text = customerObject!!.howToRead

        val customerName = view.findViewById<TextView>(R.id.customer_name)
        customerName.text = customerObject.name

        val customerAddressNumber = view.findViewById<TextView>(R.id.customer_address_number)
        customerAddressNumber.text = customerObject.addressNumber
        val customerAddress = view.findViewById<TextView>(R.id.customer_address)
        val address = customerObject.topAddress + customerObject.bottomAddress
        customerAddress.text = drawUnderline(address)
        customerAddress.setOnClickListener {
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
        customerPhoneNumber.text = drawUnderline(customerObject.phoneNumber)
        customerPhoneNumber.setOnClickListener {
            AlertDialog.Builder(this.activity)
                .setMessage("ダイヤル画面に遷移しますか？")
                .setNegativeButton("cancel") { _, _ ->
                }
                .setPositiveButton("OK") { _, _ ->
                    switchToDialScreen(customerObject.phoneNumber)
                }
                .show()
        }

        val editCustomerButton = view.findViewById<Button>(R.id.customer_edit)
        editCustomerButton.setOnClickListener {
            AlertDialog.Builder(this.activity)
                .setMessage("編集しますか？")
                .setNegativeButton("cancel") { _, _ ->
                }
                .setPositiveButton("OK") { _, _ ->
                    replaceFragment(EditCustomerFragment.newInstance(mId))
                }
                .show()
        }

        val deleteCustomerButton = view.findViewById<Button>(R.id.customer_delete)
        deleteCustomerButton.setOnClickListener {
            AlertDialog.Builder(this.activity)
                .setMessage("本当に削除しますか？")
                .setNegativeButton("cancel") { _, _ ->
                }
                .setPositiveButton("OK") { _, _ ->
                    realm.executeTransaction {
                        customerObject.deleteFromRealm()
                    }
                    replaceFragment(CustomerFragment())
                }
                .show()
        }

        setTitle("客先詳細")
        return view
    }

    // 引数の文字列に下線を引く
    private fun drawUnderline (text: String) : Spannable{
        // 1. ファクトリーにおまかせ
        val t = Spannable.Factory.getInstance().newSpannable(text)
        // 2. 下線オブジェクト(他にも種類ある)
        val us = UnderlineSpan()
        // 3. 装飾セット(装飾オブジェクト、開始位置、終了位置、装飾オブジェクト用？フラグ)
        t.setSpan(us, 0, text.length, t.getSpanFlags(us))
        return t
    }

    // 引数に指定した場所への経路検索をGoogleMapで表示
    private fun connectGoogleMap(place: String) {
        val uri = Uri.parse("https://www.google.com/maps/dir/新藤田ビル/$place")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    // 引数の番号をセットしてダイヤル画面に遷移
    private fun switchToDialScreen(num: String) {
        val uri = Uri.parse("tel:$num")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
            }

}









