package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.adapter.ExpandableAdapter

open class ChildViewHolder<CH>(val view : View) : RecyclerView.ViewHolder(view) {
    var child : CH? = null
    lateinit var expandableAdapter: ExpandableAdapter<*,*,*,*>


    fun getParentAdapterPosition() : Int {
        var flatPosition = adapterPosition
        if(adapterPosition == null || flatPosition == RecyclerView.NO_POSITION){
            return RecyclerView.NO_POSITION
        }
        return expandableAdapter.getNearestParentPosition(flatPosition)
    }

    fun getChildAdapterPosition() : Int {
        var flatPosition = adapterPosition
        if(expandableAdapter == null || flatPosition == RecyclerView.NO_POSITION){
            return RecyclerView.NO_POSITION
        }
        return expandableAdapter.getChildPosition(flatPosition)
    }


}
