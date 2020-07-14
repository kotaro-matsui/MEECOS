package com.example.meecos.Fragment.Customer

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.R
import io.realm.Realm
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_customer, container, false)

        realm = Realm.getDefaultInstance()

        val customerObject =
            realm.where(CustomerObject::class.java).equalTo("id", this.mId).findFirst()

        val customerName = view.findViewById<EditText>(R.id.customer_name)
        customerName.setText(customerObject!!.name)

        val customerHowToRead = view.findViewById<EditText>(R.id.customer_how_to_read)
        customerHowToRead.setText(customerObject.howToRead)

        val customerAddressNumber = view.findViewById<EditText>(R.id.customer_address_number)
        customerAddressNumber.setText(customerObject.addressNumber)

        val customerTopAddress = view.findViewById<EditText>(R.id.customer_top_address)
        customerTopAddress.setText(customerObject.topAddress)

        val searchButton = view.findViewById<Button>(R.id.search_address)
        searchButton.setOnClickListener {

             if (customerAddressNumber.text.toString().length == 7) {
                // 『0000000』の形式の郵便番号を『000-0000』に変換
                val sb = StringBuilder()
                sb.append(customerAddressNumber.text.toString())
                sb.insert(3, "-")

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
        customerBottomAddress.setText(customerObject.bottomAddress)

        val customerPhoneNumber = view.findViewById<EditText>(R.id.customer_phone_number)
        customerPhoneNumber.setText(customerObject.phoneNumber)

        val createButton = view.findViewById<Button>(R.id.customer_submit)
        createButton.setOnClickListener {
            onButtonClick(
                customerObject
                ,customerName.text.toString()
                , customerHowToRead.text.toString()
                , customerAddressNumber.text.toString()
                , customerTopAddress.text.toString()
                , customerBottomAddress.text.toString()
                , customerPhoneNumber.text.toString()
            )
            replaceFragment(ShowCustomerFragment.newInstance(mId))
        }

        setTitle("客先編集")
        return view
    }

    private fun onButtonClick(
        co: CustomerObject
        ,customerName: String
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
        else if(!customerHowToRead.matches("^[\\u30A0-\\u30FF]+$".toRegex())) {
            AlertDialog.Builder(this.activity)
                .setTitle("入力エラー")
                .setMessage("フリガナは全角カタカナで入力して下さい")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()
        } else {

            realm.beginTransaction()
            co.name = customerName
            co.howToRead = customerHowToRead
            co.addressNumber = customerAddressNumber
            co.topAddress = customerTopAddress
            co.bottomAddress = customerBottomAddress
            co.phoneNumber = customerPhoneNumber
            realm.commitTransaction()

        }
    }

   private fun searchAddressFromZipCode(zipCode: String): Address? {
        if (zipCode.length != 8) {
            return null
        }

        val geocoder = Geocoder(this.activity, Locale.JAPAN)
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

        return null
    }


}