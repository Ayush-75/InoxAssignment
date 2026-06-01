package com.ayush.inoxassig.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.inoxassig.R
import com.ayush.inoxassig.data.model.FilterItem
import com.ayush.inoxassig.data.model.FoodItem
import com.ayush.inoxassig.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val repository: FoodRepository) : ViewModel() {

    private val _filters = MutableStateFlow(
        listOf(
            FilterItem("Veg", FilterItem.SWITCH),
            FilterItem("Non Veg", FilterItem.SWITCH),
            FilterItem("Popcorn", FilterItem.NORMAL, R.drawable.popcorn),
            FilterItem("Snacks", FilterItem.NORMAL, R.drawable.snack),
            FilterItem("Combos", FilterItem.NORMAL, R.drawable.combo),
            FilterItem("Hot Beverages", FilterItem.NORMAL, R.drawable.hot_beverages),
            FilterItem("Cold Beverages", FilterItem.NORMAL, R.drawable.cold_beverages)
        )
    )
    val filters = _filters.asStateFlow()

    private val _allFoodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    // 1. This Flow filters the master list to show only items with quantity > 0
    val cartItems = _allFoodItems.map { list ->
        list.filter { it.quantity > 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. This Flow calculates the total count and price for your bottom bar
    val cartStats = cartItems.map { items ->
        val count = items.sumOf { it.quantity }
        val total = items.sumOf { it.quantity * it.itemRate }
        Pair(count, total)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0, 0.0))

    init {
        loadFoodData()
    }

    private fun loadFoodData() {
        viewModelScope.launch {
            _allFoodItems.value = repository.getFoodItems()
        }
    }

    // Filtered Main List: Directly reads active states out of the master filters list
    val filteredMainList = combine(_allFoodItems, _filters) { items, currentFilters ->
        val isVegActive = currentFilters.find { it.name == "Veg" }?.selected ?: false
        val isNonVegActive = currentFilters.find { it.name == "Non Veg" }?.selected ?: false

        // Finds any active normal chip category (like "Popcorn")
        val activeCategory = currentFilters.find { it.type == FilterItem.NORMAL && it.selected }?.name

        items.filter { item ->
            val matchesType = when {
                isVegActive -> item.foodType.equals("Veg", true)
                isNonVegActive -> item.foodType.equals("Non Veg", true)
                else -> true
            }
            val matchesCat = activeCategory == null || item.itemCategory.equals(activeCategory, true)

            matchesType && matchesCat
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Repeat List: Items marked as isRepeat
    val repeatList = _allFoodItems.map { list ->
        list.filter { it.isRepeat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateQuantity(itemId: String, delta: Int) {
        val currentList = _allFoodItems.value.map { item ->
            if (item.itemId == itemId) {
                val newQty = (item.quantity + delta).coerceAtLeast(0)
                item.copy(quantity = newQty)
            } else {
                item
            }
        }
        _allFoodItems.value = currentList
    }

    fun clearCart() {
        viewModelScope.launch {
            val resetList = _allFoodItems.value.map { it.copy(quantity = 0) }
            _allFoodItems.value = resetList
        }
    }

    fun onFilterClicked(clickedName: String) {
        val currentList = _filters.value.map { item ->
            when {
                item.name == clickedName -> {
                    // Toggle the clicked item
                    item.copy(selected = !item.selected)
                }
                // Mutual exclusion for Veg/Non Veg switches
                (clickedName == "Veg" && item.name == "Non Veg") ||
                        (clickedName == "Non Veg" && item.name == "Veg") -> {
                    item.copy(selected = false)
                }
                // Single-select for NORMAL chips (e.g., Popcorn)
                item.type == FilterItem.NORMAL &&
                        _filters.value.find { it.name == clickedName }?.type == FilterItem.NORMAL -> {
                    item.copy(selected = false)
                }
                else -> item
            }
        }
        _filters.value = currentList
    }
}