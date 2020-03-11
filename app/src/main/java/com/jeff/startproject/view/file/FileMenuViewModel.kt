package com.jeff.startproject.view.file

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeff.startproject.MyApplication
import com.jeff.startproject.R
import com.jeff.startproject.utils.fileExtension
import com.jeff.startproject.view.base.BaseViewModel
import com.jeff.startproject.view.base.NavigateItem
import com.log.JFLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class FileMenuViewModel : BaseViewModel() {
    private val appContext = MyApplication.applicationContext()

    private val rootPath = "${Environment.getExternalStorageDirectory().path}/scripts"
    val dirPath = "$rootPath/dir.sh"
    val mvPath = "$rootPath/mv.sh"
    val rmPath = "$rootPath/rm.sh"

    private val mBtnStartText = MutableLiveData(appContext.getString(R.string.text_start))
    val btnStartText: LiveData<String> = mBtnStartText

    override fun navigateTo(item: NavigateItem) {
        viewModelScope.launch {
            // Can choose join or cancel
            joinPreviousOrRun {
                super.navigateTo(item)
            }
        }
    }

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

                    val inputStream = MyApplication.applicationContext().resources.openRawResource(R.raw.sample)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    while (reader.ready()) {
                        val line = reader.readLine()
                        val separateIndex = line.lastIndexOf("->")
                        val separateCount = 3

                        val first = line.substring(separateIndex + separateCount, line.length)
                        val last = line.substring(0, separateIndex)

                        when (first.fileExtension) {
                            ".xml" -> {
                                sbDel.appendln("rm $first")
                            }
                            else -> {
                                val out = "mv $first $last"
                                sbMv.appendln(out)
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
                            sbDir.appendln("mkdir $dir")
                        }
                        fileDir.appendText(sbDir.toString())
                    }

                    fileRm.appendText(sbDel.toString())
                    JFLog.d("Finish: ${System.currentTimeMillis() - startTime} ns")

                    delay(5000L)
                    emit(true)
                }
                    .flowOn(Dispatchers.IO)
                    .onStart {
                        mProcessing.value = true
                        mBtnStartText.value = appContext.getString(R.string.text_processing)
                    }
                    .onCompletion {
                        mProcessing.value = false
                        mBtnStartText.value = appContext.getString(R.string.text_finish)
                    }
                    .collect {
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

                mBtnStartText.value = appContext.getString(R.string.text_start)

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