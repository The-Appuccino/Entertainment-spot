package com.appuccino.entertainment_spot

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntertainmentList( //represents a single list within the EntertainmentFavoritesFragment
//    val id: String, //id to uniquely reference it in Firebase
    val name: String,
    val items: MutableList<EntertainmentItem> = mutableListOf(),
) : Parcelable
