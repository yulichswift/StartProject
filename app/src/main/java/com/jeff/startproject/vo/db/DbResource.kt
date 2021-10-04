package com.jeff.startproject.vo.db

sealed class DbResource<out T> {
    companion object {
        fun <T> success(result: T?): DbResource<T> {
            return when (result) {
                null -> SuccessNoContent
                else -> Success(result)
            }
        }

        fun <T> failure(throwable: Throwable): DbResource<T> {
            return Failure(throwable)
        }

        fun <T> loading(result: T? = null): DbResource<T> {
            return when (result) {
                null -> Loading
                else -> LoadingData(result)
            }
        }

        fun <T> loaded(): DbResource<T> {
            return Loaded
        }
    }

    data class Success<out T>(val data: T) : DbResource<T>()
    object SuccessNoContent : DbResource<Nothing>()
    data class Failure(val throwable: Throwable) : DbResource<Nothing>()
    object Loading : DbResource<Nothing>()
    data class LoadingData<out T>(val data: T? = null) : DbResource<T>()
    object Loaded : DbResource<Nothing>()
}