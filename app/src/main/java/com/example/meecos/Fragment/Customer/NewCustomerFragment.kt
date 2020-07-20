package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Fragment.home.HomeFragment
import com.example.meecos.Listener.OnBackKeyPressedListener
import com.example.meecos.Manager.DataManager
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm
import kotlinx.coroutines.runBlocking
import java.util.*

class NewCustomerFragment : BaseFragment() {
    lateinit var realm: Realm

    private val dm = DataManager()

    var mName: EditText? = null
    var mHowToRead: EditText? = null
    var mAddressNumber: EditText? = null
    var mTopAddress: EditText? = null
    var mBottomAddress: EditText? = null
    var mPhoneNumber: EditText? = null

    private var mSearchButton: Button? = null
    private var mCreateButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_new_customer, container, false)
        view.setBackEvent(onBackListener)

        view.isFocusableInTouchMode = true
        view.requestFocus()

        this.mName = view.findViewById(R.id.customer_name)

        this.mHowToRead = view.findViewById(R.id.customer_how_to_read)

        this.mAddressNumber = view.findViewById(R.id.customer_address_number)

        this.mTopAddress = view.findViewById(R.id.customer_top_address)

        this.mBottomAddress = view.findViewById(R.id.customer_bottom_address)

        this.mPhoneNumber = view.findViewById(R.id.customer_phone_number)

        this.mSearchButton = view.findViewById(R.id.search_address)
        this.mSearchButton!!.setOnClickListener {
            searchButtonClickListener(this.mAddressNumber!!.text.toString())
        }

        this.mCreateButton = view.findViewById<Button>(R.id.customer_submit)
        this.mCreateButton!!.setOnClickListener {
            createButtonClickListener(
                this.mName!!.text.toString()
                , this.mHowToRead!!.text.toString()
                , this.mAddressNumber!!.text.toString()
                , this.mTopAddress!!.text.toString()
                , this.mBottomAddress!!.text.toString()
                , this.mPhoneNumber!!.text.toString()
            )
        }

        setTitle("客先登録")
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
            closeSoftKeyboard()
            replaceFragment(CustomerFragment())
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

    private fun createButtonClickListener(
        name: String
        , howToRead: String
        , addressNumber: String
        , topAddress: String
        , bottomAddress: String
        , phoneNumber: String
    ) {
        closeSoftKeyboard()
        // 客先名入力バリデーションチェック
        if (name.isBlank()) {
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
        } else {
            realm = Realm.getDefaultInstance()

            val section = dm.selectSection(howToRead)

            // トランザクションして登録
            try {
                realm.executeTransaction { realm ->
                    val obj = realm.createObject(CustomerObject::class.java, getNextUserId())
                    obj.name = name
                    obj.howToRead = howToRead
                    obj.addressNumber = addressNumber
                    obj.topAddress = topAddress
                    obj.bottomAddress = bottomAddress
                    obj.phoneNumber = phoneNumber
                    obj.section = section
                    Toast.makeText(activity as MainActivity, "登録が完了しました。", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                println("exceptionエラー:" + e.message)
            } catch (r: RuntimeException) {
                println("runtime exceptionエラー:" + r.message)
            }
        }
    }

    /**
     * Idの最大値をインクリメントした値を取得する。
     * CustomerObjectが1度も作成されていなければ1を取得する。
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

        /**
         * 問題のエラー発生個所
         */
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
    private fun closeSoftKeyboard() {
        val inputManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private val onBackListener = object : BackEventListener {
        override fun onBackClick() {
            replaceFragment(CustomerFragment())
        }
    }

}

