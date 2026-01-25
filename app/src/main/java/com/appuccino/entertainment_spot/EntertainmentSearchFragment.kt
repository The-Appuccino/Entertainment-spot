package com.appuccino.entertainment_spot

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * EntertainmentSearchFragment
 *
 * What this file is responsible for
 * - Owns the search screen UI:
 *    - reads query text input
 *    - triggers a search action (keyboard submit / text change / voice result)
 *    - shows results in the RecyclerView using EntertainmentSearchAdapter
 *
 * What it should NOT do (and your current structure avoids)
 * - Build HTTP requests
 * - Know Meilisearch endpoints
 * - Parse Meilisearch JSON directly
 *
 * Recommended responsibilities in this Fragment
 * - UI-only concerns:
 *    - wiring the adapter
 *    - reading from TextInputEditText
 *    - calling the UseCase
 *    - handling loading state / empty state / error toast (optional)
 *    - navigating on click
 *
 * Current architecture flow (high level)
 * - User types -> Fragment triggers useCase -> repository -> retrofit -> Meilisearch
 * - Results come back -> Fragment updates adapter -> user clicks item -> navigate to details
 */

class EntertainmentSearchFragment : Fragment(R.layout.fragment_entertainment_search) {

    private val useCase = EntertainmentSearchUseCase()

    private lateinit var rv: RecyclerView
    private lateinit var adapter: EntertainmentSearchAdapter
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val input = view.findViewById<TextInputEditText>(R.id.textInputEditText)
        rv = view.findViewById(R.id.searchResultListRV)

        rv.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter = EntertainmentSearchAdapter(emptyList()) { row ->
            viewLifecycleOwner.lifecycleScope.launch {
                val full = useCase.fetchFullItem(row)
                when (full) {
                    is Movie -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.nav_host, EntertainmentDetailFragment.newInstance(full))
                            .addToBackStack(null)
                            .commit()
                    }
                    is Series -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.nav_host, EntertainmentDetailFragment.newInstance(full))
                            .addToBackStack(null)
                            .commit()
                    }
                    else -> Toast.makeText(requireContext(), "Item not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        rv.adapter = adapter

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString().orEmpty()

                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(250)
                    try {
                        val results = useCase.searchByTitle(q)
                        adapter.submitList(results)
                    } catch (e: Exception) {
                        Log.e("Search", "Meili search failed", e)
                        adapter.submitList(emptyList())
                    }
                }
            }
        })
    }
}