package com.example.binchecker.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavArguments

data object EmptyNavArguments : NavArguments

@Serializable
data class CardInfoArguments(val cardInfoId: Long) : NavArguments