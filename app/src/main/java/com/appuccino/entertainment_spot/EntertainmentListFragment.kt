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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // cards displayed with 2 colunms

        val sampleItems = listOf(
            EntertainmentItem("Atlas", R.drawable.atlas, 5.6f),
            EntertainmentItem("Godzila x Kong: The New Empire", R.drawable.godzila, 6.0f),
            EntertainmentItem("Godzila x Kong: The New Empire", R.drawable.bad_boys, 6.5f)
        )

        adapter = EntertainmentAdapter(sampleItems) { selectedItem ->
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