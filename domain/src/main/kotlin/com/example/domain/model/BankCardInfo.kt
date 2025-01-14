package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BankCardInfo(
    val id: Long = 0,
    val bin: String = "",
    val number: Number? = null,
    val country: Country? = null,
    val bank: Bank? = null,
    val scheme: String? = null,
    val type: String? = null,
    val brand: String? = null,
    val prepaid: Boolean? = null,
)


@Serializable
data class Number(
    val length: Int? = null,
    val luhn: Boolean? = null
)


@Serializable
data class Country(
    val numeric: String? = null,
    val alpha2: String? = null,
    val name: String? = null,
    val emoji: String? = null,
    val currency: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null
)


@Serializable
data class Bank(
    val name: String? = null,
    val url: String? = null,
    val phone: String? = null,
    val city: String? = null
)
