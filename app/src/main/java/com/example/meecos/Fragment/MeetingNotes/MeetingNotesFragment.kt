
package com.example.meecos.Fragment.MeetingNotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment

class MeetingNotesFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_meeting_notes, container, false)
        setTitle("議事録リスト")

        val recordBtn = view.findViewById<Button>(R.id.recordBtn)
        recordBtn.setOnClickListener(onClickRecordBtn)

        return view
    }

    private val onClickRecordBtn = View.OnClickListener {
        (activity as MainActivity).replaceFragment(RecordFragment())
    }

}