package com.jeff.startproject.view.appmanager

import androidx.lifecycle.viewModelScope
import com.jeff.startproject.dao.RecentAppsDao
import com.jeff.startproject.repository.appmanager.PackageRepository
import com.jeff.startproject.view.appmanager.enums.AppType
import com.jeff.startproject.view.appmanager.enums.ViewType
import com.jeff.startproject.view.appmanager.vo.AppViewData
import com.jeff.startproject.vo.api.ApiResource
import com.jeff.startproject.vo.db.RecentApp
import com.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppManagerViewModel @Inject internal constructor(
    private val packageRepository: PackageRepository,
    private val recentDao: RecentAppsDao,
) : BaseViewModel() {

    companion object {
        var enableRemove = true
    }

    val currentAppType get() = packageRepository.currentAppType

    private val placeholderItem = mutableListOf<AppViewData>().apply {
        repeat(9) {
            add(AppViewData(viewType = ViewType.Loading))
        }
    }

    private val listFlow = MutableSharedFlow<ApiResource<List<AppViewData>>>(
        replay = 0,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun onListFlow(): SharedFlow<ApiResource<List<AppViewData>>> = listFlow

    fun loadList(type: AppType) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                when (type) {
                    AppType.All -> packageRepository.getInstalledPackage()
                    AppType.NonSystem -> packageRepository.getNonSystemPackage()
                    AppType.System -> packageRepository.getSystemPackage()
                    AppType.Recent -> packageRepository.getRecentApps()
                }.flatMapConcat { list ->
                    packageRepository.updateCacheData(type, list).getCacheData()
                }.map {
                    ApiResource.success(it)
                }.onStart {
                    emit(ApiResource.loading(placeholderItem))
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

    fun filterCurrentList(filter: CharSequence?) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                packageRepository.setFilter(filter).getCacheData()
                    .collect { list ->
                        listFlow.tryEmit(ApiResource.success(list))
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

    fun clearRecentApp(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelPreviousThenRun {
                recentDao.deleteApp(packageName)
                loadList(AppType.Recent)
            }
        }
    }
}