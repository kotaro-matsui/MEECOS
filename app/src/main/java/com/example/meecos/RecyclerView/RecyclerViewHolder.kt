package com.example.meecos.RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.R

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    val itemRecordIdView: TextView = view.findViewById(R.id.recordId1)
    val itemStartDateView: TextView = view.findViewById(R.id.planStartDate1)
    val itemStartTimeView: TextView = view.findViewById(R.id.planStartTime1)
    val itemEndDateView: TextView = view.findViewById(R.id.planEndDate1)
    val itemEndTimeView: TextView = view.findViewById(R.id.planEndTime1)
    val itemContentsView: TextView = view.findViewById(R.id.planDetail1)

}