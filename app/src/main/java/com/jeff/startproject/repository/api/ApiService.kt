package com.jeff.startproject.repository.api

import com.jeff.startproject.vo.api.UserDetailItem
import com.jeff.startproject.vo.api.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("/users")
    suspend fun fetchUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Response<List<UserItem>>

    @GET
    suspend fun fetchUsers(@Url url: String): Response<List<UserItem>>

    @GET("/users/{username}")
    suspend fun fetchUserDetail(@Path("username") username: String): Response<UserDetailItem>

    @GET("/publicPlatforms/v1/players")
    suspend fun testNoContentApi(): Response<Void>
}