package com.example.meecos.Fragment.Schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R

class NewPlanFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_newplan, container,false)
        return view
    }
}