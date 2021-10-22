package com.jeff.startproject.ui.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.lang.ref.WeakReference

class ItemDragDropCallback : ItemTouchHelper.Callback() {

    private val onRowMoved = MutableSharedFlow<Pair<Int, Int>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    fun onRowMoved(): SharedFlow<Pair<Int, Int>> = onRowMoved

    private val onRowSelected = MutableSharedFlow<WeakReference<RecyclerView.ViewHolder>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onRowSelected(): SharedFlow<WeakReference<RecyclerView.ViewHolder>> = onRowSelected

    private val onRowClear = MutableSharedFlow<WeakReference<RecyclerView.ViewHolder>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onRowClear(): SharedFlow<WeakReference<RecyclerView.ViewHolder>> = onRowClear

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return onRowMoved.tryEmit(Pair(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition))
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.also {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                onRowSelected.tryEmit(WeakReference(it))
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        onRowClear.tryEmit(WeakReference(viewHolder))
    }
}