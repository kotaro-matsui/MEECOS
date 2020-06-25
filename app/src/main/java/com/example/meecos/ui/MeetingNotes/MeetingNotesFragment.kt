package com.example.meecos.ui.MeetingNotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meecos.R
import com.example.meecos.ui.Base.BaseFragment

class MeetingNotesFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_meeting_notes, container, false)
        setTitle("議事録リスト")
        return view
    }
}