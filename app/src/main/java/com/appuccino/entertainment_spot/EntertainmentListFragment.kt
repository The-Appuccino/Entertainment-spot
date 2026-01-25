package com.appuccino.entertainment_spot

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import androidx.lifecycle.lifecycleScope

/**
 * Displays either Movies or Series in a 3-column grid. Reads from Firestore (movies or series collections),
 * sets the correct adapter, and navigates to "EntertainmentDetailFragment" on item click.
 **/

class EntertainmentListFragment : Fragment(R.layout.fragment_entertainment_list) {
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val ARG_TYPE = "content_type"
        const val TYPE_MOVIE = "movie"
        const val TYPE_SERIES = "series"

        fun newInstance(type: String): EntertainmentListFragment {
            val fragment = EntertainmentListFragment()
            val args = Bundle()
            args.putString(ARG_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val type = arguments?.getString(ARG_TYPE)

        lifecycleScope.launch {
            if (type == TYPE_MOVIE) {
                fetchAndShowMovies()
            } else if (type == TYPE_SERIES) {
                fetchAndShowSeries()
            }
        }
    }

    private suspend fun fetchAndShowMovies() {
        withContext(Dispatchers.IO) {
            db.collection("movies").get()
                .addOnSuccessListener { snapshot ->
                    val movieList = snapshot.toObjects(Movie::class.java)
                    recyclerView.adapter = EntertainmentMovieAdapter(movieList) { movie ->
                        val fragment = EntertainmentDetailFragment.newInstance(movie)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.nav_host, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
        }
    }

    private suspend fun fetchAndShowSeries() {
        withContext(Dispatchers.IO) {
            db.collection("series").get()
                .addOnSuccessListener { snapshot ->
                    val seriesList = snapshot.toObjects(Series::class.java)
                    recyclerView.adapter = EntertainmentSeriesAdapter(seriesList) { series ->
                        val fragment = EntertainmentDetailFragment.newInstance(series)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.nav_host, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
        }
    }
}