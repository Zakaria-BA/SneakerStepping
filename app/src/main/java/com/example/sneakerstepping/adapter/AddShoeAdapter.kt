package com.example.sneakerstepping.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sneakerstepping.R
import com.example.sneakerstepping.models.Shoe
import kotlinx.android.synthetic.main.add_shoe_item.view.*

class AddShoeAdapter(private val addShoes: List<Shoe>) :
    RecyclerView.Adapter<AddShoeAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(shoe: Shoe) {
            Glide.with(context).load(shoe.shoeImage).into(itemView.ivAddShoePicture)
            itemView.tvAddShoeName.text = shoe.shoeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.add_shoe_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addShoes[position])
    }

    override fun getItemCount(): Int {
        return addShoes.size
    }
}