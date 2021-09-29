package com.jeff.startproject.view.appmanager

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import com.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppManagerViewModel @Inject internal constructor() : BaseViewModel() {
    enum class AppType {
        All,
        NonSystem,
        System,
        ;
    }

    enum class IconType {
        Logo,
        Icon,
        Unbadged,
        Banner,
        ;
    }

    private val searchList: MutableList<CharSequence> = mutableListOf()
    private var currentList: List<ApplicationInfo> = emptyList()

    private val listFlow = MutableSharedFlow<List<ApplicationInfo>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onListFlow(): SharedFlow<List<ApplicationInfo>> = listFlow

    private fun getInstalledPackage(packageManager: PackageManager) {
        val result = packageManager.getInstalledApplications(0)
        updateList(packageManager, result)
    }

    private fun getNonSystemPackage(packageManager: PackageManager) {
        val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
        updateList(packageManager, result)
    }

    private fun getSystemPackage(packageManager: PackageManager) {
        val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 1 }
        updateList(packageManager, result)
    }

    fun loadList(packageManager: PackageManager, type: AppType) {
        when (type) {
            AppType.All -> getInstalledPackage(packageManager)
            AppType.NonSystem -> getNonSystemPackage(packageManager)
            AppType.System -> getSystemPackage(packageManager)
        }
    }

    private fun updateList(packageManager: PackageManager, list: List<ApplicationInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                searchList.clear()
                for (app in list) {
                    searchList.add(app.loadLabel(packageManager))
                }
                currentList = list
                listFlow.tryEmit(list)
            }
        }
    }

    fun filterCurrentList(filter: CharSequence?) {
        if (filter == null || filter.isEmpty()) {
            listFlow.tryEmit(currentList)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                cancelPreviousThenRun {
                    val result = arrayListOf<ApplicationInfo>()
                    for (i in 0 until searchList.size) {
                        if (searchList[i].contains(filter, true)) {
                            result.add(currentList[i])
                        }
                    }
                    listFlow.tryEmit(result)
                }
            }
        }
    }
}