package com.ayush.inoxassig.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ayush.inoxassig.R
import com.ayush.inoxassig.data.model.FoodItem
import com.ayush.inoxassig.databinding.ItemFoodRepeatBinding

class RepeatFoodAdapter(
    private val onUpdateQty: (String, Int) -> Unit
) : ListAdapter<FoodItem, RepeatFoodAdapter.RepeatViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatViewHolder {
        val binding = ItemFoodRepeatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepeatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RepeatViewHolder(private val binding: ItemFoodRepeatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodItem) {
            val context = binding.root.context

            // 1. Basic Data Binding
            binding.tvRepeatName.text = item.itemName
            binding.price.text = "₹${item.itemRate.toInt()}"

            // 2. Veg/Non-Veg Icon
            binding.ivTypeIcon.setImageResource(
                if (item.foodType.equals("Veg", true)) R.drawable.ic_veg else R.drawable.ic_non_veg
            )

            // BASE64 IMAGE LOADING WITH COIL
            val base64Image = item.itemImageURL
            if (base64Image.isNotEmpty()) {
                try {
                    val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
                    binding.ivRepeatFood.load(imageBytes) {
                        crossfade(true)
                        placeholder(R.drawable.popcorn)
                        error(R.drawable.popcorn)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.ivRepeatFood.setImageResource(R.drawable.popcorn)
                }
            } else {
                binding.ivRepeatFood.setImageResource(R.drawable.popcorn)
            }

            binding.ivTypeIcon.setImageResource(
                if (item.foodType.equals("Veg", ignoreCase = true)) R.drawable.ic_veg else R.drawable.ic_non_veg
            )

            // 3. Logic: Toggle between ADD button and Counter
            if (item.quantity > 0) {
                binding.btnAdd.visibility = View.GONE
                binding.llCounter.visibility = View.VISIBLE
                binding.tvCount.text = item.quantity.toString()
            } else {
                binding.btnAdd.visibility = View.VISIBLE
                binding.llCounter.visibility = View.GONE
            }

            // 4. Click Listeners (Passing data back to the same ViewModel function)
            binding.btnAdd.setOnClickListener { onUpdateQty(item.itemId, 1) }
            binding.btnPlus.setOnClickListener { onUpdateQty(item.itemId, 1) }
            binding.btnMinus.setOnClickListener { onUpdateQty(item.itemId, -1) }
        }
    }
}