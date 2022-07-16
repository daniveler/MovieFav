package com.example.moviefav.Classes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMoviesApiInterface
{
    @GET("https://api.themoviedb.org/3/search/movie?api_key=975785a8323d37a98a73608f9ae71b19&language=es-ES")
    fun getMovieList(
        @Query("page") page: Int,
        @Query("query") query: String) : Call<MovieResponse>
}