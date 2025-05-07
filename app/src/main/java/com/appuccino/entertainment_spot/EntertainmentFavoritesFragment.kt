package com.appuccino.entertainment_spot

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


class EntertainmentFavoritesFragment : Fragment() {

    private val entertainmentLists = mutableListOf<EntertainmentList>()
    private lateinit var adapter: EntertainmentListAdapter


    companion object {
        fun newInstance() : EntertainmentFavoritesFragment {
            return EntertainmentFavoritesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabCreateList = view.findViewById<FloatingActionButton>(R.id.fabCreateList) //floating action button
        val recyclerView = view.findViewById<RecyclerView>(R.id.favoritesListRV)


        fabCreateList.setOnClickListener {
            showCreateListDialog() //calls the method to show dialog popup and enter list name
        }

        adapter = EntertainmentListAdapter(entertainmentLists) { selectedList ->
            // Navigate to EntertainmentListDetailFragment that shows all favorited movies
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            val detailFragment = EntertainmentListDetailFragment.newInstance(selectedList.name, ArrayList(selectedList.items))

            transaction.replace(R.id.nav_host, detailFragment) //
            transaction.addToBackStack(null)
            transaction.commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        }

    private fun showCreateListDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "Enter list name"
            inputType = InputType.TYPE_CLASS_TEXT
            setPadding(32, 24, 32, 24)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("New Movie List")
            .setView(editText)
            .setPositiveButton("Create") { dialog, _ ->
                val listName = editText.text.toString().trim()
                if (listName.isNotEmpty()) {
                    createNewList(listName)
                } else {
                    Toast.makeText(requireContext(), "List name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createNewList(name: String) {
        val newList = EntertainmentList(
//            id = UUID.randomUUID().toString(), //sets an id for the created list
            name = name,
        )

        //EXAMPLE
        // Manually create and add Movie and Series objects for testing
        val movie1 = Movie("Atlas", R.drawable.atlas, 5.6f)
        val movie2 = Movie("Godzila x Kong: The New Empire", R.drawable.godzila, 6.0f)
        val series1 = Series("Attack On Titan", R.drawable.aot, 9.1f)
        val series2 = Series("Silo", R.drawable.silo, 8.1f)
        newList.items.add(movie1)
        newList.items.add(movie2)
        newList.items.add(series1)
        newList.items.add(series2)

        adapter.addList(newList)
    }
}