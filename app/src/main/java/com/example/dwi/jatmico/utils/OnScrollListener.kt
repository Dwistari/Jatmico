package com.example.dwi.jatmico.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


abstract class OnScrollListener(val layoutManager: LinearLayoutManager?) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val totalItemCount = layoutManager?.itemCount
        val lastVisibleItem = layoutManager?.findLastVisibleItemPosition()
        val visibleThreshold = layoutManager?.childCount

        if (!isLoading() && !isDataEnd() && totalItemCount!! <= lastVisibleItem!! + visibleThreshold!!) {
            loadMoreItem()
        }
    }

    abstract fun loadMoreItem()

    abstract fun isDataEnd(): Boolean

    abstract fun isLoading(): Boolean
}