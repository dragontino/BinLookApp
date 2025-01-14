package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Bank
import com.example.domain.model.BankCardInfo
import com.example.domain.model.Country
import com.example.domain.model.Number

@Entity(tableName = "CardsInfo")
internal data class BankCardInfoDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bin: String,
    val number: Number?,
    val country: Country?,
    val bank: Bank?,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean?,
) {
    constructor(cardInfo: BankCardInfo) : this(
        bin = cardInfo.bin,
        number = cardInfo.number,
        country = cardInfo.country,
        bank = cardInfo.bank,
        scheme = cardInfo.scheme,
        type = cardInfo.type,
        brand = cardInfo.brand,
        prepaid = cardInfo.prepaid
    )

    fun mapToDomainBankCardInfo() = BankCardInfo(
        id = id,
        bin = bin,
        number = number,
        country = country,
        bank = bank,
        scheme = scheme,
        type = type,
        brand = brand,
        prepaid = prepaid
    )
}
