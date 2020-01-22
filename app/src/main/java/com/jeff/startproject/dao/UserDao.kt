package com.jeff.startproject.dao

import androidx.room.*
import com.jeff.startproject.model.db.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUser(users: List<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)

    @Query("DELETE FROM user WHERE user_name= :userName")
    fun deleteUser(userName: String)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM user")
    fun nukeUserTable()

    @Query("SELECT * FROM user")
    fun queryUsers(): List<User>

    @Query("SELECT * FROM user WHERE user_name = :name")
    fun queryUserWithName(name: String): User

    @Query("SELECT * FROM user WHERE user_name LIKE '%' || :name || '%'")
    fun queryUserLikeName(name: String): User

    // 不推薦: 回傳Flow物件
    @Query("SELECT * FROM user")
    fun queryUsersFlow(): Flow<List<User>>

    // 不推薦: 回傳Flow物件
    @Query("SELECT * FROM user WHERE user_name = :name")
    fun queryUserByNameFlow(name: String): Flow<User>

    // 不推薦: 回傳Flow物件
    @Query("SELECT * FROM user WHERE user_name LIKE '%' || :name || '%'")
    fun queryUserLikeNameFlow(name: String): Flow<User>
}