package com.dannyou.artbooktesting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.dannyou.artbooktesting.R
import com.dannyou.artbooktesting.room.Art
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Art>() {

        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var arts: List<Art>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.art_row, parent, false)
        return ArtViewHolder(view)
    }

    override fun getItemCount(): Int = arts.size

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.ivArtView)
        val nameText = holder.itemView.findViewById<TextView>(R.id.tvArtView)
        val artistiText = holder.itemView.findViewById<TextView>(R.id.tvArtistName)
        val yearText = holder.itemView.findViewById<TextView>(R.id.tvYearName)

        val art = arts[position]
        holder.apply {
            nameText.text = "Name: ${art.name}"
            artistiText.text = "Artist Name: ${art.artist}"
            yearText.text = "Year: ${art.year}"
            glide.load(art.imageUrl).into(imageView)
        }
    }

    inner class ArtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}