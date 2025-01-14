package com.example.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.BankCardInfoDB
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BankCardInfoDao {
    @Query("SELECT * FROM CardsInfo WHERE id = :id")
    suspend fun getBankCardInfoById(id: Long): BankCardInfoDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBankCardInfo(bankCardInfo: BankCardInfoDB): Long?

    @Query("SELECT * FROM CardsInfo")
    fun fetchAllBankCardsInfo(): Flow<BankCardInfoDB>
}