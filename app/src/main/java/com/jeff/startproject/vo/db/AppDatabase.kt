package com.jeff.startproject.vo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeff.startproject.dao.RecentAppsDao
import com.jeff.startproject.dao.UserDao

@Database(entities = [User::class, RecentApp::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun recentAppsDao(): RecentAppsDao
}