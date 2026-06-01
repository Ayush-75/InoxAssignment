package com.ayush.inoxassig.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ayush.inoxassig.R
import com.ayush.inoxassig.data.model.FoodItem
import com.ayush.inoxassig.databinding.ItemFoodMainBinding

class FoodAdapter(
    private val onUpdateQty: (String, Int) -> Unit
) : ListAdapter<FoodItem, FoodAdapter.FoodViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodViewHolder(private val binding: ItemFoodMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodItem) {
            binding.tvFoodName.text = item.itemName
            binding.tvPrice.text = "₹${item.itemRate.toInt()}"
//            binding.tvDetails.text = item.description

            // BASE64 IMAGE LOADING WITH COIL
            val base64Image = item.itemImageURL
            if (base64Image.isNotEmpty()) {
                try {
                    // Convert the text string back into raw image bytes
                    val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)

                    // Hand the raw bytes directly to Coil
                    binding.ivFood.load(imageBytes) {
                        crossfade(true)
                        placeholder(R.drawable.popcorn)
                        error(R.drawable.popcorn)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.ivFood.setImageResource(R.drawable.popcorn)
                }
            } else {
                binding.ivFood.setImageResource(R.drawable.popcorn)
            }

            // Veg/Non-Veg icon status
            binding.ivFoodType.setImageResource(
                if (item.foodType.equals("Veg", ignoreCase = true)) R.drawable.ic_veg else R.drawable.ic_non_veg
            )

            // Quantity Control Logic
            if (item.quantity > 0) {
                binding.btnAdd.visibility = View.GONE
                binding.layoutCounter.visibility = View.VISIBLE
                binding.tvCount.text = item.quantity.toString()
            } else {
                binding.btnAdd.visibility = View.VISIBLE
                binding.layoutCounter.visibility = View.GONE
            }

            // Click Listeners
            binding.btnAdd.setOnClickListener { onUpdateQty(item.itemId, 1) }
            binding.btnPlus.setOnClickListener { onUpdateQty(item.itemId, 1) }
            binding.btnMinus.setOnClickListener { onUpdateQty(item.itemId, -1) }
        }
    }
}

class FoodDiffCallback : DiffUtil.ItemCallback<FoodItem>() {
    override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem) = oldItem.itemId == newItem.itemId
    override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem) = oldItem == newItem
}