package com.example.moviefav.Classes

import com.google.gson.annotations.SerializedName

data class MovieResponse
(
    @SerializedName("total_pages") val totalPages : Int,
    @SerializedName("results") val movieList : List<Movie>
)