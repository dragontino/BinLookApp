package com.example.data

import com.example.data.repository.BankCardInfoRepositoryImpl
import com.example.data.room.SearchDatabase
import com.example.domain.repository.BankCardInfoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SearchDatabase> { SearchDatabase.getDatabase(androidContext()) }
    single<BankCardInfoRepository> {
        BankCardInfoRepositoryImpl(dao = get<SearchDatabase>().bankCardInfoDao())
    }
}