package com.example.binchecker.ui.features.enter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.binchecker.ui.navigation.CardInfoArguments
import com.example.binchecker.ui.navigation.NavArguments
import com.example.domain.model.BankCardInfo
import com.example.domain.usecase.GetBankInformationUseCase
import com.example.domain.usecase.SaveBankInformationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EnterViewModel(
    private val getBankInformationUseCase: GetBankInformationUseCase,
    private val saveBankInformationUseCase: SaveBankInformationUseCase
) : ViewModel() {

    sealed interface EnterEvent {
        data class ShowSnackbar(val message: String) : EnterEvent
        data class NavigateToDestination(val arguments: NavArguments) : EnterEvent
    }

    var isLoading by mutableStateOf(false)
    var bin by mutableStateOf("")

    private val eventChannel = Channel<EnterEvent>()

    val eventFlow: Flow<EnterEvent> by lazy {
        eventChannel.receiveAsFlow().flowOn(Dispatchers.Default)
    }


    fun getBankInformation() {
        viewModelScope.launch {
            isLoading = true
            getBankInformationUseCase(bin)
                .onSuccess { bankCardInfo ->
                    saveBankCardInformation(bankCardInfo)
                        ?.let(::CardInfoArguments)
                        ?.let(EnterEvent::NavigateToDestination)
                        ?.let { eventChannel.send(it) }
                }
                .onFailure { exception ->
                    exception.message?.let { eventChannel.send(EnterEvent.ShowSnackbar(it)) }
                }
            isLoading = false
        }
    }


    fun showHistory() {

    }


    private suspend fun saveBankCardInformation(info: BankCardInfo): Long? {
        val result = saveBankInformationUseCase(info).onFailure { throwable ->
            throwable.message?.let { eventChannel.send(EnterEvent.ShowSnackbar(it)) }
        }
        return result.getOrNull()
    }
}