package com.example.data.room

import androidx.room.TypeConverter
import com.example.domain.model.Bank
import com.example.domain.model.Country
import com.example.domain.model.Number
import kotlinx.serialization.json.Json

internal object NumberConverter {
    @TypeConverter
    fun Number.parseToString(): String = Json.encodeToString(this)

    @TypeConverter
    fun String.parseToNumber(): Number = Json.decodeFromString(this)
}

internal object BankConverter {
    @TypeConverter
    fun Bank.parseToString(): String = Json.encodeToString(this)

    @TypeConverter
    fun String.parseToBank(): Bank = Json.decodeFromString(this)
}

internal object CountryConverter {
    @TypeConverter
    fun Country.parseToString(): String = Json.encodeToString(this)

    @TypeConverter
    fun String.parseToCounty(): Country = Json.decodeFromString(this)
}