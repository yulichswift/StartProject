package com.jeff.startproject.dao

import androidx.room.*
import com.jeff.startproject.vo.db.RecentApp

@Dao
interface RecentAppsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApp(app: RecentApp)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateApp(app: RecentApp)

    @Query("SELECT * FROM app WHERE packageName = :name")
    fun queryAppWithPackageName(name: String): RecentApp?

    @Query("DELETE FROM app WHERE packageName= :name")
    fun deleteApp(name: String)

    @Delete
    fun deleteApp(app: RecentApp)

    @Query("SELECT * FROM app ORDER BY selected_count DESC")
    fun queryApps(): List<RecentApp>
}