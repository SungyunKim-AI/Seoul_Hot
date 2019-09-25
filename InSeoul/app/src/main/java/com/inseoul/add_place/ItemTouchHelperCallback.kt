package com.inseoul.add_place

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

public class ItemTouchHelperCallback(
    listener: OnItemMoveListener
) : ItemTouchHelper.Callback() {

    val l = listener

    public interface OnItemMoveListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
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

        l.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

}
