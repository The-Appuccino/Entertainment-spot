package com.appuccino.entertainment_spot

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class EntertainmentDetailFragment: Fragment(R.layout.fragment_entertainment_detail) {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_IMAGE = "image"
        private const val ARG_RATING = "rating"

        fun newInstance(item: EntertainmentItem): EntertainmentDetailFragment {
            val args = Bundle().apply {
                putString(ARG_TITLE, item.title)
                putInt(ARG_IMAGE, item.imageResId)
                putFloat(ARG_RATING, item.imdbRating)
            }
            return EntertainmentDetailFragment().apply {
                arguments = args
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARG_TITLE)
        val imageRes = arguments?.getInt(ARG_IMAGE)
        val rating = arguments?.getFloat(ARG_RATING)

        view.findViewById<TextView>(R.id.titleTextView).text = title
        view.findViewById<ImageView>(R.id.posterImageView).setImageResource(imageRes ?: 0)
        view.findViewById<ImageView>(R.id.backgroundImageView).setImageResource(imageRes ?: 0)
        view.findViewById<TextView>(R.id.ratingTextView).text = rating.toString()
    }
}