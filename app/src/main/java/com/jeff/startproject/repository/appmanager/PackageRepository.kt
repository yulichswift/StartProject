package com.jeff.startproject.repository.appmanager

import android.content.Context
import android.content.pm.ApplicationInfo
import com.jeff.startproject.dao.RecentAppsDao
import com.jeff.startproject.view.appmanager.enums.AppType
import com.jeff.startproject.view.appmanager.vo.AppViewData
import com.log.JFLog
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class PackageRepository @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val recentDao: RecentAppsDao,
) {
    private val packageManager get() = context.packageManager

    var currentAppType = AppType.Recent
        private set

    var filterChars: CharSequence? = null
        private set

    private val searchList: MutableList<CharSequence> = mutableListOf()

    private var currentList: List<AppViewData> = emptyList()

    fun setFilter(filter: CharSequence?) =
        apply {
            filterChars = filter
        }

    fun getInstalledPackage() =
        flow {
            val result = packageManager.getInstalledApplications(0).map { AppViewData(AppType.typeWithAppInfo(it), it) }
            emit(result)
        }

    fun getNonSystemPackage() =
        flow {
            val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.map { AppViewData(AppType.NonSystem, it) }
            emit(result)
        }

    fun getSystemPackage() =
        flow {
            val result = packageManager.getInstalledApplications(0).filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 1 }.map { AppViewData(AppType.System, it) }
            emit(result)
        }

    fun getRecentApps() =
        flow {
            recentDao.queryApps().mapNotNull { dbData ->
                try {
                    val info = packageManager.getApplicationInfo(dbData.packageName, 0)
                    AppViewData(AppType.Recent, info)
                } catch (e: Exception) {
                    recentDao.deleteApp(dbData)
                    JFLog.e(e)
                    null
                }
            }.also { emit(it) }
        }

    fun updateCacheData(type: AppType, list: List<AppViewData>) =
        apply {
            searchList.clear()
            for (app in list) {
                searchList.add(app.appInfo!!.loadLabel(packageManager))
            }

            currentAppType = type
            currentList = list
        }

    fun getCacheData() =
        flow {
            val filter = filterChars
            if (filter == null || filter.isEmpty()) {
                emit(currentList)
            } else {
                val result = arrayListOf<AppViewData>()
                for (i in 0 until searchList.size) {
                    if (searchList[i].contains(filter, true)) {
                        result.add(currentList[i])
                    }
                }
                emit(result)
            }
        }
}