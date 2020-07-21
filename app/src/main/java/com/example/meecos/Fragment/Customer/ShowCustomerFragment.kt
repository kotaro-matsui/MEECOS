package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.example.meecos.Config.drawUnderline
import com.example.meecos.Dialog.CommonDialogFragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm


class ShowCustomerFragment : BaseFragment(), CommonDialogFragment.CommonDialogListener {

    var mId: Int = 0
    var backSwitch: Boolean = true

    companion object {
        fun newInstance(id: Int, bs: Boolean): ShowCustomerFragment {
            val fragment = ShowCustomerFragment()
            fragment.mId = id
            fragment.backSwitch = bs
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

    private var mEditButton: Button? = null
    private var mDeleteButton: Button? = null
    private var cDialog: CommonDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_show_customer, container, false)
        view.setBackEvent(onBackListener)

        val customerObject = co.findCustomerById(mId)

        this.mHowToRead = view.findViewById(R.id.customer_how_to_read)
        this.mHowToRead!!.text = customerObject!!.howToRead

        this.mName = view.findViewById(R.id.customer_name)
        this.mName!!.text = customerObject.name

        this.mAddressNumber = view.findViewById(R.id.customer_address_number)
        this.mAddressNumber!!.text = customerObject.addressNumber

        this.mAddress = view.findViewById(R.id.customer_address)
        this.mAddress!!.text =
            (customerObject.topAddress + customerObject.bottomAddress).drawUnderline()
        this.mAddress!!.setOnClickListener(addressTextClickListener)

        this.mPhoneNumber = view.findViewById(R.id.customer_phone_number)
        this.mPhoneNumber!!.text = customerObject.phoneNumber.drawUnderline()
        this.mPhoneNumber!!.setOnClickListener(phoneNumberTextClickListener)

        this.mEditButton = view.findViewById(R.id.customer_edit)
        this.mEditButton!!.setOnClickListener(editButtonClickListener)

        this.mDeleteButton = view.findViewById(R.id.customer_delete)
        this.mDeleteButton!!.setOnClickListener(deleteButtonClickListener)

        setTitle("客先詳細")
        return view
    }

    //ツールバー右側に＋ボタンを追加する処理
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.back_item, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    //アイコン押した時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.back_page) {
            replaceFragment(CustomerFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private val addressTextClickListener = View.OnClickListener {
        this.cDialog = CommonDialogFragment.newInstance(
            "確認",
            "経路を検索しますか？",
            this
        )
        this.cDialog!!.show(parentFragmentManager, "root")
    }

    private val phoneNumberTextClickListener = View.OnClickListener {
        this.cDialog = CommonDialogFragment.newInstance(
            "確認",
            "ダイヤル画面に遷移しますか？",
            this
        )
        this.cDialog!!.show(parentFragmentManager, "dial")
    }

    private val editButtonClickListener = View.OnClickListener {
        replaceFragment(EditCustomerFragment.newInstance(mId))
    }

    private val deleteButtonClickListener = View.OnClickListener {
        val customerObject = co.findCustomerById(mId)
        this.cDialog = CommonDialogFragment.newInstance(
            "確認",
            "本当に削除しますか？",
            this
        )
        this.cDialog!!.show(parentFragmentManager, "delete")
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

    override fun onOkClick() {
        super.onOkClick()
        when (this.cDialog!!.tag) {
            "root" -> {
                connectGoogleMap(mAddress!!.text.toString())
            }
            "dial" -> {
                switchToDialScreen(mPhoneNumber!!.text.toString())
            }
            "delete" -> {
                realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    co.findCustomerById(mId)!!.deleteFromRealm()
                }
                replaceFragment(CustomerFragment())
                this.cDialog!!.dismiss()
            }
        }
    }

    override fun onNoClick() {
        super.onNoClick()
        this.cDialog!!.dismiss()
    }

    // 現状、前画面での戻るボタンのイベントを遷移先でも拾ってしまうため、1回のクリックで登録画面→(リスト画面)→HOME画面のような遷移が起こる
    //　実機でこの問題が起こらない場合は『replaceFragment(××Fragment())』の1文でよい
    private val onBackListener = object : BackEventListener {
        override fun onBackClick() {
            if (backSwitch) {
                replaceFragment(CustomerFragment.newInstance(false))
            } else {
                backSwitch = true
            }
        }
    }

}









