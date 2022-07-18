package com.example.moviefav.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviefav.Fragments.FavMoviesFragment
import com.example.moviefav.Fragments.MovieListFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int): FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> MovieListFragment()
            1 -> FavMoviesFragment()
            else -> MovieListFragment()
        }
    }
}