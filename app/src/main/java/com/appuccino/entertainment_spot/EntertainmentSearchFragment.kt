package com.appuccino.entertainment_spot

import androidx.fragment.app.Fragment

/**
 * EntertainmentSearchFragment
 *
 * Represents the **UI layer** for search functionality.
 *
 * This Fragment is responsible for:
 *  - Displaying the search input field (text + voice)
 *  - Handling user interactions (typing, microphone button, category toggles)
 *  - Triggering search actions
 *  - Rendering search results
 *  - Navigating to detail screens when a result is selected
 */

class EntertainmentSearchFragment: Fragment(R.layout.fragment_entertainment_search) {
    companion object {
        fun newInstance() = EntertainmentSearchFragment()
    }
}