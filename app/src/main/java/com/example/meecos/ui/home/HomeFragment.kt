package com.example.meecos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.meecos.MainActivity
import com.example.meecos.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)

        var b = view.findViewById<Button>(R.id.button)
        b.setOnClickListener {
            var ac = activity as MainActivity
            ac.replaceFragment(TestFragment())
        }
        return view
    }
}