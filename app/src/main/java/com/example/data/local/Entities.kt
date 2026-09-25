package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val menuItemId: String,
    val name: String,
    val unitPrice: Double,
    val quantity: Int,
    val portionSize: String = "Regular",
    val spiceLevel: String = "Standard",
    val addOns: String = "",
    val addOnsCost: Double = 0.0,
    val instructions: String = "",
    val imageResId: Int? = null
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val menuItemId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val diningMode: String,
    val destinationInfo: String,
    val itemsSummary: String,
    val itemCount: Int,
    val subtotal: Double,
    val discount: Double,
    val tax: Double,
    val tip: Double,
    val totalAmount: Double,
    val status: String,
    val timestamp: Long
)

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
