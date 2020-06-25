package com.example.meecos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.meecos.R
import com.example.meecos.ui.Base.BaseFragment

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        setTitle("MEECOS")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //リストビューに表示するリストを手動で作成
        val texts = arrayOf("abc ", "bcd", "cde")


        val listView = view.findViewById<ListView>(R.id.today_schedule)

        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        val arrayAdapter = ArrayAdapter(requireActivity().applicationContext,
            android.R.layout.simple_list_item_1, texts)

        listView.adapter = arrayAdapter
    }
}