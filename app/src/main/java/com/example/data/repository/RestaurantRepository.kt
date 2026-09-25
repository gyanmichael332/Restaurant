package com.example.data.repository

import com.example.data.MenuDataSource
import com.example.data.local.AppDatabase
import com.example.data.local.CartEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ReservationEntity
import com.example.model.CartItem
import com.example.model.DiningMode
import com.example.model.MenuItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Reservation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RestaurantRepository(private val db: AppDatabase) {

    fun getMenuItems(): List<MenuItem> = MenuDataSource.items

    fun getMenuItemById(id: String): MenuItem? = MenuDataSource.getById(id)

    // Cart operations
    fun getCartItems(): Flow<List<CartItem>> {
        return db.cartDao().getAllCartItems().map { entities ->
            entities.map { entity ->
                val addOnsList = if (entity.addOns.isBlank()) emptyList() else entity.addOns.split(" | ")
                CartItem(
                    id = entity.id,
                    menuItemId = entity.menuItemId,
                    name = entity.name,
                    unitPrice = entity.unitPrice,
                    quantity = entity.quantity,
                    portionSize = entity.portionSize,
                    spiceLevel = entity.spiceLevel,
                    selectedAddOns = addOnsList,
                    addOnsCost = entity.addOnsCost,
                    specialInstructions = entity.instructions,
                    imageResId = entity.imageResId
                )
            }
        }
    }

    suspend fun addToCart(
        menuItem: MenuItem,
        quantity: Int,
        portionSize: String,
        spiceLevel: String,
        selectedAddOns: List<Pair<String, Double>>,
        instructions: String
    ) {
        val addOnsText = selectedAddOns.joinToString(" | ") { it.first }
        val addOnsTotal = selectedAddOns.sumOf { it.second }

        val entity = CartEntity(
            menuItemId = menuItem.id,
            name = menuItem.name,
            unitPrice = menuItem.price,
            quantity = quantity,
            portionSize = portionSize,
            spiceLevel = spiceLevel,
            addOns = addOnsText,
            addOnsCost = addOnsTotal,
            instructions = instructions,
            imageResId = menuItem.imageResId
        )
        db.cartDao().insertItem(entity)
    }

    suspend fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            db.cartDao().deleteItemById(cartItemId)
        } else {
            db.cartDao().updateQuantity(cartItemId, newQuantity)
        }
    }

    suspend fun removeCartItem(cartItemId: Long) {
        db.cartDao().deleteItemById(cartItemId)
    }

    suspend fun clearCart() {
        db.cartDao().clearCart()
    }

    // Favorites operations
    fun getFavorites(): Flow<Set<String>> {
        return db.favoriteDao().getAllFavorites().map { list ->
            list.map { it.menuItemId }.toSet()
        }
    }

    suspend fun toggleFavorite(menuItemId: String, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            db.favoriteDao().removeFavorite(menuItemId)
        } else {
            db.favoriteDao().addFavorite(FavoriteEntity(menuItemId = menuItemId))
        }
    }

    // Orders operations
    fun getOrders(): Flow<List<Order>> {
        return db.orderDao().getAllOrders().map { list ->
            list.map { entity ->
                val mode = try { DiningMode.valueOf(entity.diningMode) } catch (e: Exception) { DiningMode.DINE_IN }
                val status = try { OrderStatus.valueOf(entity.status) } catch (e: Exception) { OrderStatus.PLACED }
                Order(
                    id = entity.id,
                    diningMode = mode,
                    destinationInfo = entity.destinationInfo,
                    itemsSummary = entity.itemsSummary,
                    itemCount = entity.itemCount,
                    subtotal = entity.subtotal,
                    discount = entity.discount,
                    tax = entity.tax,
                    tip = entity.tip,
                    totalAmount = entity.totalAmount,
                    status = status,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun placeOrder(
        diningMode: DiningMode,
        destinationInfo: String,
        cartItems: List<CartItem>,
        subtotal: Double,
        discount: Double,
        tax: Double,
        tip: Double,
        total: Double
    ): String {
        val randomSuffix = (1000..9999).random()
        val orderId = "#SAV-$randomSuffix"
        val itemsSummary = cartItems.joinToString(", ") { "${it.quantity}x ${it.name}" }
        val count = cartItems.sumOf { it.quantity }

        val entity = OrderEntity(
            id = orderId,
            diningMode = diningMode.name,
            destinationInfo = destinationInfo,
            itemsSummary = itemsSummary,
            itemCount = count,
            subtotal = subtotal,
            discount = discount,
            tax = tax,
            tip = tip,
            totalAmount = total,
            status = OrderStatus.PLACED.name,
            timestamp = System.currentTimeMillis()
        )
        db.orderDao().insertOrder(entity)
        db.cartDao().clearCart()
        return orderId
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        db.orderDao().updateOrderStatus(orderId, status.name)
    }

    // Reservations
    fun getReservations(): Flow<List<Reservation>> {
        return db.reservationDao().getAllReservations().map { list ->
            list.map { entity ->
                Reservation(
                    id = entity.id,
                    guestName = entity.guestName,
                    guestPhone = entity.guestPhone,
                    date = entity.date,
                    timeSlot = entity.timeSlot,
                    partySize = entity.partySize,
                    seatingArea = entity.seatingArea,
                    occasion = entity.occasion,
                    specialRequests = entity.specialRequests,
                    status = entity.status,
                    createdAt = entity.createdAt
                )
            }
        }
    }

    suspend fun createReservation(reservation: Reservation): Long {
        val entity = ReservationEntity(
            guestName = reservation.guestName,
            guestPhone = reservation.guestPhone,
            date = reservation.date,
            timeSlot = reservation.timeSlot,
            partySize = reservation.partySize,
            seatingArea = reservation.seatingArea,
            occasion = reservation.occasion,
            specialRequests = reservation.specialRequests,
            status = "Confirmed",
            createdAt = System.currentTimeMillis()
        )
        return db.reservationDao().insertReservation(entity)
    }

    suspend fun cancelReservation(id: Long) {
        db.reservationDao().updateStatus(id, "Cancelled")
    }
}
