package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib

interface Parent<T> {
    fun gethildList() : ArrayList<T>
    fun isExpandedFromTheStart() : Boolean
}