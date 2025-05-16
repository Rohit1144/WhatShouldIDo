package com.example.fit5046_g4_whatshouldido.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fit5046_g4_whatshouldido.data.local.dao.QuoteDAO
import com.example.fit5046_g4_whatshouldido.data.local.dao.TaskDAO
import com.example.fit5046_g4_whatshouldido.data.local.entity.*

@Database(
    entities = [
        Task::class,
        Quote::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDAO
    abstract fun quoteDao(): QuoteDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "app_db"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
