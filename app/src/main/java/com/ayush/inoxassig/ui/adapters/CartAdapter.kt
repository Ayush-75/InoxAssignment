package com.ayush.inoxassig.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ayush.inoxassig.R
import com.ayush.inoxassig.data.model.FoodItem
import com.ayush.inoxassig.databinding.ItemCartBinding

class CartAdapter(
    private val onUpdateQty: (String, Int) -> Unit
) : ListAdapter<FoodItem, CartAdapter.CartViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodItem) {
            binding.tvCartFoodName.text = item.itemName
            // Display price with two decimal points as per image
            binding.tvCartItemPrice.text = "₹${String.format("%.2f", item.itemRate * item.quantity)}"
            binding.tvCartQty.text = item.quantity.toString()

            // Load Image using Coil
            val base64Image = item.itemImageURL
            if (base64Image.isNotEmpty()) {
                try {
                    // Convert the text string back into raw image bytes
                    val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)

                    // Hand the raw bytes directly to Coil
                    binding.ivCartFood.load(imageBytes) {
                        crossfade(true)
                        placeholder(R.drawable.popcorn)
                        error(R.drawable.popcorn)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.ivCartFood.setImageResource(R.drawable.popcorn)
                }
            } else {
                binding.ivCartFood.setImageResource(R.drawable.popcorn)
            }

            binding.btnCartPlus.setOnClickListener { onUpdateQty(item.itemId, 1) }
            binding.btnCartMinus.setOnClickListener { onUpdateQty(item.itemId, -1) }
        }
    }
}