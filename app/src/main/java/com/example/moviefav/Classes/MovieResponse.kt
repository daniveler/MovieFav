package com.example.moviefav.Classes

import com.google.gson.annotations.SerializedName

data class MovieResponse
(
    @SerializedName("results") val movieList : List<Movie>
)