package com.jeff.startproject.view.appmanager

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import com.jeff.startproject.dao.RecentAppsDao
import com.jeff.startproject.model.db.RecentApp
import com.log.JFLog
import com.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppManagerViewModel @Inject internal constructor(
    private val recentDao: RecentAppsDao
) : BaseViewModel() {

    companion object {
        var enableRemove = true
    }

    enum class AppType {
        All,
        NonSystem,
        System,
        Recent,
        ;

        companion object {
            fun typeWithAppInfo(appInfo: ApplicationInfo): AppType {
                return if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 1) {
                    System
                } else {
                    NonSystem
                }
            }
        }
    }

    enum class IconType {
        Logo,
        Icon,
        Unbadged,
        Banner,
        ;
    }

    var currentAppType = AppType.Recent
        private set

    private val searchList: MutableList<CharSequence> = mutableListOf()
    private var currentList: List<CustomApplicationInfo> = emptyList()

    private val listFlow = MutableSharedFlow<List<CustomApplicationInfo>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onListFlow(): SharedFlow<List<CustomApplicationInfo>> = listFlow

    fun loadList(packageManager: PackageManager, type: AppType) {
        currentAppType = type
        when (type) {
            AppType.All -> getInstalledPackage(packageManager)
            AppType.NonSystem -> getNonSystemPackage(packageManager)
            AppType.System -> getSystemPackage(packageManager)
            AppType.Recent -> getRecentApps(packageManager)
        }
    }

    private fun getInstalledPackage(packageManager: PackageManager) {
        val result = packageManager.getInstalledApplications(0).map { CustomApplicationInfo(AppType.typeWithAppInfo(it), it) }
        updateList(packageManager, result)
    }

    private fun getNonSystemPackage(packageManager: PackageManager) {
        val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.map { CustomApplicationInfo(AppType.NonSystem, it) }
        updateList(packageManager, result)
    }

    private fun getSystemPackage(packageManager: PackageManager) {
        val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 1 }.map { CustomApplicationInfo(AppType.System, it) }
        updateList(packageManager, result)
    }

    private fun getRecentApps(packageManager: PackageManager) {
        viewModelScope.launch {
            recentDao.queryAppsFlow()
                .flowOn(Dispatchers.IO)
                .map { dbList ->
                    val result = mutableListOf<CustomApplicationInfo>()
                    for (dbData in dbList) {
                        try {
                            val info = packageManager.getApplicationInfo(dbData.packageName, 0)
                            result.add(CustomApplicationInfo(AppType.Recent, info))
                        } catch (e: Exception) {
                            recentDao.deleteApp(dbData)
                            JFLog.e(e)
                        }
                    }
                    result
                }
                .collect {
                    updateList(packageManager, it)
                }
        }
    }

    private fun updateList(packageManager: PackageManager, list: List<CustomApplicationInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                searchList.clear()
                for (app in list) {
                    searchList.add(app.appInfo.loadLabel(packageManager))
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
                    val result = arrayListOf<CustomApplicationInfo>()
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

    fun selectedApp(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recentApp = recentDao.queryAppWithPackageName(packageName)
            if (recentApp == null) {
                RecentApp(packageName, 1).also { recentDao.insertApp(it) }
            } else {
                recentApp.selectedCount++
                recentDao.updateApp(recentApp)
            }
        }
    }

    fun clearRecentApp(packageManager: PackageManager, packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                recentDao.deleteApp(packageName)
                loadList(packageManager, AppType.Recent)
            }
        }
    }
}