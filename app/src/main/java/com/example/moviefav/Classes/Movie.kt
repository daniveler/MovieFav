package com.example.moviefav.Classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
   @PrimaryKey(autoGenerate = false) @SerializedName("id")  var id : Int,
   @SerializedName("original_title") var title : String,
   @SerializedName("overview") var description : String,
   @SerializedName("poster_path") var poster : String?,
   @SerializedName("release_date") var relaseDate : String,
   @SerializedName("vote_average") var score : Double
)