package com.example.binchecker.di

import com.example.binchecker.ui.features.bininfo.BankCardInfoViewModel
import com.example.binchecker.ui.features.enter.EnterViewModel
import com.example.binchecker.ui.features.history.BinHistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::EnterViewModel)
    viewModel<BankCardInfoViewModel> { parameters ->
        BankCardInfoViewModel(
            cardInfoId = parameters.get(),
            getBankInformationUseCase = get(),
        )
    }
    viewModelOf(::BinHistoryViewModel)
}