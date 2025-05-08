package com.appuccino.entertainment_spot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class EntertainmentCastAdapter(
    private val cast: List<CastMember>
) : RecyclerView.Adapter<EntertainmentCastAdapter.CastViewHolder>() {

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ShapeableImageView = itemView.findViewById(R.id.castImageView)
        val name: TextView = itemView.findViewById(R.id.castName)
        val character: TextView = itemView.findViewById(R.id.playedCharacter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entertainment_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val member = cast[position]
        holder.name.text = member.name
        holder.character.text = member.character

        val imageUrl = "https://image.tmdb.org/t/p/w185${member.profilePath}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            //.placeholder(R.drawable.ic_outline_person_24)
            .into(holder.image)
    }

    override fun getItemCount(): Int = cast.size
}