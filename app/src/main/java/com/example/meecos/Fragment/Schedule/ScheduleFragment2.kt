package com.example.meecos.Fragment.Schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.RecyclerView.RecyclerAdapter
import com.example.meecos.RecyclerView.RecyclerViewHolder

class ScheduleFragment2 : BaseFragment(), RecyclerViewHolder.ItemClickListener {

    val list = listOf("a", "b","a", "b","a", "b","a", "b","a", "b","a", "b","a", "b","a", "b","a", "b")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_schedule2, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.adapter = RecyclerAdapter(
            (activity as MainActivity),
            this,
            list)
        recyclerView.layoutManager = LinearLayoutManager(
            (activity as MainActivity),
            LinearLayoutManager.VERTICAL, false)

        setTitle("スケジュール")
        return view
    }

    override fun onItemClick(view: View, position: Int) {

    }
}