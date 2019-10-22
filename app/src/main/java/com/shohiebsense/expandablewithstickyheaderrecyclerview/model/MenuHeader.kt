package com.shohiebsense.expandablewithstickyheaderrecyclerview.model

import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.Parent

data class MenuHeader(val name : String, val menuList : ArrayList<Menu> ) : Parent<Menu> {


    override fun gethildList(): ArrayList<Menu> {
        return menuList
    }

    override fun isExpandedFromTheStart() : Boolean {
        return false
    }

}