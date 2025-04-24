package com.appuccino.entertainment_spot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EntertainmentListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntertainmentListFragment : Fragment(R.layout.fragment_entertainment_list) {
    private lateinit var recyclerView: RecyclerView
    private  lateinit var adapter: EntertainmentAdapter

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
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // cards displayed with 2 colunms

//        val sampleItems = listOf(
//            EntertainmentItem("Atlas", R.drawable.atlas, 5.6f),
//            EntertainmentItem("Godzila x Kong: The New Empire", R.drawable.godzila, 6.0f),
//            EntertainmentItem("Bad Boys: Ride or Die", R.drawable.bad_boys, 6.5f),
//            EntertainmentItem("Sinners", R.drawable.sinners, 8.2f),
//            EntertainmentItem("Thor: Ragnarok ", R.drawable.thor, 7.9f),
//            EntertainmentItem("Man Of Steel", R.drawable.man_of_steel, 7.1f),
//            EntertainmentItem("The Dark Knight", R.drawable.dark_knight, 9.0f)
//        )

        val sampleMovieItems = listOf(
            Movie("Atlas", R.drawable.atlas, 5.6f),
            Movie("Godzila x Kong: The New Empire", R.drawable.godzila, 6.0f),
            Movie("Bad Boys: Ride or Die", R.drawable.bad_boys, 6.5f),
            Movie("Sinners", R.drawable.sinners, 8.2f),
            Movie("Thor: Ragnarok ", R.drawable.thor, 7.9f),
            Movie("Man Of Steel", R.drawable.man_of_steel, 7.1f),
            Movie("The Dark Knight", R.drawable.dark_knight, 9.0f)
        )

        val sampleSeriesItems = listOf(
            Series("Severance", R.drawable.severance, 8.7f),
            Series("Attack On Titan", R.drawable.aot, 9.1f),
            Series("Silo", R.drawable.silo, 8.1f),
            Series("Arcane", R.drawable.arcane, 9.0f),
            Series("Daredevil: Born Again", R.drawable.daredevil, 8.3f),
            Series("Ozark", R.drawable.ozark, 8.5f),
            Series("Jujutsu Kaisen", R.drawable.juju, 8.5f)
        )

        val type = arguments?.getString(ARG_TYPE)
        val items = when (type) {
            TYPE_MOVIE -> sampleMovieItems
            TYPE_SERIES -> sampleSeriesItems
            else -> emptyList()
        }

        adapter = EntertainmentAdapter(items) { selectedItem ->
            //Navigate to detail screen
            val fragment = EntertainmentDetailFragment.newInstance(selectedItem)
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

    }
}