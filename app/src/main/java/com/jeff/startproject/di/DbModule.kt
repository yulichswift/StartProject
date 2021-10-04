package com.jeff.startproject.di

import androidx.room.Room
import com.jeff.startproject.Constant
import com.jeff.startproject.MyApplication
import com.jeff.startproject.dao.RecentAppsDao
import com.jeff.startproject.dao.UserDao
import com.jeff.startproject.vo.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Singleton
    @Provides
    fun provideAppDatabase(): AppDatabase {
        return Room.databaseBuilder(
            MyApplication.applicationContext(),
            AppDatabase::class.java,
            Constant.DB_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideRecentAppsDao(appDatabase: AppDatabase): RecentAppsDao {
        return appDatabase.recentAppsDao()
    }
}