package com.example.domain.usecase

import android.util.Log
import com.example.domain.model.BankCardInfo
import com.example.domain.repository.BankCardInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

sealed interface SaveBankInformationUseCase {
    suspend operator fun invoke(bankCardInfo: BankCardInfo): Result<Long>
}


internal class SaveBankInformationUseCaseImpl(
    private val repository: BankCardInfoRepository,
    private val dispatcher: CoroutineDispatcher
) : SaveBankInformationUseCase {
    private companion object {
        const val TAG = "SaveBankCardInformationUseCase"
    }

    override suspend fun invoke(bankCardInfo: BankCardInfo): Result<Long> {
        return withContext(dispatcher) {
            repository.saveBankInformation(bankCardInfo).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}