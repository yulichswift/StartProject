package com.jeff.startproject.view.db

import androidx.lifecycle.viewModelScope
import com.jeff.startproject.dao.UserDao
import com.jeff.startproject.model.db.DbResult
import com.jeff.startproject.model.db.User
import com.log.JFLog
import com.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DbViewModel @Inject internal constructor(
    private val userDao: UserDao,
) : BaseViewModel() {

    companion object {
        private const val METHOD = 2
    }

    private val _dbSingleResult by lazy {
        MutableSharedFlow<DbResult<User>>(
            replay = 0,
            extraBufferCapacity = 3,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }
    val dbSingleResult: SharedFlow<DbResult<User>> get() = _dbSingleResult

    private val _dbListResult by lazy {
        MutableSharedFlow<DbResult<List<User>>>(
            replay = 0,
            extraBufferCapacity = 3,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }
    val dbListResult: SharedFlow<DbResult<List<User>>> get() = _dbListResult

    private fun insertUserFlow(users: List<User>) = flow {
        JFLog.d("insertUserFlow start")
        userDao.insertUser(users)
        emit(DbResult.success("insertUserFlow"))
    }

    private fun insertOrReplaceUserFlow(users: List<User>) = flow {
        JFLog.d("insertOrReplaceUserFlow start")
        userDao.insertOrReplaceUser(users)
        emit(DbResult.success("insertOrReplaceUserFlow"))
    }

    fun nukeTable() {
        viewModelScope.launch {
            userDao.nukeUserTable()
        }
    }

    fun insertUsers(users: List<User>) {
        viewModelScope.launch {
            insertUserFlow(users)
                .flatMapConcat {
                    when (it) {
                        is DbResult.Success -> {
                            insertOrReplaceUserFlow(users)
                        }
                        else -> {
                            throw RuntimeException("insertUsers fail")
                        }
                    }
                }
                .retryWhen { cause, attempt ->
                    JFLog.d("retryWhen cause: $cause.")
                    JFLog.d("retryWhen attempt: $attempt")
                    attempt < 3
                }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    when (it) {
                        is DbResult.Success -> {
                            JFLog.i("Db: $it")
                        }
                        is DbResult.Failure -> {
                            JFLog.e("Db: ${it.throwable}")
                        }
                        else -> {
                            JFLog.w("Db: $it")
                        }
                    }
                }
        }
    }

    fun queryUserList() {
        when (METHOD) {
            1 -> queryUsers1()
            2 -> queryUsers2()
        }
    }

    fun queryUserByName(name: String) {
        when {
            name.isBlank() -> _dbSingleResult.tryEmit(DbResult.failure(RuntimeException("Please input something")))
            else -> {
                when (METHOD) {
                    1 -> queryUserByName1(name)
                    2 -> queryUserByName2(name)
                }
            }
        }
    }

    fun queryUserLikeName(name: String) {
        when {
            name.isBlank() -> _dbListResult.tryEmit(DbResult.failure(RuntimeException("Please input something")))
            else -> {
                when (METHOD) {
                    1 -> queryUserLikeName1(name)
                    2 -> queryUserLikeName2(name)
                }
            }
        }
    }

    /**
     * 不推薦: 流不會結束, onCompletion 不會執行.
     * Dao回傳Flow, 不用再包裝, 透過map轉換成DBResult.
     */
    private fun queryUsers1() {
        viewModelScope.launch {
            userDao.queryUsersFlow()
                .flowOn(Dispatchers.IO)
                .map {
                    val data =
                        if (it.isEmpty()) {
                            null
                        } else {
                            it
                        }

                    DbResult.success(data)
                }
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    collectListResult(it)
                }
        }
    }

    /**
     * 不推薦: 流不會結束, onCompletion 不會執行.
     * Dao回傳Flow, 不用再包裝, 透過map轉換成DBResult.
     */
    private fun queryUserByName1(name: String) {
        viewModelScope.launch {
            userDao.queryUserByNameFlow(name)
                .flowOn(Dispatchers.IO)
                .map {
                    DbResult.success(it)
                }
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    collectResult(it)
                }
        }
    }

    /**
     * 不推薦: 流不會結束, onCompletion 不會執行.
     * Dao回傳Flow, 不用再包裝, 透過map轉換成DBResult.
     */
    private fun queryUserLikeName1(name: String) {
        viewModelScope.launch {
            userDao.queryUserLikeNameFlow(name)
                .flowOn(Dispatchers.IO)
                .map {
                    DbResult.success(it)
                }
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    collectResult(it)
                }
        }
    }

    /**
     * 用Flow包裝結果
     */
    private val flowQueryUsers2 = flow {
        userDao.queryUsers().also {
            val data =
                if (it.isEmpty()) {
                    null
                } else {
                    it
                }

            emit(DbResult.success(data))
        }
    }

    private fun queryUsers2() {
        viewModelScope.launch {
            flowQueryUsers2
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    collectListResult(it)
                }
        }
    }

    /**
     * 用Flow包裝結果
     */
    private fun queryUserByName2(name: String) {
        viewModelScope.launch {
            flow {
                userDao.queryUserWithName(name).also {
                    emit(DbResult.success(it))
                }
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    collectResult(it)
                }
        }
    }

    private fun queryUserLikeName2(name: String) {
        viewModelScope.launch {
            flow {
                userDao.queryUserLikeName(name).also {
                    emit(DbResult.success(it))
                }
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    emit(DbResult.failure(e))
                }
                .onStart {
                    emit(DbResult.loading())
                }
                .onCompletion {
                    emit(DbResult.loaded())
                }
                .collect {
                    collectResult(it)
                }
        }
    }

    private fun collectResult(result: DbResult<User>) {
        _dbSingleResult.tryEmit(result)
    }

    private fun collectListResult(result: DbResult<List<User>>) {
        _dbListResult.tryEmit(result)
    }
}