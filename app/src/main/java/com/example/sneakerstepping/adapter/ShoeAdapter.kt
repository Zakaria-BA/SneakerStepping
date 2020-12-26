package com.example.sneakerstepping.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sneakerstepping.R
import com.example.sneakerstepping.models.Shoe

class ShoeAdapter(private val shoes: List<Shoe>) : RecyclerView.Adapter<ShoeAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(shoe: Shoe) {
            // binding happens here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shoes[position])
    }

    override fun getItemCount(): Int {
        return shoes.size
    }
}