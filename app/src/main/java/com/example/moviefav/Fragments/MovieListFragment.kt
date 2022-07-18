package com.example.moviefav.Fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefav.Adapters.MovieListItemAdapter
import com.example.moviefav.Classes.*
import com.example.moviefav.R
import com.example.moviefav.Retrofit.MovieApiService
import com.example.moviefav.Retrofit.MovieResponse
import com.example.moviefav.Retrofit.PopularMoviesApiInterface
import com.example.moviefav.Retrofit.SearchMoviesApiInterface
import com.example.moviefav.RoomDB.MovieDB
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieListFragment : Fragment()
{
    lateinit var rvMovieList : RecyclerView
    lateinit var db : MovieDB

    var totalPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val searchView      = view.findViewById<SearchView>(R.id.searchViewMovieListFragment)
        rvMovieList         = view.findViewById<RecyclerView>(R.id.rvMovieListFragment)
        val btNext          = view.findViewById<LinearLayout>(R.id.btNextPageMovieListFragment)
        val btPrev          = view.findViewById<LinearLayout>(R.id.btPrevPageMovieListFragment)

        var pageNum = 1
        var isSearching = false

        searchView.isIconifiedByDefault = false

        loadPopularMoviesPage(pageNum)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean
            {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean
            {
                val searchText = newText!!.lowercase(Locale.getDefault())

                if (searchText.isNotEmpty())
                {
                    isSearching = true
                    loadSearchMoviesPage(1, searchText)
                }
                else
                {
                    isSearching = false
                    pageNum = 1
                    loadPopularMoviesPage(pageNum)
                }

                return true
            }
        })

        btPrev.setOnClickListener {
            if (!isSearching)
            {
                if (pageNum > 1) { loadPopularMoviesPage(--pageNum) }
                else { Toast.makeText(context, "Ya estás en la primera página", Toast.LENGTH_SHORT).show() }
            }
            else
            {
                if (pageNum > 1) { loadSearchMoviesPage(--pageNum, searchView.query.toString()) }
                else { Toast.makeText(context, "Ya estás en la primera página", Toast.LENGTH_SHORT).show() }
            }

        }

        btNext.setOnClickListener {
            if (!isSearching)
            {
                if (pageNum < totalPages) { loadPopularMoviesPage(++pageNum) }
                else { Toast.makeText(context, "Ya estás en la última página", Toast.LENGTH_SHORT).show() }
            }
            else
            {
                if (pageNum < totalPages) { loadSearchMoviesPage(++pageNum, searchView.query.toString()) }
                else { Toast.makeText(context, "Ya estás en la última página", Toast.LENGTH_SHORT).show() }
            }
        }

    }

    override fun onResume()
    {
        super.onResume()

        loadPopularMoviesPage(1)
    }

    fun getAllMoviesData(pageNum : Int, callback: (List<Movie>, Int) -> Unit)
    {
        val apiService = MovieApiService.getInstance().create(PopularMoviesApiInterface::class.java)

        apiService.getMovieList(pageNum).enqueue(object : Callback<MovieResponse>
        {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>)
            {
                return callback(response.body()!!.movieList, response.body()!!.totalPages)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable)
            {
                Toast.makeText(context, "Error al cargar la lista de películas. Comprueba tu conexión a internet", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getSearchMoviesData(pageNum : Int, query: String, callback: (List<Movie>, totalPages: Int) -> Unit)
    {
        val apiService = MovieApiService.getInstance().create(SearchMoviesApiInterface::class.java)

        apiService.getMovieList(pageNum, query).enqueue(object : Callback<MovieResponse>
        {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>)
            {
                return callback(response.body()!!.movieList, response.body()!!.totalPages)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable)
            {
                Toast.makeText(context, "Error al cargar la lista de películas. Comprueba tu conexión a internet", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun loadPopularMoviesPage(pageNum: Int)
    {
        rvMovieList.layoutManager = LinearLayoutManager(context)
        rvMovieList.setHasFixedSize(true)
        getAllMoviesData(pageNum) { movies: List<Movie>, totalPages: Int ->
            var adapter = MovieListItemAdapter(movies)
            this.totalPages = totalPages

            rvMovieList.adapter = adapter

            adapter.setOnItemClickListener(object : MovieListItemAdapter.onItemClickListener
            {
                override fun onItemClick(position: Int)
                {
                    var actualMovie = movies.get(position)

                    openDialog(actualMovie)
                }
            })
        }
    }

    fun loadSearchMoviesPage(pageNum: Int, query: String)
    {
        rvMovieList.layoutManager = LinearLayoutManager(context)
        rvMovieList.setHasFixedSize(true)
        getSearchMoviesData(pageNum, query) { movies: List<Movie>, totalPages: Int ->
            this.totalPages = totalPages

            var adapter = MovieListItemAdapter(movies)

            rvMovieList.adapter = adapter

            adapter.setOnItemClickListener(object : MovieListItemAdapter.onItemClickListener
            {
                override fun onItemClick(position: Int)
                {
                    var actualMovie = movies.get(position)

                    openDialog(actualMovie)
                }
            })
        }
    }

    fun openDialog(movie: Movie)
    {
        db = MovieDB.getDatabase(requireActivity().applicationContext)

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Añadir a Favoritos")
        dialogBuilder.setMessage("¿Deseas añadir a favoritos la película " + movie.title + "?")

        dialogBuilder.setPositiveButton("Sí", DialogInterface.OnClickListener {
            dialog, id ->
                lifecycleScope.launch {
                    var favoritesList = db.getMovieDao().getAll()

                    if (!favoritesList.contains(movie))
                    {
                        db.getMovieDao().insert(movie)
                        Toast.makeText(context, "Añadida a favoritos: " + movie.title, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(context, movie.title + " ya está añadida a favoritos", Toast.LENGTH_LONG).show()
                    }
                }
        })

        dialogBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })

        val alert = dialogBuilder.create()
        alert.show()
    }
}