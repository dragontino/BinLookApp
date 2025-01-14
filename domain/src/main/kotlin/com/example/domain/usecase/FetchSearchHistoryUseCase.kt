package com.example.domain.usecase

import android.util.Log
import com.example.domain.model.BankCardInfo
import com.example.domain.repository.BankCardInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface FetchSearchHistoryUseCase {
    operator fun invoke(): Flow<List<BankCardInfo>>
}


internal class FetchSearchHistoryUseCaseImpl(
    private val repository: BankCardInfoRepository,
    private val dispatcher: CoroutineDispatcher
) : FetchSearchHistoryUseCase {
    private companion object {
        const val TAG = "FetchSearchHistoryUseCase"
    }

    override fun invoke(): Flow<List<BankCardInfo>> {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, throwable.message, throwable)
        }
        return repository.fetchSearchHistory().flowOn(dispatcher + exceptionHandler)
    }
}