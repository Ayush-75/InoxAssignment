package com.ayush.inoxassig.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayush.inoxassig.R
import com.ayush.inoxassig.databinding.ActivityMainBinding
import com.ayush.inoxassig.ui.adapters.CartAdapter
import com.ayush.inoxassig.ui.adapters.FilterAdapter
import com.ayush.inoxassig.ui.adapters.FoodAdapter
import com.ayush.inoxassig.ui.adapters.RepeatFoodAdapter
import com.ayush.inoxassig.utils.collectIn
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val viewModel: FoodViewModel by viewModels()
    private lateinit var filterAdapter: FilterAdapter

    private lateinit var mainAdapter: FoodAdapter
    private lateinit var repeatAdapter: RepeatFoodAdapter
    private lateinit var cartAdapter: CartAdapter

    private lateinit var cartBehavior: BottomSheetBehavior<CardView>

    private var lastCartCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAdapters()
        setupFilters()
        setupCartBehavior()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupAdapters() {
        // 1. Setup Repeat List (Horizontal)
        repeatAdapter = RepeatFoodAdapter { id, delta ->
            viewModel.updateQuantity(id, delta)
        }
        binding.rvRepeatList.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = repeatAdapter
        }

        // 2. Setup Main List (Vertical)
        mainAdapter = FoodAdapter { id, delta ->
            viewModel.updateQuantity(id, delta)
        }
        binding.rvMainFoodList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }

        cartAdapter = CartAdapter { id, delta ->
            viewModel.updateQuantity(id, delta)
        }
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cartAdapter
        }
    }

    private fun setupFilters() {
        filterAdapter = FilterAdapter { name ->
            viewModel.onFilterClicked(name)
        }

        binding.rvFilters.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    private fun observeViewModel() {
        // Observe Filter Configuration Updates
        viewModel.filters.collectIn(this) { list ->
            filterAdapter.submitList(list)
        }

        // Observe Repeat List Horizontal Updates
        viewModel.repeatList.collectIn(this) { list ->
            repeatAdapter.submitList(list)
        }

        // Observe Filtered Food Items Vertical Updates
        viewModel.filteredMainList.collectIn(this) { list ->
            mainAdapter.submitList(list)
        }

        // Update the Persistent Cart Bar
        viewModel.cartStats.collectIn(this) { (count, total) ->
            updateBottomCartBar(count, total)
        }

        viewModel.cartItems.collectIn(this) { list ->
            val newCount = list.sumOf { it.quantity }

            if (newCount == 0) {
                cartBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            lastCartCount = newCount
            cartAdapter.submitList(list)
        }
    }

    private fun updateBottomCartBar(count: Int, total: Double) {
        if (count == 0) {
            // Empty State: Grey background, "No items"
            binding.cartSummaryLayout.isActivated = false
            binding.viewCartBadge.visibility = View.GONE
            binding.tvItemCount.text = "No items added"
            binding.tvTotalAmount.text = "₹00.00"
        } else {
            // Active State: Yellow background, Item count
            binding.cartSummaryLayout.isActivated = true
            binding.viewCartBadge.visibility = View.VISIBLE
            binding.tvItemCount.text = if (count == 1) "1 item added" else "$count items added"
            binding.tvTotalAmount.text = "₹${String.format("%.2f", total)}"
        }
    }

    private fun setupClickListeners() {
        binding.btnProceed.setOnClickListener {
            if (viewModel.cartItems.value.isNotEmpty()) {
                // 1. Reset the entire state
                viewModel.clearCart()

                // 2. Show confirmation
                Toast.makeText(this, "Order Processed!", Toast.LENGTH_SHORT).show()

                // 3. Optional: Explicitly hide the cart sheet if it was open
                cartBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCartBehavior() {
        cartBehavior = BottomSheetBehavior.from(binding.cartSheetContainer)

        // Set to HIDDEN initially
        cartBehavior.isHideable = true
        cartBehavior.skipCollapsed = true // prevents COLLAPSED state
        cartBehavior.peekHeight = 0
        cartBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        cartBehavior.isFitToContents = true

        cartBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Handle indicator arrow rotations here if needed later
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Handle custom backgrounds/alpha transforms here if needed
            }
        })

        binding.cartSummaryLayout.setOnClickListener {
            cartBehavior.state =
                if (cartBehavior.state == BottomSheetBehavior.STATE_HIDDEN)
                    BottomSheetBehavior.STATE_EXPANDED
                else
                    BottomSheetBehavior.STATE_HIDDEN
        }
    }
}



