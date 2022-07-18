package com.example.moviefav.Adapters

import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefav.Classes.Movie
import com.example.moviefav.R
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MovieListItemAdapter(val movieList : List<Movie>?) : RecyclerView.Adapter<MovieListItemAdapter.MovieViewHolder>()
{
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener
    {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener)
    {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(itemView, mListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
    {
        if (!movieList.isNullOrEmpty())
        {
            val movie = movieList[position]
            Picasso.get().load(Uri.parse( "https://image.tmdb.org/t/p/w500" + movie.poster)).into(holder.ivPoster)
            holder.tvTitle.setText(movie.title)
            holder.tvDescription.setText(movie.description)
            holder.tvScore.setText("" + movie.score + " ⭐")

            val spanishLocale = Locale("es", "ES")

            val apiFormat       = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val spanishFormat   = DateTimeFormatter.ofPattern("d MMMM, yyyy", spanishLocale)

            if (movie.relaseDate != null && movie.relaseDate != "")
            {
                val date = LocalDate.parse(movie.relaseDate, apiFormat)

                val spanishDate = date.format(spanishFormat)

                holder.tvReleaseDate.setText(spanishDate.toString())
            }
        }
    }

    override fun getItemCount(): Int
    {
        if (!movieList.isNullOrEmpty())
        {
            return movieList.size
        }
        else
            return 0
    }

    class MovieViewHolder(itemView : View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView)
    {
        val ivPoster        = itemView.findViewById<ImageView>(R.id.ivPosterMovieListItem)
        val tvTitle         = itemView.findViewById<TextView>(R.id.tvTitleMovieListItem)
        val tvDescription   = itemView.findViewById<TextView>(R.id.tvDescriptionMovieListItem)
        val tvScore         = itemView.findViewById<TextView>(R.id.tvScoreMovieListItem)
        val tvReleaseDate   = itemView.findViewById<TextView>(R.id.tvReleaseDateMovieListItem)

        init
        {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}