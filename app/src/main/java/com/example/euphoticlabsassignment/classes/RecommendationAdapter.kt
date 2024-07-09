package com.example.euphoticlabsassignment.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euphoticlabsassignment.R
import com.example.euphoticlabsassignment.models.Recommendation
import com.squareup.picasso.Picasso


class RecommendationAdapter(private val recommendationList: List<Recommendation>,private val onItemSelected: (Recommendation) -> Unit) : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recommendationList[position])
        holder.itemView.setOnClickListener{
            onItemSelected(recommendationList[position])
        }

    }

    override fun getItemCount(): Int = recommendationList.size
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Recommendation){
            val imageView = itemView.findViewById<ImageView>(R.id.image_recommendation)
            val textView = itemView.findViewById<TextView>(R.id.text_recommendation)

            Picasso.get()
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder) // optional placeholder image
                .into(imageView)

            textView.text = item.dishName
        }
    }
}