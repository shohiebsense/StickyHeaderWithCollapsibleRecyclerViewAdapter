package com.shohiebsense.expandablewithstickyheaderrecyclerview.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shohiebsense.expandablewithstickyheaderrecyclerview.R
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.adapter.ExpandableAdapter
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.viewholder.ParentViewHolder
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.Menu
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.MenuHeader
import kotlinx.android.synthetic.main.item_menu_header.view.*

class MenuAdapter(val recyclerView : RecyclerView, val menuHeaderList : ArrayList<MenuHeader>) : ExpandableAdapter<MenuHeader, Menu, MenuHeaderViewHolder, MenuViewHolder>(menuHeaderList) {


    init {
        recyclerView.addOnItemTouchListener(object  : AdapterView.OnItemClickListener,
            RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if(e.action == MotionEvent.ACTION_UP){
                    var layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()
                    if(position == 0) return false
                    val completelyPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        var clickedPosition = rv.getChildAdapterPosition(child)
                        if(clickedPosition == position){
                            var parentPosition = getNearestParentPosition(clickedPosition)
                            var parentView = rv.findViewHolderForAdapterPosition(parentPosition)
                            var iswrappedParent = flatItemList[parentPosition].isWrappedParent
                            var isExpanded = flatItemList[parentPosition].isExpanded

                            if(parentView != null){
                                ParentViewHolder.animateArrowExpandCollapse(parentView.itemView.image_arrow_down, isExpanded)
                            }

                            if(flatItemList[parentPosition].isExpanded){
                                parentCollapsedFromViewHolder(parentPosition)
                            }
                            else{
                                parentExpandedFromViewHolder(parentPosition)
                            }
                            notifyItemChanged(parentPosition)
                            return true
                        }
                        //todo workaround when is 3/4 view ....
                    }
                    else{
                    }
                }
                return false

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        })
    }

    override fun bindStickyHeader(headerView: View, headerPosition: Int) {
        var flatItem = flatItemList[headerPosition]
        if(flatItem.isWrappedParent){
            var parent = flatItem.parent
            headerView.text_name.setText(parent!!.name)
            ParentViewHolder.animateArrowExpandCollapse(headerView.image_arrow_down, flatItem.isExpanded)
        }
    }

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
        parentViewHolder.bind(flatItemList[parentPosition])
    }

    override fun onBindChildViewHolder(
        childViewHolder: MenuViewHolder,
        parentPosition: Int,
        childPosition: Int,
        child: Menu
    ) {
        childViewHolder.bind(child)
    }


}