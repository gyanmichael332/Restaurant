package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.DiningMode
import java.util.Locale

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    diningMode: DiningMode,
    destinationInfo: String,
    couponCode: String,
    discountPercent: Double,
    couponMessage: String?,
    selectedTipPercent: Int,
    onDiningModeChange: (DiningMode) -> Unit,
    onDestinationInfoChange: (String) -> Unit,
    onCouponCodeChange: (String) -> Unit,
    onApplyCoupon: () -> Unit,
    onTipChange: (Int) -> Unit,
    onUpdateQuantity: (cartItemId: Long, newQuantity: Int) -> Unit,
    onRemoveItem: (cartItemId: Long) -> Unit,
    onClearCart: () -> Unit,
    onPlaceOrder: () -> Unit,
    onExploreMenu: () -> Unit
) {
    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .testTag("cart_empty_state"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Order is Empty",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Indulge in our exquisite dishes, handcrafted pastas, and fine desserts.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onExploreMenu,
                    modifier = Modifier.testTag("browse_menu_button")
                ) {
                    Text("Explore Menu")
                }
            }
        }
        return
    }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val discount = subtotal * discountPercent
    val discountedSubtotal = (subtotal - discount).coerceAtLeast(0.0)
    val tax = discountedSubtotal * 0.0825
    val tip = discountedSubtotal * (selectedTipPercent / 100.0)
    val total = discountedSubtotal + tax + tip

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("cart_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dining Mode Selector
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Dining Preference",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple(DiningMode.DINE_IN, "Dine-In", Icons.Default.TableRestaurant),
                            Triple(DiningMode.TAKEAWAY, "Takeaway", Icons.Default.ShoppingBag),
                            Triple(DiningMode.DELIVERY, "Delivery", Icons.Default.DeliveryDining)
                        ).forEach { (mode, label, icon) ->
                            FilterChip(
                                selected = diningMode == mode,
                                onClick = { onDiningModeChange(mode) },
                                label = { Text(label) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("mode_${mode.name}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = destinationInfo,
                        onValueChange = onDestinationInfoChange,
                        label = {
                            Text(
                                when (diningMode) {
                                    DiningMode.DINE_IN -> "Table Number / Hall"
                                    DiningMode.TAKEAWAY -> "Pickup Details / Vehicle Info"
                                    DiningMode.DELIVERY -> "Delivery Address & Apt / Gate Code"
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("destination_info_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Cart items header with clear all
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Items in Order (${cartItems.sumOf { it.quantity }})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = onClearCart,
                    modifier = Modifier.testTag("clear_cart_button")
                ) {
                    Text("Clear All", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        // Cart Items
        items(cartItems, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (item.portionSize != "Regular" || item.spiceLevel != "Standard") {
                                Text(
                                    text = "${item.portionSize} • ${item.spiceLevel}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (item.selectedAddOns.isNotEmpty()) {
                                Text(
                                    text = "+ ${item.selectedAddOns.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (item.specialInstructions.isNotBlank()) {
                                Text(
                                    text = "Note: \"${item.specialInstructions}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = "$${String.format(Locale.US, "%.2f", item.totalPrice)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onRemoveItem(item.id) },
                            modifier = Modifier.testTag("remove_item_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Remove Item",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        // Quantity counter
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = { onUpdateQuantity(item.id, item.quantity - 1) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("cart_decrease_${item.id}")
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = "${item.quantity}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            IconButton(
                                onClick = { onUpdateQuantity(item.id, item.quantity + 1) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("cart_increase_${item.id}")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Promo Coupon Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Promotion Voucher",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = couponCode,
                            onValueChange = onCouponCodeChange,
                            placeholder = { Text("Enter code (e.g. SAVOR15)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("coupon_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onApplyCoupon,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("apply_coupon_button")
                        ) {
                            Text("Apply")
                        }
                    }

                    if (couponMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = couponMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (discountPercent > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Hospitality Gratuity Tip Selection
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Chef & Staff Gratuity",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0, 10, 15, 20).forEach { percent ->
                            FilterChip(
                                selected = selectedTipPercent == percent,
                                onClick = { onTipChange(percent) },
                                label = { Text(if (percent == 0) "No Tip" else "$percent%") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tip_chip_$percent")
                            )
                        }
                    }
                }
            }
        }

        // Summary & Checkout
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bill Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$${String.format(Locale.US, "%.2f", subtotal)}")
                    }

                    if (discount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Discount (${(discountPercent * 100).toInt()}%)", color = MaterialTheme.colorScheme.primary)
                            Text("-$${String.format(Locale.US, "%.2f", discount)}", color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tax & Culinary Surcharge (8.25%)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$${String.format(Locale.US, "%.2f", tax)}")
                    }

                    if (tip > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Staff Gratuity ($selectedTipPercent%)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$${String.format(Locale.US, "%.2f", tip)}")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Payable",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", total)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onPlaceOrder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("place_order_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Confirm & Place Order",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
