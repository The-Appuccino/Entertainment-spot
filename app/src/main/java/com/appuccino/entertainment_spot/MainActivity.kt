package com.appuccino.entertainment_spot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    // Fragments declared once for reuse
    //private val entertainmentListFragment = EntertainmentListFragment()
    private val entertainmentDetailFragment = EntertainmentDetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, EntertainmentListFragment.newInstance(EntertainmentListFragment.TYPE_MOVIE))
            .commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_movies -> EntertainmentListFragment.newInstance(EntertainmentListFragment.TYPE_MOVIE)
                R.id.nav_series -> EntertainmentListFragment.newInstance(EntertainmentListFragment.TYPE_SERIES) // update if you add a separate fragment later
                //R.id.nav_detail -> entertainmentDetailFragment
                else -> entertainmentDetailFragment
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host, selectedFragment)
                .commit()

            true
        }
    }
}
