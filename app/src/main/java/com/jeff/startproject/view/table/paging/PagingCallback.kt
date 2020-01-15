package com.jeff.startproject.view.table.paging

interface PagingCallback {
    fun onLoading()
    fun onLoaded()
    fun onTotalCount(count: Int)
    fun onThrowable(throwable: Throwable)

}