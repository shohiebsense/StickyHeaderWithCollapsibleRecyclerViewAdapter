package com.shohiebsense.expandablewithstickyheaderrecyclerview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.shohiebsense.expandablewithstickyheaderrecyclerview.R
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.adapter.ExpandableAdapter
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.Menu
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.MenuHeader

class MenuAdapter(val menuHeaderList : ArrayList<MenuHeader>) : ExpandableAdapter<MenuHeader, Menu, MenuHeaderViewHolder, MenuViewHolder>(menuHeaderList) {


    override fun onCreateParentViewHolder(
        parentViewGroup: ViewGroup,
        viewType: Int
    ): MenuHeaderViewHolder {
        return MenuHeaderViewHolder(LayoutInflater.from(parentViewGroup.context).inflate(R.layout.item_menu_header, parentViewGroup, false))
    }

    override fun onCreateChildViewHolder(childViewGroup: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(LayoutInflater.from(childViewGroup.context).inflate(R.layout.item_menu, childViewGroup, false))
    }

    override fun onBindParentViewHolder(
        parentViewHolder: MenuHeaderViewHolder,
        parentPosition: Int,
        parent: MenuHeader
    ) {

    }

    override fun onBindChildViewHolder(
        childViewHolder: MenuViewHolder,
        parentPosition: Int,
        childPosition: Int,
        child: Menu
    ) {

    }


}