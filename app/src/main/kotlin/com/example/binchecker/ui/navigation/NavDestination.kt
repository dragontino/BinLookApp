package com.example.binchecker.ui.navigation

sealed class NavDestination<Args : NavArguments>(private val baseRoute: String) {

    protected abstract val argumentKeys: Set<String>

    protected abstract fun Args.associateWithKeys(): Map<String, Any?>

    val route: String by lazy {
        buildString {
            append(baseRoute)
            argumentKeys
                .joinToString("/") { "{$it}" }
                .takeIf { it.isNotBlank() }
                ?.let { append("/", it) }
        }
    }

    fun createUrl(args: Args) = buildString {
        append(baseRoute)
        val argsMap = args.associateWithKeys()
        argumentKeys
            .joinToString("/") { argsMap[it].toString() }
            .takeIf { it.isNotBlank() }
            ?.let { append("/", it) }
    }



    data object Enter : NavDestination<Nothing>("enter") {
        override val argumentKeys: Set<String> = setOf()
        override fun Nothing.associateWithKeys(): Map<String, Any?> = emptyMap()
    }


    data object BankCardInfo : NavDestination<CardInfoArguments>("details") {
        const val BANK_CARD_INFO_ID_KEY = "BankCardInfo"

        override val argumentKeys: Set<String> = setOf(BANK_CARD_INFO_ID_KEY)

        override fun CardInfoArguments.associateWithKeys(): Map<String, Any?> {
            return mapOf(BANK_CARD_INFO_ID_KEY to cardInfoId)
        }
    }


    data object SearchHistory : NavDestination<EmptyNavArguments>("history") {
        override val argumentKeys: Set<String> = setOf()

        override fun EmptyNavArguments.associateWithKeys(): Map<String, Any?> = emptyMap()

        fun createUrl(): String = createUrl(EmptyNavArguments)
    }
}