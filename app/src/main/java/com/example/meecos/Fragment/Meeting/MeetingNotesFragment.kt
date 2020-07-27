
package com.example.meecos.Fragment.Meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.Fragment.Customer.CustomerFragment
import com.example.meecos.Fragment.MeetingNotes.RecordFragment
import com.example.meecos.Fragment.home.HomeFragment

class MeetingNotesFragment : BaseFragment() {

    //　戻るボタンを制御するための真偽地
    // trueなら戻る、falseなら戻らない
    var backSwitch: Boolean = true

    companion object {
        fun newInstance(bs: Boolean): MeetingNotesFragment {
            val fragment = MeetingNotesFragment()
            fragment.backSwitch = bs
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meeting_notes, container, false)
        view.setBackEvent(onBackListener)
        setTitle("議事録リスト")

        val recordBtn = view.findViewById<Button>(R.id.recordBtn)
        recordBtn.setOnClickListener(onClickRecordBtn)

        return view
    }

    private val onClickRecordBtn = View.OnClickListener {
        (activity as MainActivity).replaceFragment(RecordFragment())
    }

    // 現状、前画面での戻るボタンのイベントを、遷移先であるここで拾ってしまうため、登録画面→HOME画面のような遷移が起こる
    //　実機でこの問題が起こらない場合は『replaceFragment(HomeFragment())』の1文でよい
    private val onBackListener = object : BackEventListener {
        override fun onBackClick() {
            if (backSwitch) {
                replaceFragment(HomeFragment.newInstance(false))
            } else {
                backSwitch = true
            }
        }
    }

}