package com.shohiebsense.expandablewithstickyheaderrecyclerview.model.generator

import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.Menu
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.MenuHeader

class MenuService {

    companion object {
        fun generateMenu(size: Int): ArrayList<Menu>{
            val menuList = arrayListOf<Menu>()
            for(i in 0 .. size){
                menuList.add(Menu("menu pos $i"))
            }
            return menuList
        }

        fun generate() : ArrayList<MenuHeader> {
            val menuHeaderList = arrayListOf<MenuHeader>()
            val menuHeader1 = MenuHeader("Header1", generateMenu(12))
            menuHeaderList.add(menuHeader1)
            val menuHeaDer2 = MenuHeader("Header2", generateMenu(15))
            menuHeaderList.add(menuHeaDer2)
            return menuHeaderList
        }
    }


}