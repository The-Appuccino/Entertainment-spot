package com.appuccino.entertainment_spot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        //fragments
        val fragment1: Fragment = EntertainmentListFragment()
        val fragment2: Fragment = EntertainmentDetailFragment()

        val bottomNavigationView: BottomNavigationView = findViewById((R.id.bottom_navigation))

        //handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_movies -> fragment = fragment1
                R.id.nav_series -> fragment = fragment1
            }
            fragmentManager.beginTransaction().replace(R.id.)
        }

    }
}