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
import android.widget.ImageButton
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

    private val co = CustomerObject()

    lateinit var realm: Realm

    var mName: TextView? = null
    var mHowToRead: TextView? = null
    var mAddressNumber: TextView? = null
    var mAddress: TextView? = null
    var mPhoneNumber: TextView? = null

    private var mBackButton: ImageButton? = null
    private var mEditButton: Button? = null
    private var mDeleteButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_customer, container, false)

        val customerObject = co.findCustomerById(mId)

        this.mBackButton = view.findViewById(R.id.back_list)
        this.mBackButton!!.setOnClickListener(backButtonClickListener)

        this.mHowToRead = view.findViewById(R.id.customer_how_to_read)
        this.mHowToRead!!.text = customerObject!!.howToRead

        this.mName = view.findViewById(R.id.customer_name)
        this.mName!!.text = customerObject.name

        this.mAddressNumber = view.findViewById(R.id.customer_address_number)
        this.mAddressNumber!!.text = customerObject.addressNumber

        this.mAddress = view.findViewById(R.id.customer_address)
        this.mAddress!!.text =
            drawUnderline(customerObject.topAddress + customerObject.bottomAddress)
        this.mAddress!!.setOnClickListener(addressTextClickListener)

        this.mPhoneNumber = view.findViewById(R.id.customer_phone_number)
        this.mPhoneNumber!!.text = drawUnderline(customerObject.phoneNumber)
        this.mPhoneNumber!!.setOnClickListener(phoneNumberTextClickListener)

        this.mEditButton = view.findViewById(R.id.customer_edit)
        this.mEditButton!!.setOnClickListener(editButtonClickListener)

        this.mDeleteButton = view.findViewById(R.id.customer_delete)
        this.mDeleteButton!!.setOnClickListener(deleteButtonClickListener)

        setTitle("客先詳細")
        return view
    }

    private val backButtonClickListener = View.OnClickListener {
        replaceFragment(CustomerFragment())
    }

    private val addressTextClickListener = View.OnClickListener {
        AlertDialog.Builder(this.activity)
            .setMessage("経路を検索しますか？")
            .setNegativeButton("cancel") { _, _ ->
            }
            .setPositiveButton("OK") { _, _ ->
                connectGoogleMap(mAddress!!.text.toString())
            }
            .show()
    }

    private val phoneNumberTextClickListener = View.OnClickListener {
        AlertDialog.Builder(this.activity)
            .setMessage("ダイヤル画面に遷移しますか？")
            .setNegativeButton("cancel") { _, _ ->
            }
            .setPositiveButton("OK") { _, _ ->
                switchToDialScreen(mPhoneNumber!!.text.toString())
            }
            .show()
    }

    private val editButtonClickListener = View.OnClickListener {
        AlertDialog.Builder(this.activity)
            .setMessage("編集しますか？")
            .setNegativeButton("cancel") { _, _ ->
            }
            .setPositiveButton("OK") { _, _ ->
                replaceFragment(EditCustomerFragment.newInstance(mId))
            }
            .show()
    }

    private val deleteButtonClickListener = View.OnClickListener {
        val customerObject = co.findCustomerById(mId)

        AlertDialog.Builder(this.activity)
            .setMessage("本当に削除しますか？")
            .setNegativeButton("cancel") { _, _ ->
            }
            .setPositiveButton("OK") { _, _ ->
                realm.executeTransaction {
                    customerObject!!.deleteFromRealm()
                }
                replaceFragment(CustomerFragment())
            }
            .show()
    }

    // 引数の文字列に下線を引く
    private fun drawUnderline(text: String): Spannable {
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









