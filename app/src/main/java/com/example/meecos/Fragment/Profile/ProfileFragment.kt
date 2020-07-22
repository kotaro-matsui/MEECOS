package com.example.meecos.Fragment.Profile

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Fragment.home.HomeFragment
import com.example.meecos.Model.CustomerObject
import com.example.meecos.Model.ProfileObject
import io.realm.Realm

class ProfileFragment : BaseFragment() {
    lateinit var realm: Realm

    private val po = ProfileObject()

    var mName: TextView? = null
    var mAddressNumber: TextView? = null
    var mAddress: TextView? = null
    var mPhoneNumber: TextView? = null

    private var mEditButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.setBackEvent(onBackListener)

        realm = Realm.getDefaultInstance()

        // トランザクションして登録
        try {
            realm.executeTransaction { realm ->
                val obj = realm.createObject(ProfileObject::class.java, 1 )
                obj.name = "株式会社ブレーンナレッジシステムズ"
            }
        } catch (e: Exception) {
            println("exceptionエラー:" + e.message)
        } catch (r: RuntimeException) {
            println("runtime exceptionエラー:" + r.message)
        }

        val profileObject = po.findProfileById(1)

        this.mName = view.findViewById(R.id.user_name)
        this.mName!!.text = profileObject!!.name

        this.mAddressNumber = view.findViewById(R.id.user_address_number)
        this.mAddressNumber!!.text = profileObject.addressNumber

        this.mAddress = view.findViewById(R.id.user_address)
        this.mAddress!!.text =
            (profileObject.topAddress + profileObject.bottomAddress)

        this.mPhoneNumber = view.findViewById(R.id.user_phone_number)
        this.mPhoneNumber!!.text = profileObject.phoneNumber

        this.mEditButton = view.findViewById(R.id.user_edit)
        this.mEditButton!!.setOnClickListener(editButtonClickListener)

        setTitle("プロフィール")
        return view
    }

    private val editButtonClickListener = View.OnClickListener {
        replaceFragment(EditProfileFragment())
    }

    // 戻るボタンを押したときの処理
    private val onBackListener = object : BackEventListener {
        override fun onBackClick() {
                replaceFragment(HomeFragment.newInstance(false))
        }
    }
}