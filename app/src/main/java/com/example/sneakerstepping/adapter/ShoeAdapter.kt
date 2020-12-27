package com.example.sneakerstepping.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sneakerstepping.R
import com.example.sneakerstepping.models.Shoe
import kotlinx.android.synthetic.main.shoe_card_item.view.*
import java.lang.reflect.Field


class ShoeAdapter(private val shoes: List<Shoe>, private val onClick: (Shoe) -> Unit) : RecyclerView.Adapter<ShoeAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.putOnButton.setOnClickListener {
                onClick(shoes[adapterPosition])
            }
        }

        fun bind(shoe: Shoe) {
            Glide.with(context).load(shoe.shoeImage).into(itemView.ivShoePicture)
            itemView.tvTitle.text = shoe.shoeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.shoe_card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shoes[position])
    }

    override fun getItemCount(): Int {
        return shoes.size
    }
}