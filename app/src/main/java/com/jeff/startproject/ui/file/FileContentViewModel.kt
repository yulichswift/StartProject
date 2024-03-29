package com.jeff.startproject.ui.file

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.jeff.startproject.R
import com.jeff.startproject.enums.ModelResource
import com.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileContentViewModel @Inject internal constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _status by lazy {
        MutableSharedFlow<ModelResource<String>>(
            replay = 0,
            extraBufferCapacity = 3,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }
    val status: SharedFlow<ModelResource<String>> get() = _status

    fun readFile(path: String, isInit: Boolean = false) {
        viewModelScope.launch {
            flow {
                if (isInit) {
                    delay(1000L)
                }

                val file = File(path)
                when (file.exists()) {
                    false -> {
                        val message = context.getString(R.string.message_file_not_found)
                        throw RuntimeException(message)
                    }
                    true -> {
                        val lines = file.readLines()
                        val sb = StringBuilder()
                        for (line in lines) {
                            sb.appendln(line)
                        }
                        emit(ModelResource.success(sb.toString()))
                    }
                }
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    emit(ModelResource.failure(e))
                }
                .onStart {
                    emit(ModelResource.loading())
                }
                .onCompletion {
                    emit(ModelResource.loaded())
                }
                .collect {
                    _status.tryEmit(it)
                }
        }
    }
}