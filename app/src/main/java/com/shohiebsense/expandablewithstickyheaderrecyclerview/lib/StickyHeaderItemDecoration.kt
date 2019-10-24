package com.shohiebsense.expandablewithstickyheaderrecyclerview.lib

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration(val listener: StickyHeaderListener) :
    RecyclerView.ItemDecoration() {

    var stickyHeaderHeight = 0

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val topChild = parent.getChildAt(0) ?: return

        var topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        var headerPosition = listener.getHeaderPositionForItem(topChildPosition)
        val currentHeader = getHeaderViewForItem(headerPosition, parent)
        fixLayoutSize(parent, currentHeader)
        var contactPoint = currentHeader.bottom
        var childInContact = gethildInContact(parent, contactPoint, headerPosition)



        if (childInContact != null) {
            var childAdapterPosition = parent.getChildAdapterPosition(childInContact)
            if(childAdapterPosition > 0){
                if (listener.isHeader(childAdapterPosition)) {
                    moveHeader(c, currentHeader, childInContact)
                    return
                }
            }
        }

        drawHeader(c, currentHeader)
    }


    fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        var layoutResId = listener.getHeaderLayout(headerPosition)
        var header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        listener.bindStickyHeader(header, headerPosition)
        return header
    }

    fun fixLayoutSize(parent: ViewGroup, view: View) {
        var widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        var heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        var childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        var childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        stickyHeaderHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, stickyHeaderHeight)
    }


    fun gethildInContact(parent: RecyclerView, contactPoint: Int, currentHeaderPos: Int): View? {
        var childInContact: View? = null
        for (index in 0..parent.childCount) {


            var heightTolerance = 0
            var child = parent.getChildAt(index)

            if (currentHeaderPos != index) {
                var position = parent.getChildAdapterPosition(child)
                if (position > 0) {
                    var isChildHeader = listener.isHeader(position)
                    if (isChildHeader) {
                        heightTolerance = stickyHeaderHeight - child.height
                    }
                }
            }

            if (child != null) {
                var childBottomPosition =
                    if (child.top > 0) child.bottom + heightTolerance else child.bottom
                if (childBottomPosition > contactPoint) {
                    if (child.top <= contactPoint) {
                        childInContact = child
                        break
                    }
                }
            }
        }
        return childInContact
    }

    fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0F, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0F, 0F)
        header.draw(c)
        c.restore()
    }

    interface StickyHeaderListener {
        fun getHeaderPositionForItem(itemPosition: Int): Int
        fun getHeaderLayout(headerPosition: Int): Int
        fun bindStickyHeader(headerView: View, headerPosition: Int)
        fun isHeader(itemPosition: Int): Boolean
    }
}