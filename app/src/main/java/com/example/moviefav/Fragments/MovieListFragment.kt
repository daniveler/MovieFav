package com.example.moviefav.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefav.Classes.*
import com.example.moviefav.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListFragment : Fragment()
{
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

        val rvMovieList = view.findViewById<RecyclerView>(R.id.rvMovieListFragment)

        rvMovieList.layoutManager = LinearLayoutManager(context)
        rvMovieList.setHasFixedSize(true)
        getMoviesData { movies : List<Movie> ->
            rvMovieList.adapter = MovieListItemAdapter(movies)
        }
    }

    public fun getMoviesData(callback: (List<Movie>) -> Unit)
    {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)

        apiService.getMovieList().enqueue(object : Callback<MovieResponse>
        {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>)
            {
                return callback(response.body()!!.movieList)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable)
            {
                Toast.makeText(context, "Error when trying to load movie list", Toast.LENGTH_LONG).show()
            }

        })
    }
}