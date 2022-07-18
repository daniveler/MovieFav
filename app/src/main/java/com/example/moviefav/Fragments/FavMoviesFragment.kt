package com.example.moviefav.Fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefav.Classes.Movie
import com.example.moviefav.Adapters.MovieListItemAdapter
import com.example.moviefav.R
import com.example.moviefav.RoomDB.MovieDB
import kotlinx.coroutines.launch

class FavMoviesFragment : Fragment()
{
    lateinit var db : MovieDB
    lateinit var rvFavMovies : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        rvFavMovies = view.findViewById<RecyclerView>(R.id.rvFavMoviesFragment)

        updateMovieList()
    }

    override fun onResume()
    {
        super.onResume()

        updateMovieList()
    }

    fun updateMovieList()
    {
        db = MovieDB.getDatabase(requireActivity().applicationContext)

        var movieList = listOf<Movie>()
        lifecycleScope.launch {
            movieList = db.getMovieDao().getAll()

            movieList = movieList.sortedBy { it.title }

            rvFavMovies.layoutManager = LinearLayoutManager(context)
            rvFavMovies.setHasFixedSize(true)

            var adapter = MovieListItemAdapter(movieList)
            adapter.setOnItemClickListener(object : MovieListItemAdapter.onItemClickListener
            {
                override fun onItemClick(position: Int)
                {
                    var actualMovie = movieList.get(position)

                    openDialog(actualMovie)
                }
            })

            rvFavMovies.adapter = adapter
        }
    }

    fun openDialog(movie: Movie)
    {
        db = MovieDB.getDatabase(requireActivity().applicationContext)

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Eliminar de Favoritos")
        dialogBuilder.setMessage("¿Deseas eliminar de favoritos la película " + movie.title + "?")

        dialogBuilder.setPositiveButton("Sí", DialogInterface.OnClickListener {
                dialog, id ->
            lifecycleScope.launch {
                db.getMovieDao().delete(movie)
            }

            Toast.makeText(context, movie.title + " ha sido eliminada de favoritos", Toast.LENGTH_LONG).show()

            updateMovieList()
        })

        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })

        val alert = dialogBuilder.create()
        alert.show()
    }

}