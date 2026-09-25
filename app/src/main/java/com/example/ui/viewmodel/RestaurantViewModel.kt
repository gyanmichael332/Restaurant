package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MenuDataSource
import com.example.data.local.AppDatabase
import com.example.data.repository.RestaurantRepository
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.DietaryTag
import com.example.model.DiningMode
import com.example.model.MenuItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Reservation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RestaurantRepository(AppDatabase.getInstance(application))

    // Navigation & Tabs
    private val _currentTab = MutableStateFlow(0) // 0: Home, 1: Menu, 2: Reservations, 3: Cart, 4: Orders
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    // Menu Filters & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    private val _selectedDietaryTag = MutableStateFlow<DietaryTag?>(null)
    val selectedDietaryTag: StateFlow<DietaryTag?> = _selectedDietaryTag.asStateFlow()

    // Filtered menu items
    val filteredMenuItems: StateFlow<List<MenuItem>> = combine(
        _searchQuery,
        _selectedCategory,
        _selectedDietaryTag
    ) { query, category, dietary ->
        MenuDataSource.items.filter { item ->
            val matchesQuery = query.isBlank() ||
                item.name.contains(query, ignoreCase = true) ||
                item.description.contains(query, ignoreCase = true) ||
                item.ingredients.any { it.contains(query, ignoreCase = true) }

            val matchesCategory = when (category) {
                Category.ALL -> true
                Category.CHEF_SPECIALS -> item.isChefSpecial
                else -> item.category == category
            }

            val matchesDietary = dietary == null || item.dietaryTags.contains(dietary)

            matchesQuery && matchesCategory && matchesDietary
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MenuDataSource.items)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun toggleDietaryTag(tag: DietaryTag) {
        _selectedDietaryTag.value = if (_selectedDietaryTag.value == tag) null else tag
    }

    // Dish Detail & Customization
    private val _selectedDish = MutableStateFlow<MenuItem?>(null)
    val selectedDish: StateFlow<MenuItem?> = _selectedDish.asStateFlow()

    fun openDishDetails(dish: MenuItem) {
        _selectedDish.value = dish
    }

    fun closeDishDetails() {
        _selectedDish.value = null
    }

    // Cart State
    val cartItems: StateFlow<List<CartItem>> = repository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<Set<String>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val orders: StateFlow<List<Order>> = repository.getOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reservations: StateFlow<List<Reservation>> = repository.getReservations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart checkout controls
    private val _diningMode = MutableStateFlow(DiningMode.DINE_IN)
    val diningMode: StateFlow<DiningMode> = _diningMode.asStateFlow()

    private val _destinationInfo = MutableStateFlow("Table 7 (Main Dining Room)")
    val destinationInfo: StateFlow<String> = _destinationInfo.asStateFlow()

    private val _couponCode = MutableStateFlow("")
    val couponCode: StateFlow<String> = _couponCode.asStateFlow()

    private val _discountPercent = MutableStateFlow(0.0)
    val discountPercent: StateFlow<Double> = _discountPercent.asStateFlow()

    private val _couponMessage = MutableStateFlow<String?>(null)
    val couponMessage: StateFlow<String?> = _couponMessage.asStateFlow()

    private val _selectedTipPercent = MutableStateFlow(15)
    val selectedTipPercent: StateFlow<Int> = _selectedTipPercent.asStateFlow()

    // Tracking active order in modal
    private val _activeTrackedOrder = MutableStateFlow<Order?>(null)
    val activeTrackedOrder: StateFlow<Order?> = _activeTrackedOrder.asStateFlow()

    // Snack / Notification events
    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    fun setDiningMode(mode: DiningMode) {
        _diningMode.value = mode
        _destinationInfo.value = when (mode) {
            DiningMode.DINE_IN -> "Table 7 (Main Dining Room)"
            DiningMode.TAKEAWAY -> "Curbside Pickup (Parking Bay 3)"
            DiningMode.DELIVERY -> "742 Evergreen Terrace, Apt 4B"
        }
    }

    fun setDestinationInfo(info: String) {
        _destinationInfo.value = info
    }

    fun setCouponCode(code: String) {
        _couponCode.value = code
    }

    fun applyCoupon() {
        val trimmed = _couponCode.value.trim().uppercase()
        when (trimmed) {
            "SAVOR15" -> {
                _discountPercent.value = 0.15
                _couponMessage.value = "15% discount applied!"
                emitMessage("SAVOR15 applied: 15% off!")
            }
            "BISTRO20" -> {
                _discountPercent.value = 0.20
                _couponMessage.value = "20% VIP Chef discount applied!"
                emitMessage("BISTRO20 applied: 20% off!")
            }
            "WELCOME10" -> {
                _discountPercent.value = 0.10
                _couponMessage.value = "10% Welcome gift applied!"
                emitMessage("WELCOME10 applied: 10% off!")
            }
            else -> {
                _discountPercent.value = 0.0
                _couponMessage.value = "Invalid code. Try SAVOR15 or BISTRO20"
                emitMessage("Invalid promo code")
            }
        }
    }

    fun setTipPercent(percent: Int) {
        _selectedTipPercent.value = percent
    }

    fun addToCart(
        menuItem: MenuItem,
        quantity: Int,
        portionSize: String,
        spiceLevel: String,
        selectedAddOns: List<Pair<String, Double>>,
        instructions: String
    ) {
        viewModelScope.launch {
            repository.addToCart(
                menuItem = menuItem,
                quantity = quantity,
                portionSize = portionSize,
                spiceLevel = spiceLevel,
                selectedAddOns = selectedAddOns,
                instructions = instructions
            )
            emitMessage("Added ${menuItem.name} to order")
            closeDishDetails()
        }
    }

    fun quickAddToCart(menuItem: MenuItem) {
        viewModelScope.launch {
            repository.addToCart(
                menuItem = menuItem,
                quantity = 1,
                portionSize = "Regular",
                spiceLevel = "Standard",
                selectedAddOns = emptyList(),
                instructions = ""
            )
            emitMessage("Added ${menuItem.name} to order")
        }
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, newQuantity)
        }
    }

    fun removeCartItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeCartItem(cartItemId)
            emitMessage("Item removed from cart")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleFavorite(menuItemId: String) {
        viewModelScope.launch {
            val isFav = favorites.value.contains(menuItemId)
            repository.toggleFavorite(menuItemId, isFav)
            emitMessage(if (isFav) "Removed from favorites" else "Saved to favorites")
        }
    }

    fun placeOrder() {
        val items = cartItems.value
        if (items.isEmpty()) return

        val subtotal = items.sumOf { it.totalPrice }
        val discount = subtotal * _discountPercent.value
        val discountedSubtotal = (subtotal - discount).coerceAtLeast(0.0)
        val tax = discountedSubtotal * 0.0825
        val tip = discountedSubtotal * (_selectedTipPercent.value / 100.0)
        val total = discountedSubtotal + tax + tip

        viewModelScope.launch {
            val orderId = repository.placeOrder(
                diningMode = _diningMode.value,
                destinationInfo = _destinationInfo.value,
                cartItems = items,
                subtotal = subtotal,
                discount = discount,
                tax = tax,
                tip = tip,
                total = total
            )

            val newOrder = Order(
                id = orderId,
                diningMode = _diningMode.value,
                destinationInfo = _destinationInfo.value,
                itemsSummary = items.joinToString(", ") { "${it.quantity}x ${it.name}" },
                itemCount = items.sumOf { it.quantity },
                subtotal = subtotal,
                discount = discount,
                tax = tax,
                tip = tip,
                totalAmount = total,
                status = OrderStatus.PLACED,
                timestamp = System.currentTimeMillis()
            )

            _activeTrackedOrder.value = newOrder
            emitMessage("Order $orderId placed successfully!")

            // Start simulated kitchen live progression
            startOrderProgression(orderId)
        }
    }

    private fun startOrderProgression(orderId: String) {
        viewModelScope.launch {
            delay(5000)
            repository.updateOrderStatus(orderId, OrderStatus.PREPARING)
            updateActiveTrackedStatus(orderId, OrderStatus.PREPARING)

            delay(8000)
            repository.updateOrderStatus(orderId, OrderStatus.COOKED)
            updateActiveTrackedStatus(orderId, OrderStatus.COOKED)

            delay(9000)
            repository.updateOrderStatus(orderId, OrderStatus.OUT_OR_READY)
            updateActiveTrackedStatus(orderId, OrderStatus.OUT_OR_READY)
        }
    }

    private fun updateActiveTrackedStatus(orderId: String, status: OrderStatus) {
        val current = _activeTrackedOrder.value
        if (current != null && current.id == orderId) {
            _activeTrackedOrder.value = current.copy(status = status)
        }
    }

    fun openOrderTracker(order: Order) {
        _activeTrackedOrder.value = order
    }

    fun closeOrderTracker() {
        _activeTrackedOrder.value = null
    }

    fun makeReservation(
        guestName: String,
        phone: String,
        date: String,
        timeSlot: String,
        partySize: Int,
        seatingArea: String,
        occasion: String,
        specialRequests: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val res = Reservation(
                guestName = guestName,
                guestPhone = phone,
                date = date,
                timeSlot = timeSlot,
                partySize = partySize,
                seatingArea = seatingArea,
                occasion = occasion,
                specialRequests = specialRequests
            )
            repository.createReservation(res)
            emitMessage("Table reserved for $partySize guests on $date at $timeSlot")
            onSuccess()
        }
    }

    fun cancelReservation(id: Long) {
        viewModelScope.launch {
            repository.cancelReservation(id)
            emitMessage("Reservation cancelled")
        }
    }

    private fun emitMessage(msg: String) {
        viewModelScope.launch {
            _userMessage.emit(msg)
        }
    }
}
