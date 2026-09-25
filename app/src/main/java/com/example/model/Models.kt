package com.example.model

enum class Category(val displayName: String, val iconName: String) {
    ALL("All Dishes", "Restaurant"),
    CHEF_SPECIALS("Chef's Specials", "Star"),
    STARTERS("Starters", "Tapas"),
    MAINS("Artisan Mains", "DinnerDining"),
    PASTA_PIZZA("Pasta & Risotto", "RamenDining"),
    DESSERTS("Desserts", "Cake"),
    DRINKS("Wines & Drinks", "LocalBar")
}

enum class DietaryTag(val label: String) {
    VEGETARIAN("Vegetarian"),
    VEGAN("Vegan"),
    GLUTEN_FREE("Gluten-Free"),
    SPICY("Spicy"),
    CHEF_PICK("Chef's Choice")
}

data class MenuItem(
    val id: String,
    val name: String,
    val category: Category,
    val description: String,
    val price: Double,
    val calories: Int,
    val prepTimeMin: Int,
    val rating: Double,
    val reviewCount: Int,
    val imageResId: Int?,
    val dietaryTags: List<DietaryTag> = emptyList(),
    val allergens: List<String> = emptyList(),
    val isPopular: Boolean = false,
    val isChefSpecial: Boolean = false,
    val ingredients: List<String> = emptyList(),
    val availableAddOns: List<Pair<String, Double>> = emptyList()
)

data class CartItem(
    val id: Long = 0,
    val menuItemId: String,
    val name: String,
    val unitPrice: Double,
    val quantity: Int,
    val portionSize: String = "Regular",
    val spiceLevel: String = "Standard",
    val selectedAddOns: List<String> = emptyList(),
    val addOnsCost: Double = 0.0,
    val specialInstructions: String = "",
    val imageResId: Int? = null
) {
    val totalPrice: Double
        get() = (unitPrice + addOnsCost) * quantity
}

enum class DiningMode(val label: String) {
    DINE_IN("Dine-In Table"),
    TAKEAWAY("Takeaway / Curbside"),
    DELIVERY("Fast Delivery")
}

enum class OrderStatus(val label: String, val stepIndex: Int) {
    PLACED("Order Placed", 0),
    PREPARING("Kitchen Preparing", 1),
    COOKED("Plated & Packed", 2),
    OUT_OR_READY("Ready for Serving / Pickup", 3),
    COMPLETED("Completed", 4)
}

data class Order(
    val id: String,
    val diningMode: DiningMode,
    val destinationInfo: String,
    val itemsSummary: String,
    val itemCount: Int,
    val subtotal: Double,
    val discount: Double,
    val tax: Double,
    val tip: Double,
    val totalAmount: Double,
    val status: OrderStatus,
    val timestamp: Long
)

data class Reservation(
    val id: Long = 0,
    val guestName: String,
    val guestPhone: String,
    val date: String,
    val timeSlot: String,
    val partySize: Int,
    val seatingArea: String,
    val occasion: String,
    val specialRequests: String,
    val status: String = "Confirmed",
    val createdAt: Long = System.currentTimeMillis()
)
