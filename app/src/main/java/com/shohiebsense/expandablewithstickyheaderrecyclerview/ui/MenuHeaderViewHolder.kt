package com.shohiebsense.expandablewithstickyheaderrecyclerview.ui

import android.view.View
import android.view.animation.RotateAnimation
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder.ParentViewHolder
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.Menu
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.MenuHeader
import kotlinx.android.synthetic.main.item_menu_header.view.*

class MenuHeaderViewHolder(view : View) : ParentViewHolder<MenuHeader, Menu>(view) {


    override fun onExpansionToggled() {
        super.onExpansionToggled()
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
        itemView.image_arrow_down.startAnimation(rotateAnimation)
    }

}