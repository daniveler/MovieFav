package com.example.moviefav.Retrofit

import com.example.moviefav.Classes.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse
(
    @SerializedName("total_pages") val totalPages : Int,
    @SerializedName("results") val movieList : List<Movie>
)