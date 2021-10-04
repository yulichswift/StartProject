package com.jeff.startproject.vo.api

sealed class ApiResource<out T> {
    companion object {
        fun <T> success(result: T?): ApiResource<T> {
            return when (result) {
                null -> SuccessEmpty
                else -> Success(result)
            }
        }

        fun <T> failure(throwable: Throwable): ApiResource<T> {
            return Failure(throwable)
        }

        fun <T> loading(result: T? = null): ApiResource<T> {
            return when (result) {
                null -> Loading
                else -> LoadingContent(result)
            }
        }

        fun <T> loaded(): ApiResource<T> {
            return Loaded
        }
    }

    data class Success<out T>(val data: T) : ApiResource<T>()
    object SuccessEmpty : ApiResource<Nothing>()
    data class Failure(val throwable: Throwable) : ApiResource<Nothing>()
    object Loading : ApiResource<Nothing>()
    data class LoadingContent<out T>(val data: T) : ApiResource<T>()
    object Loaded : ApiResource<Nothing>()
}