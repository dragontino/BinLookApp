package com.example.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.BankCardInfoDB

@TypeConverters(NumberConverter::class, BankConverter::class, CountryConverter::class)
@Database(
    entities = [BankCardInfoDB::class],
    version = 1,
    exportSchema = true
)
abstract class SearchDatabase : RoomDatabase() {
    internal abstract fun bankCardInfoDao(): BankCardInfoDao

    internal companion object {
        @Volatile
        private var instance: SearchDatabase? = null

        fun getDatabase(context: Context): SearchDatabase {
            instance?.let { return it }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context = context,
                        klass = SearchDatabase::class.java,
                        name = "SearchDatabase.db"
                    )
                    .build()

                this.instance = instance
                return instance
            }
        }
    }
}