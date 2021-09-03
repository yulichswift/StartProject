package com.jeff.startproject.view.main

import com.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : BaseViewModel() {
    private val searchFlow = MutableStateFlow("")
    fun getSearchFlow(): StateFlow<String> = searchFlow
    fun search(text: String) {
        searchFlow.value = text
    }
}