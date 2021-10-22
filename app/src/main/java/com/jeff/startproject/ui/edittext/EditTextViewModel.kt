package com.jeff.startproject.ui.edittext

import com.ui.base.BaseViewModel
import com.utils.lifecycle.PluginEditTextLiveData
import com.utils.lifecycle.PluginEditTextMutableLiveData

class EditTextViewModel : BaseViewModel() {
    private val _pluginEditTextMutableLiveData = PluginEditTextMutableLiveData().also {
        it.setTextListener(CurrencyEditLiveDataListener())
    }

    val pluginEditTextLiveData: PluginEditTextLiveData = _pluginEditTextMutableLiveData
}