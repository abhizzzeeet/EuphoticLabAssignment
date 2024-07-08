package com.example.euphoticlabsassignment.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.euphoticlabsassignment.R
import com.example.euphoticlabsassignment.models.TabItem


class VerticalTabLayoutAdapter(
    private val tabItems: List<TabItem>,
    private val onTabClick: (Int) -> Unit
) : RecyclerView.Adapter<VerticalTabLayoutAdapter.TabViewHolder>() {

    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tab, parent, false)
        return TabViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(tabItems[position], position, onTabClick, selectedPosition)
    }

    override fun getItemCount(): Int = tabItems.size

    fun selectTab(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    class TabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.title_text_view)
        private val imageView: ImageView = itemView.findViewById(R.id.icon_image_view)
        private val tabIndicator: View = itemView.findViewById(R.id.tab_indicator)

        fun bind(tabItem: TabItem, position: Int, onTabClick: (Int) -> Unit, selectedPosition: Int) {
            textView.text = tabItem.title
            imageView.setImageResource(tabItem.iconResId)
            itemView.setOnClickListener { onTabClick(position) }
            itemView.isSelected = position == selectedPosition

            val isSelected = position == selectedPosition
            itemView.isSelected = isSelected

            val context = itemView.context
            val textColor = if (isSelected) R.color.colorOrange else R.color.colorText
            val iconColor = if (isSelected) R.color.colorOrange else R.color.colorText

            textView.setTextColor(ContextCompat.getColor(context, textColor))
            imageView.setColorFilter(ContextCompat.getColor(context, iconColor))

//            val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
//            layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT // Adjust height as needed
//            itemView.layoutParams = layoutParams

            tabIndicator.visibility = if (isSelected) View.VISIBLE else View.GONE
        }
    }
}