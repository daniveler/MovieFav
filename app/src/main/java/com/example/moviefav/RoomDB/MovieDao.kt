package com.example.moviefav.RoomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.moviefav.Classes.Movie

@Dao
interface MovieDao
{
    @Query("SELECT * FROM MOVIE")
    suspend fun getAll() : List<Movie>

    @Query("SELECT * FROM MOVIE WHERE id = :id")
    suspend fun getById(id: Int) : Movie

    @Insert
    suspend fun insert(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)
}