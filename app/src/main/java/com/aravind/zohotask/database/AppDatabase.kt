package com.aravind.zohotask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aravind.zohotask.network.model.NewsModelData

@Database(entities = [NewsModelData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao(): NewsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getAppDbInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext, AppDatabase::class.java, "news"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}