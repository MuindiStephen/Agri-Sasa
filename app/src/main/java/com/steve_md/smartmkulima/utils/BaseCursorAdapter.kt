package com.steve_md.smartmkulima.utils

import android.database.Cursor
import androidx.recyclerview.widget.RecyclerView

abstract class BaseCursorAdapter<V : RecyclerView.ViewHolder?>(c: Cursor?) :
    RecyclerView.Adapter<V>() {
    private var mCursor: Cursor? = null
    private var mDataValid = false
    private var mRowIDColumn = 0
    abstract fun onBindViewHolder(holder: V, cursor: Cursor?)
    override fun onBindViewHolder(holder: V & Any, position: Int) {
        check(mDataValid) { "Cannot bind view holder when cursor is in invalid state." }
        check(mCursor!!.moveToPosition(position)) { "Could not move cursor to position $position when trying to bind view holder" }
        onBindViewHolder(holder, mCursor)
    }

    override fun getItemCount(): Int {
        return if (mDataValid) {
            mCursor!!.count
        } else {
            0
        }
    }

    override fun getItemId(position: Int): Long {
        check(mDataValid) { "Cannot lookup item id when cursor is in invalid state." }
        check(mCursor!!.moveToPosition(position)) { "Could not move cursor to position $position when trying to get an item id" }
        return mCursor!!.getLong(mRowIDColumn)
    }

    fun getItem(position: Int): Cursor? {
        check(mDataValid) { "Cannot lookup item id when cursor is in invalid state." }
        check(mCursor!!.moveToPosition(position)) { "Could not move cursor to position $position when trying to get an item id" }
        return mCursor
    }

    open fun swapCursor(newCursor: Cursor?) {
        if (newCursor === mCursor) {
            return
        }
        if (newCursor != null) {
            mCursor = newCursor
            mDataValid = true
            // notify the observers about the new cursor
            notifyDataSetChanged()
        } else {
            notifyItemRangeRemoved(0, itemCount)
            mCursor = null
            mRowIDColumn = -1
            mDataValid = false
        }
    }

    init {
        setHasStableIds(true)
        swapCursor(c)
    }
}