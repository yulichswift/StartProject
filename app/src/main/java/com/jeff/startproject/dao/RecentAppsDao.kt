package com.jeff.startproject.dao

import androidx.room.*
import com.jeff.startproject.model.db.RecentApp
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentAppsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: RecentApp)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateApp(app: RecentApp)

    @Query("SELECT * FROM app WHERE packageName = :name")
    suspend fun queryAppWithPackageName(name: String): RecentApp?

    @Query("DELETE FROM app WHERE packageName= :name")
    suspend fun deleteApp(name: String)

    @Delete
    suspend fun deleteApp(app: RecentApp)

    @Query("SELECT * FROM app ORDER BY selected_count DESC")
    fun queryAppsFlow(): Flow<List<RecentApp>>
}