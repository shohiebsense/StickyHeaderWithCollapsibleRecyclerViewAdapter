package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder

import android.view.View
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.ExpandableWrapper
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.Parent
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.adapter.ExpandableAdapter

open class ParentViewHolder<PR : Parent<CH>, CH>
constructor(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    companion object {
        val INITIAL_POSITION = 180F
        val ROTATED_POSITION = 0F
        val PIVOT_VALUE = 0.5F
        val DEFAULT_ROTATE_DURATION_MS: Long = 500

        fun animateArrowExpandCollapse(image_arrow_down : ImageView, isExpanded : Boolean){
            val rotateAnimation: RotateAnimation
            if (isExpanded) { // rotate clockwise
                rotateAnimation = RotateAnimation(
                    ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE,
                    RotateAnimation.RELATIVE_TO_SELF, PIVOT_VALUE
                )
            } else { // rotate counterclockwise
                rotateAnimation = RotateAnimation(
                    INITIAL_POSITION*-1,
                    ROTATED_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF,  PIVOT_VALUE,
                    RotateAnimation.RELATIVE_TO_SELF,  PIVOT_VALUE
                )
            }
            rotateAnimation.duration = 200
            rotateAnimation.fillAfter = true
            image_arrow_down.startAnimation(rotateAnimation)
        }
    }

    var listener: ExpandableAdapter.ParentViewHolderListener? = null
    var isExpanded = false
    var mExpandableAdapter: ExpandableAdapter<*, *, *, *>? = null
    lateinit var parentItem: ExpandableWrapper<PR, CH>

    open fun bind(parentItem : ExpandableWrapper<PR, CH>){

    }


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
        onExpansionToggled()
        listener!!.onExpanded(adapterPosition)
    }

    @UiThread
    fun collapseView() {
        onExpansionToggled()
        listener!!.onCollapsed(adapterPosition)
    }

    open fun onExpansionToggled() {

    }

    @UiThread
    fun shouldItemViewClickToggleExpansion(): Boolean {
        return true
    }


}