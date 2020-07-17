package com.example.meecos.RecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import org.w3c.dom.Text

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    var item: ScheduleObject? = null
    val itemRecordIdView: TextView = view.findViewById(R.id.recordId)
    val itemTitleView:TextView = view.findViewById(R.id.mTitle)
    val itemStartDateView: TextView = view.findViewById(R.id.planStartDate)
    val itemStartTimeView: TextView = view.findViewById(R.id.planStartTime)
    val itemEndDateView: TextView = view.findViewById(R.id.planEndDate)
    val itemEndTimeView: TextView = view.findViewById(R.id.planEndTime)
    val itemContentsView: TextView = view.findViewById(R.id.planDetail)

}