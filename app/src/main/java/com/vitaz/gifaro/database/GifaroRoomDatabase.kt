package com.vitaz.gifaro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vitaz.gifaro.database.tables.favourite.Favourite
import com.vitaz.gifaro.database.tables.favourite.FavouriteDao
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        Favourite::class,
    ],
    version = 1, exportSchema = false
)

abstract class GifaroRoomDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    private class GifaroRoomDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GifaroRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): GifaroRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GifaroRoomDatabase::class.java,
                    "gifaro_main"
                )
                    .addCallback(GifaroRoomDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
