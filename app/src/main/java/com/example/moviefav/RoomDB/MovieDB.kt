package com.example.moviefav.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviefav.Classes.Movie

@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieDB : RoomDatabase()
{
    abstract fun getMovieDao() : MovieDao

    companion object
    {
        @Volatile
        private var INSTANCE : MovieDB? = null

        fun getDatabase(context: Context) : MovieDB
        {
            val tempInstance = INSTANCE
            if (tempInstance != null) { return tempInstance }

            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDB::class.java,
                    "movie").build()

                INSTANCE = instance

                return instance
            }
        }
    }
}