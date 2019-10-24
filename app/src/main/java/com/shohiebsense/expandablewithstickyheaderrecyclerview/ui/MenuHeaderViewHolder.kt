package com.shohiebsense.expandablewithstickyheaderrecyclerview.ui

import android.view.View
import android.view.animation.RotateAnimation
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.ExpandableWrapper
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder.ParentViewHolder
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.Menu
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.MenuHeader
import kotlinx.android.synthetic.main.item_menu_header.view.*

class MenuHeaderViewHolder(view : View) : ParentViewHolder<MenuHeader, Menu>(view) {





    override fun bind(expandableWrapperParent: ExpandableWrapper<MenuHeader, Menu>) {
        super.bind(expandableWrapperParent)
        isExpanded = parentItem.isExpanded
        itemView.text_name.text =parentItem.parent?.name
        animateArrowExpandCollapse(itemView.image_arrow_down, !isExpanded)
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        isExpanded = parentItem.isExpanded
    }

    override fun onExpansionToggled() {
        animateArrowExpandCollapse(itemView.image_arrow_down, isExpanded)
    }

}