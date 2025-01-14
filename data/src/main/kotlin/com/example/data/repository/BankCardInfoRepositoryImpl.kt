package com.example.data.repository

import com.example.data.model.BankCardInfoDB
import com.example.data.room.BankCardInfoDao
import com.example.domain.model.BankCardInfo
import com.example.domain.repository.BankCardInfoRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.reflect.full.memberProperties

internal class BankCardInfoRepositoryImpl(private val dao: BankCardInfoDao) : BankCardInfoRepository {
    override suspend fun getBankInformation(bin: String): Result<BankCardInfo> {
        HttpClient().use { client ->
            val response = client.get(getBinListUri(bin))
            try {
                when (response.status.value) {
                    in 200..299 -> {
                        val cardInfo = Json
                            .decodeFromString<BankCardInfo?>(response.bodyAsText())
                            ?.filterProperties()
                            ?.copy(bin = bin)
                            ?: throw Exception("No data found with bin number $bin")
                        return Result.success(cardInfo)
                    }

                    else -> with(response.status) {
                        throw Exception("Error: $description [$value]")
                    }
                }
            } catch (e: Exception) {
                return Result.failure(e)
            }
        }
    }


    override suspend fun getBankInformation(id: Long): Result<BankCardInfo> {
        return dao.getBankCardInfoById(id)
            ?.let { Result.success(it.mapToDomainBankCardInfo()) }
            ?: Result.failure(Exception("There is no data in the database with id $id"))
    }


    override suspend fun saveBankInformation(bankCardInfo: BankCardInfo): Result<Long> {
        val newId = dao.addBankCardInfo(BankCardInfoDB(bankCardInfo))
        return when {
            newId != null && newId >= 0 -> Result.success(newId)
            else -> Result.failure(Exception("Failed to save bank card information to the database"))
        }
    }


    override fun fetchSearchHistory(): Flow<List<BankCardInfo>> {
        return dao.fetchAllBankCardsInfo().map { list ->
            list.map { it.mapToDomainBankCardInfo() }
        }
    }


    private fun BankCardInfo.filterProperties(): BankCardInfo? {
        fun <T : Any> T.checkProperties(): Boolean {
            return this::class.memberProperties.any { it.call(this) != null }
        }

        return copy(
            number = number?.takeIf { it.checkProperties() },
            country = country?.takeIf { it.checkProperties() },
            bank = bank?.takeIf { it.checkProperties() }
        ).takeIf { it.checkProperties() }
    }


    private companion object {
        private const val BIN_LIST_BASE_URI = "https://lookup.binlist.net"

        fun getBinListUri(bin: String) = "$BIN_LIST_BASE_URI/$bin"
    }
}