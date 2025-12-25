package com.appuccino.entertainment_spot

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class EntertainmentFavoriteListFragment: Fragment(R.layout.fragment_entertainment_favorite_list) {
    companion object {

        private const val TAG = "EntertainmentFavoriteList"

        private const val ARG_TYPE = "content_type"

        private const val TYPE_MOVIE = "movie"
        private const val TYPE_SERIES = "series"

        fun newInstance() = EntertainmentFavoriteListFragment()
        
    }
}