package ir.fallahpoor.enlightened.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.fallahpoor.enlightened.data.entity.NewsEntity
import ir.fallahpoor.enlightened.data.repository.dao.NewsDao

@androidx.room.Database(
    entities = [NewsEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {

        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {

            if (INSTANCE == null) {
                synchronized(Database::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        Database::class.java,
                        "database"
                    ).allowMainThreadQueries()
                        .build()
                }
            }

            return INSTANCE!!

        }

        fun destroy() {
            INSTANCE = null
        }

    }

}