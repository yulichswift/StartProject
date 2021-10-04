package com.jeff.startproject.vo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app")
data class RecentApp(
    @PrimaryKey var packageName: String,

    @ColumnInfo(name = "selected_count") var selectedCount: Long = 0,
)