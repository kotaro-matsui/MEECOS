package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Manager.DataManager
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm
import kotlinx.coroutines.runBlocking
import java.util.*

class EditCustomerFragment : BaseFragment() {
    var mId: Int = 0

    companion object {
        fun newInstance(id: Int): EditCustomerFragment {
            val fragment = EditCustomerFragment()
            fragment.mId = id
            return fragment
        }
    }

    lateinit var realm: Realm

    private val co = CustomerObject()
    private val dm = DataManager()

    var mName: EditText? = null
    var mHowToRead: EditText? = null
    var mAddressNumber: EditText? = null
    var mTopAddress: EditText? = null
    var mBottomAddress: EditText? = null
    var mPhoneNumber: EditText? = null

    private var mSearchButton: Button? = null
    private var mEditButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_edit_customer, container, false)

        val customerObject = co.findCustomerById(mId)

        this.mName = view.findViewById(R.id.customer_name)
        this.mName!!.setText(customerObject!!.name)

        this.mHowToRead = view.findViewById(R.id.customer_how_to_read)
        this.mHowToRead!!.setText(customerObject.howToRead)

        this.mAddressNumber = view.findViewById(R.id.customer_address_number)
        this.mAddressNumber!!.setText(customerObject.addressNumber)

        this.mTopAddress = view.findViewById(R.id.customer_top_address)
        this.mTopAddress!!.setText(customerObject.topAddress)

        this.mBottomAddress = view.findViewById(R.id.customer_bottom_address)
        this.mBottomAddress!!.setText(customerObject.bottomAddress)

        this.mPhoneNumber = view.findViewById(R.id.customer_phone_number)
        this.mPhoneNumber!!.setText(customerObject.phoneNumber)

        this.mSearchButton = view.findViewById(R.id.search_address)
        this.mSearchButton!!.setOnClickListener {
            searchButtonClickListener(this.mAddressNumber!!.text.toString())
        }

        this.mEditButton = view.findViewById(R.id.customer_submit)
        this.mEditButton!!.setOnClickListener {
            editButtonClickListener(
                this.mName!!.text.toString()
                , this.mHowToRead!!.text.toString()
                , this.mAddressNumber!!.text.toString()
                , this.mTopAddress!!.text.toString()
                , this.mBottomAddress!!.text.toString()
                , this.mPhoneNumber!!.text.toString()
            )
        }

        setTitle("客先編集")
        return view
    }

    //ツールバー右側に戻るボタンを追加する処理
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.back_item, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }
    //アイコン押した時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.back_page){
            closeSoftKeyboard()
            replaceFragment(ShowCustomerFragment.newInstance(mId))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchButtonClickListener(addressNumber: String) {
        closeSoftKeyboard()
        // 郵便番号桁数バリデーションチェック
        if (addressNumber.length != 7) {
            AlertDialog.Builder(this.activity)
                .setTitle("入力エラー")
                .setMessage("7桁の郵便番号を入力して下さい")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()
        } else {
            // 『0000000』の形式の郵便番号を『000-0000』に変換
            val sb = StringBuilder()
            sb.append(addressNumber)
            sb.insert(3, "-")
            Log.d("TAG", "郵便番号の中身は$sb")

            val address = runBlocking {
                searchAddressFromZipCode(sb.toString())
            }

            // 郵便番号存在バリデーションチェック
            if (address == null) {
                AlertDialog.Builder(this.activity)
                    .setTitle("検索エラー")
                    .setMessage("住所が取得できませんでした")
                    .setPositiveButton("OK") { _, _ ->
                    }
                    .show()
            } else {
                val topAddress =
                    address.adminArea + address.locality + address.featureName
                this.mTopAddress!!.setText(topAddress, TextView.BufferType.NORMAL)
            }
        }
    }

    private fun editButtonClickListener(
        name: String
        , howToRead: String
        , addressNumber: String
        , topAddress: String
        , bottomAddress: String
        , phoneNumber: String
    ) {
        closeSoftKeyboard()
        // 客先名入力バリデーションチェック
        if (name == "") {
            AlertDialog.Builder(this.activity)
                .setTitle("入力エラー")
                .setMessage("客先名を入力して下さい")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()
        }
        // フリガナの全角カタカナバリデーションチェック
        else if (!howToRead.matches("^[\\u30A0-\\u30FF]+$".toRegex())) {
            AlertDialog.Builder(this.activity)
                .setTitle("入力エラー")
                .setMessage("フリガナは全角カタカナで入力して下さい")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()

            // 更新処理
        } else {
            realm = Realm.getDefaultInstance()
            val customerObject = co.findCustomerById(mId)
            val section = dm.selectSection(howToRead)

            realm.beginTransaction()

            customerObject!!.name = name
            customerObject.howToRead = howToRead
            customerObject.addressNumber = addressNumber
            customerObject.topAddress = topAddress
            customerObject.bottomAddress = bottomAddress
            customerObject.phoneNumber = phoneNumber
            customerObject.section = section
            Toast.makeText(activity as MainActivity, "変更が完了しました。", Toast.LENGTH_SHORT)
                .show()

            realm.commitTransaction()
        }
    }

    /**
     * 郵便番号から緯度と経度を取得し、対応するAddress取得する
     * "address.adminArea + address.locality + address.featureName"で都道府県・市区町村を表示
     * エラーが発生する可能性あり
     */
    private fun searchAddressFromZipCode(zipCode: String): Address? {
        if (zipCode.length != 8) {
            return null
        }

        val gc = Geocoder(this.activity, Locale.JAPAN)

        val address = gc.getFromLocationName(zipCode, 1)
        if (address != null && address.size != 0) {
            // latitudeが緯度、longitudeが軽度
            val latitude = address[0].latitude
            val longitude = address[0].longitude
            val addressList = gc.getFromLocation(latitude, longitude, 100)

            val filteredList =
                addressList.filter { x -> x.latitude == latitude && x.longitude == longitude }
            val sortedList = filteredList.sortedBy { x -> x.getAddressLine(0).length }
            sortedList.forEach { temp ->
                if (temp.featureName != zipCode) {
                    return temp
                }
            }
            return if (addressList.isEmpty()) address[0] else addressList[0]
        }
        return null
    }

    /**
     * キーボードを閉じる処理
     */
    private fun closeSoftKeyboard(){
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}