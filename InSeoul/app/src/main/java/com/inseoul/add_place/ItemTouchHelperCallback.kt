package com.inseoul.add_place

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

public class ItemTouchHelperCallback(
    adapter: ItemTouchHelperAdapter
) : ItemTouchHelper.Callback() {

    val mAdapter = adapter

    interface ItemTouchHelperAdapter {
        fun onItemMove(fromPosition: Int, toPosition: Int):Boolean
        fun onItemDismiss(position:Int)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        var dragFlags = ItemTouchHelper.UP; ItemTouchHelper.DOWN
        var swipeFlags = ItemTouchHelper.START; ItemTouchHelper.END

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    //ItemTouchHelper drag 없이 swipe 용도로만 사용가능하다(반대도 가능). RecyclerView item 에서 long press를 통해 drag 시점을 알기 위해서는 isLongPressDragEnabled()에서 true를 return 해야만 한다.
    //대신에 ItemTouchHelper.startDrag(RecyclerView.ViewHolder)를 사용할 수도 있지만 추후에 다루도록 한다.
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

}
