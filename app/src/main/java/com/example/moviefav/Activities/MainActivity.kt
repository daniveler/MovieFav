package com.example.moviefav.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.moviefav.Adapters.TabPageAdapter
import com.example.moviefav.R
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabBar()
    }

    private fun setUpTabBar()
    {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayoutMainActivity)
        val viewPager = findViewById<ViewPager2>(R.id.viewPagerMainActivity)

        val adapter = TabPageAdapter(this, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int)
            {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?)
            {

            }

            override fun onTabReselected(tab: TabLayout.Tab?)
            {

            }
        })
    }
}