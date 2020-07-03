package com.example.meecos.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meecos.Model.ScheduleObject
import com.example.meecos.R
import io.realm.RealmResults

class RecyclerAdapter(private val context: Context,
                      private val itemClickListener: RecyclerViewHolder.ItemClickListener,
                      private val itemList:RealmResults<ScheduleObject>) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mRecyclerView : RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder?.let {
            //ここもかえる
            it.item = itemList[position]
            it.itemRecordIdView.text = itemList[position]?.id.toString()
            it.itemStartDateView.text = itemList[position]?.startDate
            it.itemStartTimeView.text = itemList[position]?.startTime
            it.itemEndDateView.text = itemList[position]?.endDate
            it.itemEndTimeView.text = itemList[position]?.endTime
            it.itemContentsView.text = itemList[position]?.contents
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.list_item, parent, false)

        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }

        return RecyclerViewHolder(mView)
    }

}