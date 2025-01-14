package com.example.domain.repository

import com.example.domain.model.BankCardInfo
import kotlinx.coroutines.flow.Flow

interface BankCardInfoRepository {
    suspend fun getBankInformation(bin: String): Result<BankCardInfo>

    suspend fun getBankInformation(id: Long): Result<BankCardInfo>

    suspend fun saveBankInformation(bankCardInfo: BankCardInfo): Result<Long>

    fun fetchSearchHistory(): Flow<List<BankCardInfo>>
}