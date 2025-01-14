package com.example.binchecker.ui.features.history

import androidx.lifecycle.ViewModel
import com.example.domain.model.BankCardInfo
import com.example.domain.usecase.FetchSearchHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow

class BinHistoryViewModel(
    private val useCase: FetchSearchHistoryUseCase
) : ViewModel() {
    private val messageChannel = Channel<String>()

    val messageFlow: Flow<String> by lazy {
        messageChannel.receiveAsFlow().flowOn(Dispatchers.Default)
    }

    val searchHistoryFlow: Flow<List<BankCardInfo>> by lazy {
        useCase().catch { throwable ->
            throwable.message?.let { messageChannel.send(it) }
        }
    }
}