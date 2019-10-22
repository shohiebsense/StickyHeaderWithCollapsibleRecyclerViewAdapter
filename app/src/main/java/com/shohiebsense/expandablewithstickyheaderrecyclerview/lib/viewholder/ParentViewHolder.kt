package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder

import android.view.View
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.Parent
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.adapter.ExpandableAdapter

open class ParentViewHolder<PR : Parent<CH>, CH>
constructor(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    companion object {
        val INITIAL_POSITION = 180F
        val ROTATED_POSITION = 0F
        val PIVOT_VALUE = 0.5F
        val DEFAULT_ROTATE_DURATION_MS: Long = 500
    }

    var listener: ExpandableAdapter.ParentViewHolderListener? = null
    var isExpanded = false
    var mExpandableAdapter: ExpandableAdapter<*, *, *, *>? = null
    lateinit var parent: PR


    override fun onClick(view: View?) {
        if (view == null) return
        if (isExpanded) {
            collapseView()
        } else {
            expandView()
        }
    }

    fun getParentAdapterPosition(): Int {
        var flatPosition = adapterPosition
        if (flatPosition == RecyclerView.NO_POSITION) {
            return flatPosition
        }
        return mExpandableAdapter!!.getNearestParentPosition(flatPosition)
    }

    @UiThread
    fun setMainItemClickToExpand() {
        itemView.setOnClickListener(this)
    }


    @UiThread
    fun expandView() {
        isExpanded = true
        onExpansionToggled()
        if (listener != null) {
            listener!!.onExpanded(adapterPosition)
        }
    }

    @UiThread
    fun collapseView() {
        isExpanded = false
        onExpansionToggled()
        if (listener != null) {
            listener!!.onCollapsed(adapterPosition)
        }
    }

    open fun onExpansionToggled() {

    }

    @UiThread
    fun shouldItemViewClickToggleExpansion(): Boolean {
        return true
    }


}