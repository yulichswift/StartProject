package com.jeff.startproject.view.appmanager

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.viewModelScope
import com.jeff.startproject.dao.RecentAppsDao
import com.jeff.startproject.vo.api.ApiResource
import com.jeff.startproject.vo.db.RecentApp
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

    private val listFlow = MutableSharedFlow<ApiResource<List<CustomApplicationInfo>>>(
        replay = 0,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onListFlow(): SharedFlow<ApiResource<List<CustomApplicationInfo>>> = listFlow

    fun loadList(packageManager: PackageManager, type: AppType) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                when (type) {
                    AppType.All -> getInstalledPackage(packageManager)
                    AppType.NonSystem -> getNonSystemPackage(packageManager)
                    AppType.System -> getSystemPackage(packageManager)
                    AppType.Recent -> getRecentApps(packageManager)
                }.map { list ->
                    searchList.clear()
                    for (app in list) {
                        searchList.add(app.appInfo.loadLabel(packageManager))
                    }

                    currentAppType = type
                    currentList = list

                    ApiResource.success(list)
                }.onStart {
                    emit(ApiResource.loading())
                }.onCompletion {
                    emit(ApiResource.loaded())
                }.catch {
                    emit(ApiResource.failure(it))
                }.collect {
                    listFlow.tryEmit(it)
                }
            }
        }
    }

    private fun getInstalledPackage(packageManager: PackageManager) =
        flow {
            val result = packageManager.getInstalledApplications(0).map { CustomApplicationInfo(AppType.typeWithAppInfo(it), it) }
            emit(result)
        }

    private fun getNonSystemPackage(packageManager: PackageManager) =
        flow {
            val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.map { CustomApplicationInfo(AppType.NonSystem, it) }
            emit(result)
        }

    private fun getSystemPackage(packageManager: PackageManager) =
        flow {
            val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 1 }.map { CustomApplicationInfo(AppType.System, it) }
            emit(result)
        }

    private fun getRecentApps(packageManager: PackageManager) =
        flow {
            recentDao.queryApps().mapNotNull { dbData ->
                try {
                    val info = packageManager.getApplicationInfo(dbData.packageName, 0)
                    CustomApplicationInfo(AppType.Recent, info)
                } catch (e: Exception) {
                    recentDao.deleteApp(dbData)
                    JFLog.e(e)
                    null
                }
            }.also { emit(it) }
        }

    fun filterCurrentList(filter: CharSequence?) {
        if (filter == null || filter.isEmpty()) {
            listFlow.tryEmit(ApiResource.success(currentList))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                cancelPreviousThenRun {
                    val result = arrayListOf<CustomApplicationInfo>()
                    for (i in 0 until searchList.size) {
                        if (searchList[i].contains(filter, true)) {
                            result.add(currentList[i])
                        }
                    }
                    listFlow.tryEmit(ApiResource.success(result))
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