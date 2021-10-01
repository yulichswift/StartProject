package com.jeff.startproject.view.file

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeff.startproject.R
import com.jeff.startproject.enums.ModelResult
import com.log.JFLog
import com.utils.extension.fileExtension
import com.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class FileMenuViewModel @Inject internal constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val rootPath = "${context.getExternalFilesDir(null)!!.path}/scripts"
    val dirPath = "$rootPath/dir.sh"
    val mvPath = "$rootPath/mv.sh"
    val rmPath = "$rootPath/rm.sh"

    private val _btnStartText = MutableLiveData(context.getString(R.string.text_start))
    val btnStartText: LiveData<String> = _btnStartText

    private val _request by lazy {
        MutableSharedFlow<ModelResult<Nothing>>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }
    val request: SharedFlow<ModelResult<Nothing>> get() = _request

    fun start() {
        viewModelScope.launch {
            joinPreviousOrRun {
                flow {
                    val startTime = System.currentTimeMillis()

                    File(rootPath).mkdirs()

                    val fileDir = createNewFile(dirPath)
                    val fileMv = createNewFile(mvPath)
                    val fileRm = createNewFile(rmPath)

                    val dirList = mutableListOf<String>()
                    val sbMv = StringBuilder()
                    val sbDel = StringBuilder()

                    val inputStream = context.resources.openRawResource(R.raw.sample)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    while (reader.ready()) {
                        val line = reader.readLine()
                        val separateIndex = line.lastIndexOf("->")
                        val separateCount = 3

                        val first = line.substring(separateIndex + separateCount, line.length)
                        val last = line.substring(0, separateIndex)

                        when (first.fileExtension) {
                            ".xml" -> {
                                sbDel.appendLine("rm $first")
                            }
                            else -> {
                                val out = "mv $first $last"
                                sbMv.appendLine(out)
                            }
                        }

                        last.substring(0, last.lastIndexOf('/')).also {
                            if (!dirList.contains(it)) {
                                dirList.add(it)
                            }
                        }
                    }

                    reader.close()
                    inputStream.close()

                    fileMv.appendText(sbMv.toString())

                    if (dirList.isNotEmpty()) {
                        val sbDir = StringBuilder()
                        for (dir in dirList) {
                            sbDir.appendLine("mkdir $dir")
                        }
                        fileDir.appendText(sbDir.toString())
                    }

                    fileRm.appendText(sbDel.toString())
                    JFLog.d("Finish: ${System.currentTimeMillis() - startTime} ns")

                    delay(5000L)
                    emit(ModelResult.success(null))
                }
                    .flowOn(Dispatchers.IO)
                    .onStart {
                        _btnStartText.value = context.getString(R.string.text_processing)
                        emit(ModelResult.loading())
                    }
                    .onCompletion {
                        _btnStartText.value = context.getString(R.string.text_finish)
                        emit(ModelResult.loaded())
                    }
                    .collect {
                        _request.tryEmit(it)
                    }
            }
        }
    }

    fun resetFiles() {
        viewModelScope.launch {
            joinPreviousOrRun {
                File(dirPath).delete()
                File(mvPath).delete()
                File(rmPath).delete()

                _btnStartText.value = context.getString(R.string.text_start)

                delay(1000L)
            }
        }
    }

    private fun createNewFile(path: String): File {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        return file
    }
}