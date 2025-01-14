package com.example.domain

import com.example.domain.usecase.FetchSearchHistoryUseCase
import com.example.domain.usecase.FetchSearchHistoryUseCaseImpl
import com.example.domain.usecase.GetBankInformationUseCase
import com.example.domain.usecase.GetBankInformationUseCaseImpl
import com.example.domain.usecase.SaveBankInformationUseCase
import com.example.domain.usecase.SaveBankInformationUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    single<GetBankInformationUseCase> {
        GetBankInformationUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<SaveBankInformationUseCase> {
        SaveBankInformationUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<FetchSearchHistoryUseCase> {
        FetchSearchHistoryUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }
}