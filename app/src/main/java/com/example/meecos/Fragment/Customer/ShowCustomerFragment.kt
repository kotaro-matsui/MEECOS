package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.example.meecos.Config.drawUnderline
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

    private var mEditButton: Button? = null
    private var mDeleteButton: Button? = null

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
        if(item.itemId == R.id.back_page){
            replaceFragment(CustomerFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private val addressTextClickListener = View.OnClickListener {
//        this.cDialog = CommonDialogFragment.newInstance(
//            "確認",
//            "経路を検索しますか？",
//            this
//        )
//        this.cDialog!!.show(parentFragmentManager, "dialog")
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
//        val cDialog = CommonDialogFragment.newInstance(
//            "確認",
//            "ダイヤル画面に遷移しますか？",
//            this
//        )
//        cDialog.show(parentFragmentManager, "dialog")
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
        replaceFragment(EditCustomerFragment.newInstance(mId))
    }

    private val deleteButtonClickListener = View.OnClickListener {
        val customerObject = co.findCustomerById(mId)
//        this.cDialog = CommonDialogFragment.newInstance(
//            "確認",
//            "本当に削除しますか？",
//            this
//        )
//        this.cDialog!!.show(parentFragmentManager, "dialog")
        AlertDialog.Builder(this.activity)
            .setMessage("本当に削除しますか？")
            .setNegativeButton("cancel") { _, _ ->
            }
            .setPositiveButton("OK") { _, _ ->
                realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    customerObject!!.deleteFromRealm()
                }
                replaceFragment(CustomerFragment())
            }
            .show()
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

    private val onBackListener = object : BackEventListener {
        override fun onBackClick() {
            replaceFragment(CustomerFragment())
        }
    }

//    override fun onOkClick() {
//        super.onOkClick()
//        this.cDialog!!.dismiss()
//    }
//
//    override fun onNoClick() {
//        super.onNoClick()
//        this.cDialog!!.dismiss()
//    }

}









