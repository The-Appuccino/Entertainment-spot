package com.appuccino.entertainment_spot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

/**
 * App’s single activity host: contains the BottomNavigationView,
 * sets the default fragment (Movies list), and swaps fragments in R.id.nav_host
 * when the user taps Movies / Series / Search.
 * */
class MainActivity : AppCompatActivity() {
    // Fragments declared once for reuse
    //private val entertainmentListFragment = EntertainmentListFragment()

    /**
     * for temporarily displaying a screen for any un-implemented navigation screens
     * since "when" expression in the bottomNavigationView.requires an else branch.
    **/
    private val entertainmentDetailFragment = EntertainmentDetailFragment()


    /** Function to takes the fireStoreDataUploader file and launches the coroutine
     *  and to gets pulls from the api sources and uploads to the firestore database.
     */
    private fun fireStoreUploader() {
        val upload = FirestoreDataUploader
        lifecycleScope.launch {
            upload.uploadPopularMovies()
            upload.uploadPopularSeries()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //call the function to upload data to firestore
        //fireStoreUploader()

        // Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, EntertainmentListFragment.newInstance(EntertainmentListFragment.TYPE_MOVIE))
            .commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_movies -> EntertainmentListFragment.newInstance(EntertainmentListFragment.TYPE_MOVIE)
                R.id.nav_series -> EntertainmentListFragment.newInstance(EntertainmentListFragment.TYPE_SERIES) // update if you add a separate fragment later
                R.id.nav_search -> EntertainmentSearchFragment()
                R.id.nav_favorite_list -> EntertainmentFavoriteListFragment()
                R.id.nav_settings -> EntertainmentSettingsFragment()
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
