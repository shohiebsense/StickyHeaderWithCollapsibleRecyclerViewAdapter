package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.shohiebsense.expandablewithstickyheaderrecyclerview.R
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.Parent
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder.ChildViewHolder
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder.ParentViewHolder
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.ExpandableWrapper
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.StickyHeaderItemDecoration
import java.lang.IllegalStateException
import java.util.ArrayList

abstract class ExpandableAdapter<PR : Parent<CH>, CH, PVH : ParentViewHolder<PR, CH>, CVH : ChildViewHolder<CH>> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>,
    StickyHeaderItemDecoration.StickyHeaderListener {

    companion object {
        const val EXPANDED_STATE_MAP = "ExpandableAdapter.ExpandedStateMap"
        const val TYPE_PARENT = 0
        const val TYPE_CHILD = 1
        const val TYPE_FIRST_USER = 2
        const val INVALID_FLAT_POSITION = -1
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var itemPosition = itemPosition
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }
            itemPosition -= 1
        } while (itemPosition >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.item_menu_header
    }

    override fun bindHeaderData(headerView: View, headerPosition: Int) {
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return flatItemList[itemPosition].isWrappedParent
    }

    lateinit var flatItemList: ArrayList<ExpandableWrapper<PR, CH>>
    lateinit var parentList: ArrayList<PR>
    var listener: ParentViewHolderListener = object : ParentViewHolderListener {
        override fun onExpanded(flatParentPosition: Int) {
            parentExpandedFromViewHolder(flatParentPosition)
        }

        override fun onCollapsed(flatPrentPosition: Int) {
            parentCollapsedFromViewHolder(flatPrentPosition)
        }
    }

    var attachedRecyclerViewPool = arrayListOf<RecyclerView>()
    lateinit var expansionStateMap: HashMap<PR, Boolean>

    constructor(parentList: ArrayList<PR>) : super() {
        this.parentList = parentList
        flatItemList = generateFlattenedParentChildList(parentList)
        expansionStateMap = hashMapOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (isParentViewType(viewType)) {
            val pvh = onCreateParentViewHolder(parent, viewType)
            pvh.listener = listener
            pvh.mExpandableAdapter = this
            return pvh
        } else {
            val cvh = onCreateChildViewHolder(parent, viewType)
            cvh.expandableAdapter = this
            return cvh
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, flatPosition: Int) {
        if (flatPosition > flatItemList.size) {
            throw IllegalStateException("Binding items out of bounds")
        }

        var item = flatItemList[flatPosition]
        if (item.isWrappedParent) {
            var parentHolder = holder as PVH
            if (parentHolder.shouldItemViewClickToggleExpansion()) {
                parentHolder.setMainItemClickToExpand()
            }

            parentHolder.isExpanded = item.isExpanded
            parentHolder.parent = item.parent!!
            onBindParentViewHolder(
                parentHolder,
                getNearestParentPosition(flatPosition),
                item.parent!!
            )
        } else {
            var childHolder = holder as CVH
            childHolder.child = item.child
            onBindChildViewHolder(
                childHolder,
                getNearestParentPosition(flatPosition),
                getChildPosition(flatPosition),
                item.child!!
            )
        }
    }

    fun notifyChildMoved(parentPosition: Int, fromChildPosition: Int, toChildPosition: Int) {
        var parent = parentList[parentPosition]
        var flatParentPosition = getFlatParentPosition(parentPosition)

        var parentWrapper = flatItemList[flatParentPosition]
        parentWrapper.parent = parent
        if (parentWrapper.isExpanded) {
            var fromChild = flatItemList.removeAt(flatParentPosition + 1 + fromChildPosition)
            flatItemList.add(flatParentPosition + 1 + toChildPosition, fromChild)
            notifyItemMoved(
                flatParentPosition + 1 + fromChildPosition,
                flatParentPosition + 1 + toChildPosition
            )
        }
    }

    fun generateFlattenedParentChildList(parentList: ArrayList<PR>): ArrayList<ExpandableWrapper<PR, CH>> {
        val flatItemList = arrayListOf<ExpandableWrapper<PR, CH>>()
        parentList.forEach {
            generateParentWrapper(flatItemList, it, it.isExpandedFromTheStart())
        }
        return flatItemList
    }

    fun generateFlattenedParentChildList(
        parentList: ArrayList<PR>,
        savedLastExpansionState: Map<PR, Boolean>
    ): ArrayList<ExpandableWrapper<PR, CH>> {
        val flatItemList = arrayListOf<ExpandableWrapper<PR, CH>>()
        parentList.forEach {
            val lastExpandedState = savedLastExpansionState.get(it)
            val shouldExpand =
                if (lastExpandedState == null) it.isExpandedFromTheStart() else lastExpandedState
            generateParentWrapper(flatItemList, it, shouldExpand)
        }
        return flatItemList
    }

    fun generateParentWrapper(
        flatItemList: ArrayList<ExpandableWrapper<PR, CH>>,
        parent: PR,
        shouldExpand: Boolean
    ) {
        var parentWrapper =
            ExpandableWrapper(parent)
        flatItemList.add(parentWrapper)
        if (shouldExpand) {
            generateExpandChildren(flatItemList, parentWrapper)
        }
    }

    fun generateExpandChildren(
        flatItemList: ArrayList<ExpandableWrapper<PR, CH>>,
        parentWrapper: ExpandableWrapper<PR, CH>
    ) {
        parentWrapper.isExpanded = true
        var wrappedChildList = parentWrapper.wrappedChildList
        wrappedChildList?.forEach {
            flatItemList.add(it)
        }
    }

    fun generateExpandedStateMap(): HashMap<Int, Boolean> {
        var parentHashMap = hashMapOf<Int, Boolean>()
        var childCount = 0
        for (i in 0..flatItemList.size) {
            var it = flatItemList[i]
            if (it.isWrappedParent) {
                parentHashMap.put(i - childCount, it.isExpanded)
            } else {
                childCount++
            }
        }
        return parentHashMap
    }

    abstract fun onCreateParentViewHolder(parentViewGroup: ViewGroup, viewType: Int): PVH
    abstract fun onCreateChildViewHolder(childViewGroup: ViewGroup, viewType: Int): CVH
    abstract fun onBindParentViewHolder(parentViewHolder: PVH, parentPosition: Int, parent: PR)
    abstract fun onBindChildViewHolder(
        childViewHolder: CVH,
        parentPosition: Int,
        childPosition: Int,
        child: CH
    )


    fun getNearestParentPosition(flatPosition: Int): Int {
        if (flatPosition == 0) {
            return 0
        }
        var parentCount = -1
        flatItemList.forEach {
            if (it.isWrappedParent) {
                parentCount++
            }
        }
        return parentCount
    }

    fun getFlatParentPosition(parentPosition: Int): Int {
        var parentCount = 0
        var listItemCount = flatItemList.size

        for (i in 0..flatItemList.size) {
            var it = flatItemList[i]
            if (it.isWrappedParent) {
                parentCount++

                if (parentCount > parentPosition) {
                    return i
                }
            }
        }
        return INVALID_FLAT_POSITION
    }

    fun getChildPosition(flatPosition: Int): Int {
        if (flatPosition == 0)
            return 0

        var childCount = 0
        for (i in 0..flatPosition) {
            var item = flatItemList[i]
            if (item.isWrappedParent) {
                childCount = 0
            } else {
                childCount++
            }
        }
        return childCount
    }

    override fun getItemViewType(position: Int): Int {
        var item = flatItemList[position]
        if (item.isWrappedParent) {
            return getParentViewType(getNearestParentPosition(position))
        }
        return getChildViewType(getNearestParentPosition(position), getChildPosition(position))
    }

    override fun getItemCount(): Int {
        return flatItemList.size
    }

    fun setParentList(parentList: ArrayList<PR>, isExpansionStatePreserved: Boolean) {
        this.parentList = parentList
        notifyParentDataSetCChange(isExpansionStatePreserved)
    }

    fun notifyParentDataSetCChange(isExpansionStatePreserved: Boolean) {
        if (isExpansionStatePreserved) {
            flatItemList = generateFlattenedParentChildList(parentList, expansionStateMap)
        } else {
            flatItemList = generateFlattenedParentChildList(parentList)
        }
        notifyDataSetChanged()
    }


    fun isParentViewType(viewType: Int): Boolean {
        return viewType == TYPE_PARENT
    }


    fun getParentViewType(parentPosition: Int): Int {
        return TYPE_PARENT
    }

    fun getChildViewType(parentPosition: Int, childPosition: Int): Int {
        return TYPE_CHILD
    }

    @UiThread
    protected fun parentExpandedFromViewHolder(flatParentPosition: Int) {
        val parentWrapper = flatItemList[flatParentPosition]
        updateExpandedParent(parentWrapper, flatParentPosition, true)
    }

    fun updateExpandedParent(
        parentWrapper: ExpandableWrapper<PR, CH>,
        flatParentPosition: Int,
        isTriggered: Boolean
    ) {
        if (parentWrapper.isExpanded) {
            return
        }

        parentWrapper.isExpanded = true
        expansionStateMap.put(parentWrapper.parent!!, true)

        var wrappedChildList = parentWrapper.wrappedChildList
        if (wrappedChildList != null) {
            var childCount = wrappedChildList.lastIndex
            for (i in 0..childCount) {
                flatItemList.add(flatParentPosition + i + 1, wrappedChildList[i])
            }
            notifyItemRangeInserted(flatParentPosition + 1, childCount)
        }

        if (isTriggered) {
            //todo event. ...
        }
    }

    /**
     * Called when a ParentViewHolder has triggered a collapse for it's parent
     *
     * @param flatParentPosition the position of the parent that is calling to be collapsed
     */
    @UiThread
    protected fun parentCollapsedFromViewHolder(flatParentPosition: Int) {
        val parentWrapper = flatItemList[flatParentPosition]
        updateCollapsedParent(parentWrapper, flatParentPosition, true)
    }

    fun updateCollapsedParent(
        parentWrapper: ExpandableWrapper<PR, CH>,
        flatPosition: Int,
        isCollapseTriggered: Boolean
    ) {
        if (!parentWrapper.isExpanded) {
            return
        }
        parentWrapper.isExpanded = false
        expansionStateMap[parentWrapper.parent!!] = false
        var wrappedCChildList = parentWrapper.wrappedChildList

        if (wrappedCChildList != null) {
            var childCount = wrappedCChildList.size
            for (i in childCount - 1 downTo 0) {
                flatItemList.removeAt(flatPosition + i + 1)
            }
            notifyItemRangeRemoved(flatPosition + 1, childCount)
        }

        if (isCollapseTriggered) {
            //event
        }
    }


    //as event in activity
    interface ParentViewHolderListener {
        fun onExpanded(flatParentPosition: Int)
        fun onCollapsed(flatPrentPosition: Int)
    }
}
