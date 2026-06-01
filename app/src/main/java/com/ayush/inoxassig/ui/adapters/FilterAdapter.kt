package com.ayush.inoxassig.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayush.inoxassig.R
import com.ayush.inoxassig.data.model.FilterItem
import com.ayush.inoxassig.databinding.ItemFilterNormalBinding
import com.ayush.inoxassig.databinding.ItemFilterSwitchBinding

class FilterAdapter(
    private val onFilterClick: (String) -> Unit
) : ListAdapter<FilterItem, RecyclerView.ViewHolder>(FilterDiffCallback()) {

    override fun getItemViewType(position: Int): Int = getItem(position).type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == FilterItem.SWITCH) {
            SwitchViewHolder(ItemFilterSwitchBinding.inflate(inflater, parent, false))
        } else {
            NormalViewHolder(ItemFilterNormalBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is SwitchViewHolder -> holder.bind(item)
            is NormalViewHolder -> holder.bind(item)
        }
    }

    inner class SwitchViewHolder(private val binding: ItemFilterSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterItem) {
            val context = binding.root.context
            binding.tvName.text = item.name

            // This handles the smooth thumb slide
            binding.switchFilter.isChecked = item.selected

            if (item.selected) {
                // 1. Uniform Yellow background for the pill
                binding.container.setBackgroundResource(R.drawable.bg_filter_selected)
                binding.tvName.setTextColor(context.getColor(R.color.text_primary))

                // 2. Identity color ONLY for the switch track
                val identityColor = if (item.name.equals("Veg", ignoreCase = true)) {
                    context.getColor(R.color.veg_active_stroke) // Green
                } else if (item.name.equals("Non Veg", ignoreCase = true)) {
                    context.getColor(R.color.nonveg_active_stroke) // Red/Pink
                } else {
                    context.getColor(R.color.filter_inactive_stroke)
                }

                binding.switchFilter.trackTintList =
                    android.content.res.ColorStateList.valueOf(identityColor)
            } else {
                // Unselected state
                binding.container.setBackgroundResource(R.drawable.bg_filter_unselected)
                binding.tvName.setTextColor(context.getColor(R.color.filter_inactive_text))

                // Neutral grey track
                binding.switchFilter.trackTintList = android.content.res.ColorStateList.valueOf(
                    context.getColor(R.color.filter_inactive_stroke)
                )
            }

            binding.root.setOnClickListener { onFilterClick(item.name) }
            binding.switchFilter.isClickable = false
        }
    }

    inner class NormalViewHolder(private val binding: ItemFilterNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterItem) {
            val context = binding.root.context
            binding.tvName.text = item.name
            item.icon?.let { binding.imgIcon.setImageResource(it) }

            if (item.selected) {
                // Use the same Yellow selection pill as the switches
                binding.container.setBackgroundResource(R.drawable.bg_filter_selected)
                binding.tvName.setTextColor(context.getColor(R.color.text_primary))
            } else {
                binding.container.setBackgroundResource(R.drawable.bg_filter_unselected)
                binding.tvName.setTextColor(context.getColor(R.color.filter_inactive_text))
            }

            binding.root.setOnClickListener { onFilterClick(item.name) }
        }
    }
}
// DiffUtil to handle smooth animations
class FilterDiffCallback : DiffUtil.ItemCallback<FilterItem>() {
    override fun areItemsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean = oldItem == newItem
}