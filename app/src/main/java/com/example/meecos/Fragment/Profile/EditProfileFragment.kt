package com.example.meecos.Fragment.Profile

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Config.closeSoftKeyboard
import com.example.meecos.Dialog.ErrorDialogFragment
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Model.ProfileObject
import com.example.meecos.R
import io.realm.Realm
import kotlinx.coroutines.runBlocking
import java.util.*

class EditProfileFragment : BaseFragment(), ErrorDialogFragment.ErrorDialogListener {

    lateinit var realm: Realm

    private val po = ProfileObject()

    private var mView: View? = null

    var mName: EditText? = null
    var mAddressNumber: EditText? = null
    var mTopAddress: EditText? = null
    var mBottomAddress: EditText? = null
    var mPhoneNumber: EditText? = null

    private var mSearchButton: Button? = null
    private var mEditButton: Button? = null
    private var eDialog: ErrorDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        this.mView = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        this.mView!!.setBackEvent(onBackListener)

        val profileObject = po.findProfileById(1)

        this.mName = this.mView!!.findViewById(R.id.user_name)
        this.mName!!.setText(profileObject!!.name)

        this.mAddressNumber = this.mView!!.findViewById(R.id.user_address_number)
        this.mAddressNumber!!.setText(profileObject.addressNumber)

        this.mTopAddress = this.mView!!.findViewById(R.id.user_top_address)
        this.mTopAddress!!.setText(profileObject.topAddress)

        this.mBottomAddress = this.mView!!.findViewById(R.id.user_bottom_address)
        this.mBottomAddress!!.setText(profileObject.bottomAddress)

        this.mPhoneNumber = this.mView!!.findViewById(R.id.user_phone_number)
        this.mPhoneNumber!!.setText(profileObject.phoneNumber)

        this.mSearchButton = this.mView!!.findViewById(R.id.search_address)
        this.mSearchButton!!.setOnClickListener {
            searchButtonClickListener(this.mAddressNumber!!.text.toString())
        }

        this.mEditButton = this.mView!!.findViewById(R.id.user_edit)
        this.mEditButton!!.setOnClickListener {
            editButtonClickListener(
                this.mName!!.text.toString()
                , this.mAddressNumber!!.text.toString()
                , this.mTopAddress!!.text.toString()
                , this.mBottomAddress!!.text.toString()
                , this.mPhoneNumber!!.text.toString()
            )
        }

        setTitle("プロフィール編集")
        return this.mView
    }

    //ツールバー右側に戻るボタンを追加する処理
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.back_item, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    //アイコン押した時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.back_page) {
            closeSoftKeyboard(this.mView!!, requireActivity())
            replaceFragment(ProfileFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchButtonClickListener(addressNumber: String) {
        closeSoftKeyboard(this.mView!!, requireActivity())
        // 郵便番号桁数バリデーションチェック
        if (addressNumber.length != 7) {
            this.eDialog = ErrorDialogFragment.newInstance(
                "入力エラー",
                "7桁の郵便番号を\n入力して下さい",
                this
            )
            this.eDialog!!.show(parentFragmentManager, "dialog")
        } else {
            // 『0000000』の形式の郵便番号を『000-0000』に変換
            val sb = StringBuilder()
            sb.append(addressNumber)
            sb.insert(3, "-")

            val address = runBlocking {
                searchAddressFromZipCode(sb.toString())
            }

            // 郵便番号存在バリデーションチェック
            if (address == null) {
                this.eDialog = ErrorDialogFragment.newInstance(
                    "検索エラー",
                    "住所が取得できません",
                    this
                )
                this.eDialog!!.show(parentFragmentManager, "dialog")
            } else {
                val topAddress =
                    address.adminArea + address.locality + address.featureName
                this.mTopAddress!!.setText(topAddress, TextView.BufferType.NORMAL)
            }
        }
    }

    private fun editButtonClickListener(
        name: String
        , addressNumber: String
        , topAddress: String
        , bottomAddress: String
        , phoneNumber: String
    ) {
        closeSoftKeyboard(this.mView!!, requireActivity())

        realm = Realm.getDefaultInstance()
        val customerObject = po.findProfileById(1)

        realm.beginTransaction()

        customerObject!!.name = name
        customerObject.addressNumber = addressNumber
        customerObject.topAddress = topAddress
        customerObject.bottomAddress = bottomAddress
        customerObject.phoneNumber = phoneNumber
        Toast.makeText(activity as MainActivity, "変更を反映しました。", Toast.LENGTH_SHORT)
            .show()

        realm.commitTransaction()
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

    private val onBackListener = object : BackEventListener {
        override fun onBackClick() {
            replaceFragment(ProfileFragment())
        }
    }

    override fun onErrorOkClick() {
        super.onErrorOkClick()
        this.eDialog!!.dismiss()
    }

}