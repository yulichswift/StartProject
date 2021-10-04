package com.jeff.startproject.enums

sealed class ModelResource<out T> {
    companion object {
        fun <T> success(result: T?): ModelResource<T> {
            return when (result) {
                null -> SuccessNoContent
                else -> Success(result)
            }
        }

        fun <T> failure(throwable: Throwable): ModelResource<T> {
            return Failure(throwable)
        }

        fun <T> loading(result: T? = null): ModelResource<T> {
            return when (result) {
                null -> Loading
                else -> LoadingData(result)
            }
        }

        fun <T> loaded(): ModelResource<T> {
            return Loaded
        }
    }

    data class Success<out T>(val data: T) : ModelResource<T>()
    object SuccessNoContent : ModelResource<Nothing>()
    data class Failure(val throwable: Throwable) : ModelResource<Nothing>()
    object Loading : ModelResource<Nothing>()
    data class LoadingData<out T>(val data: T? = null) : ModelResource<T>()
    object Loaded : ModelResource<Nothing>()
}