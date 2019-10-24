package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib

open class ExpandableWrapper<PR : Parent<CH>, CH> {


     var parent: PR? = null
        set(parent) {
            field = parent
            wrappedChildList = generateChildItemList(parent!!)
        }

    var child: CH? = null
    var isWrappedParent = false
    var isExpanded: Boolean = false

    constructor(parent: PR) : super() {
        this.parent = parent
        isWrappedParent = true
        isExpanded = parent.isExpandedFromTheStart()
        wrappedChildList = generateChildItemList(parent)
    }

    constructor(child: CH) : super() {
        this.child = child
        isWrappedParent = false
        isExpanded = false
    }


    var wrappedChildList: ArrayList<ExpandableWrapper<PR, CH>>? = null
        get() {
            kotlin.check(isWrappedParent) {
                "Parent Not Wrapped"
            }
            return field
        }


    fun generateChildItemList(parentItem: PR): ArrayList<ExpandableWrapper<PR, CH>> {
        val childItemList = arrayListOf<ExpandableWrapper<PR, CH>>()
        parentItem.gethildList().forEach {
            childItemList.add(
                ExpandableWrapper(
                    it
                )
            )
        }
        return childItemList
    }


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as ExpandableWrapper<*, *>?

        if (if (parent != null) !parent!!.equals(that!!.parent) else that!!.parent != null)
            return false
        return if (child != null) child == that.child else that.child == null

    }

    override fun hashCode(): Int {
        var result = if (parent != null) parent.hashCode() else 0
        result = 31 * result + if (child != null) child.hashCode() else 0
        return result
    }


}