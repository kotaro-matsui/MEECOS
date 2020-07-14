package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm
import java.io.IOException
import java.util.*

class CreateCustomerFragment : BaseFragment() {

    lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_customer, container, false)

        val backListButton = view.findViewById<ImageButton>(R.id.back_list)
        backListButton.setOnClickListener{
            replaceFragment(CustomerFragment())
        }

        val customerName = view.findViewById<EditText>(R.id.customer_name)

        val customerHowToRead = view.findViewById<EditText>(R.id.customer_how_to_read)

        val customerAddressNumber = view.findViewById<EditText>(R.id.customer_address_number)

        val customerTopAddress = view.findViewById<EditText>(R.id.customer_top_address)

        val searchButton = view.findViewById<Button>(R.id.search_address)
        searchButton.setOnClickListener {

            // 現状、エラーが発生したりしなかったりして、原因が解明できていない
            // grpc failed at android.location.Geocoder.getFromLocationName

            if (customerAddressNumber.text.toString().length == 7) {
                // 『0000000』の形式の郵便番号を『000-0000』に変換
                val sb = StringBuilder()
                sb.append(customerAddressNumber.text.toString())
                sb.insert(3, "-")

                Log.d("TAG", "郵便番号の中身は$sb")

                val address = searchAddressFromZipCode(sb.toString())
                if (address != null) {
                    val customerAddress =
                        address.adminArea + address.locality + address.featureName
                    customerTopAddress.setText(customerAddress, TextView.BufferType.NORMAL)
                } else {
                    AlertDialog.Builder(this.activity)
                        .setTitle("検索エラー")
                        .setMessage("住所が取得できませんでした")
                        .setPositiveButton("OK") { _, _ ->
                        }
                        .show()
                }

            } else {
                AlertDialog.Builder(this.activity)
                    .setTitle("入力エラー")
                    .setMessage("7桁の郵便番号を入力して下さい")
                    .setPositiveButton("OK") { _, _ ->
                    }
                    .show()
            }

        }

        val customerBottomAddress = view.findViewById<EditText>(R.id.customer_bottom_address)

        val customerPhoneNumber = view.findViewById<EditText>(R.id.customer_phone_number)

        val createButton = view.findViewById<Button>(R.id.customer_submit)
        createButton.setOnClickListener {
            onButtonClick(
                customerName.text.toString()
                , customerHowToRead.text.toString()
                , customerAddressNumber.text.toString()
                , customerTopAddress.text.toString()
                , customerBottomAddress.text.toString()
                , customerPhoneNumber.text.toString()
            )
        }

        setTitle("客先登録")
        return view
    }

    private fun onButtonClick(
        customerName: String
        , customerHowToRead: String
        , customerAddressNumber: String
        , customerTopAddress: String
        , customerBottomAddress: String
        , customerPhoneNumber: String
    ) {
        // 客先名入力バリデーションチェック
        if (customerName == "") {
            AlertDialog.Builder(this.activity)
                .setTitle("入力エラー")
                .setMessage("客先名を入力して下さい")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()
        }
        // フリガナの全角カタカナバリデーションチェック
        else if (!customerHowToRead.matches("^[\\u30A0-\\u30FF]+$".toRegex())) {
            AlertDialog.Builder(this.activity)
                .setTitle("入力エラー")
                .setMessage("フリガナは全角カタカナで入力して下さい")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()
        } else {

            //initしたインスタンスをとってくる
            realm = Realm.getDefaultInstance()

            // トランザクションして登録
            try {

                realm.executeTransaction { realm ->

                    val obj = realm.createObject(CustomerObject::class.java, getNextUserId())
                    obj.name = customerName
                    obj.howToRead = customerHowToRead
                    obj.addressNumber = customerAddressNumber
                    obj.topAddress = customerTopAddress
                    obj.bottomAddress = customerBottomAddress
                    obj.phoneNumber = customerPhoneNumber

                    val toast =
                        Toast.makeText(activity as MainActivity, "登録に成功しました。", Toast.LENGTH_SHORT)

                    toast.setGravity(Gravity.CENTER, 0, -200)
                    toast.show()

                }

            } catch (e: Exception) {
                println("exceptionエラー:" + e.message)
            } catch (r: RuntimeException) {
                println("runtime exceptionエラー:" + r.message)
            }
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

    private fun searchAddressFromZipCode(zipCode: String): Address? {
        if (zipCode.length != 8) {
            return null
        }

        val geocoder = Geocoder(this.activity, Locale.JAPAN)

        try {

            val address = geocoder.getFromLocationName(zipCode, 1)
            if (address != null && address.size != 0) {
                // latitudeが緯度、longitudeが軽度
                val latitude = address[0].latitude
                val longitude = address[0].longitude
                val addressList = geocoder.getFromLocation(latitude, longitude, 100)

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

        } catch (e: IOException) {

            e.printStackTrace()
            Log.d("TAG", "やばいやつ")

        }

        return null
    }

}