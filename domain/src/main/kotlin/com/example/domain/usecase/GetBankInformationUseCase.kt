package com.example.domain.usecase

import android.util.Log
import com.example.domain.model.BankCardInfo
import com.example.domain.repository.BankCardInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

sealed interface GetBankInformationUseCase {
    suspend operator fun invoke(bin: String): Result<BankCardInfo>

    suspend operator fun invoke(id: Long): Result<BankCardInfo>
}


internal class GetBankInformationUseCaseImpl(
    private val repository: BankCardInfoRepository,
    private val dispatcher: CoroutineDispatcher
) : GetBankInformationUseCase {
    private companion object {
        const val TAG = "GetBankInformationUseCase"
    }

    override suspend operator fun invoke(bin: String): Result<BankCardInfo> {
        return withContext(dispatcher) {
            repository.getBankInformation(bin).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }

    override suspend fun invoke(id: Long): Result<BankCardInfo> {
        return withContext(dispatcher) {
            repository.getBankInformation(id).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}