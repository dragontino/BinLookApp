package com.example.binchecker.ui.features.bininfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BankCardInfo
import com.example.domain.usecase.GetBankInformationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BankCardInfoViewModel(
    private val cardInfoId: Long,
    private val getBankInformationUseCase: GetBankInformationUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var bankCardInfo: BankCardInfo? by mutableStateOf(null)

    private val messageChannel = Channel<String>()

    val messageFlow by lazy {
        messageChannel.receiveAsFlow().flowOn(Dispatchers.Default)
    }

    init {
        viewModelScope.launch {
            isLoading = true
            getBankInformationUseCase(cardInfoId)
                .onSuccess { bankCardInfo = it }
                .onFailure { throwable ->
                    throwable.message?.let { messageChannel.send(it) }
                }
            isLoading = false
        }
    }
}


internal sealed class Links(val url: String) {
    data object LuhnWiki : Links(url = "https://en.wikipedia.org/wiki/Luhn_algorithm")

    class Website(url: String) : Links(url)

    class Phone(phoneNumber: String) : Links("tel:$phoneNumber")
}