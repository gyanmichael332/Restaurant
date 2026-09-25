package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.Category
import com.example.model.OrderStatus
import com.example.ui.components.DishDetailBottomSheet
import com.example.ui.components.OrderTrackerDialog
import com.example.ui.screens.CartScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OrdersAndFavoritesScreen
import com.example.ui.screens.ReservationsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.RestaurantViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                SavoriaRestaurantApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavoriaRestaurantApp(viewModel: RestaurantViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val reservations by viewModel.reservations.collectAsStateWithLifecycle()

    val filteredDishes by viewModel.filteredMenuItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedDietaryTag by viewModel.selectedDietaryTag.collectAsStateWithLifecycle()

    val selectedDish by viewModel.selectedDish.collectAsStateWithLifecycle()
    val activeTrackedOrder by viewModel.activeTrackedOrder.collectAsStateWithLifecycle()

    val diningMode by viewModel.diningMode.collectAsStateWithLifecycle()
    val destinationInfo by viewModel.destinationInfo.collectAsStateWithLifecycle()
    val couponCode by viewModel.couponCode.collectAsStateWithLifecycle()
    val discountPercent by viewModel.discountPercent.collectAsStateWithLifecycle()
    val couponMessage by viewModel.couponMessage.collectAsStateWithLifecycle()
    val selectedTipPercent by viewModel.selectedTipPercent.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(message = msg)
        }
    }

    val totalCartQuantity = cartItems.sumOf { it.quantity }
    val activeOrdersCount = orders.count { it.status != OrderStatus.COMPLETED }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Dining,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (currentTab) {
                                0 -> "Savoria Bistro"
                                1 -> "Culinary Menu"
                                2 -> "Table Booking"
                                3 -> "Your Dining Cart"
                                4 -> "Orders & Activity"
                                else -> "Savoria"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                actions = {
                    if (currentTab != 3 && totalCartQuantity > 0) {
                        IconButton(
                            onClick = { viewModel.selectTab(3) },
                            modifier = Modifier.testTag("top_bar_cart_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Text("$totalCartQuantity")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Cart",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                // Tab 0: Home
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 0) Icons.Default.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                // Tab 1: Menu
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 1) Icons.Default.RestaurantMenu else Icons.Outlined.RestaurantMenu,
                            contentDescription = "Menu"
                        )
                    },
                    label = { Text("Menu") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_menu")
                )

                // Tab 2: Reservations
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == 2) Icons.Default.EventSeat else Icons.Outlined.EventSeat,
                            contentDescription = "Reservations"
                        )
                    },
                    label = { Text("Reserve") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_reservations")
                )

                // Tab 3: Cart
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = {
                        if (totalCartQuantity > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Text("$totalCartQuantity")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == 3) Icons.Default.ShoppingBag else Icons.Outlined.ShoppingBag,
                                    contentDescription = "Cart"
                                )
                            }
                        } else {
                            Icon(
                                imageVector = if (currentTab == 3) Icons.Default.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Cart"
                            )
                        }
                    },
                    label = { Text("Order") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_cart")
                )

                // Tab 4: Activity
                NavigationBarItem(
                    selected = currentTab == 4,
                    onClick = { viewModel.selectTab(4) },
                    icon = {
                        if (activeOrdersCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ) {
                                        Text("$activeOrdersCount")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == 4) Icons.Default.ReceiptLong else Icons.Outlined.ReceiptLong,
                                    contentDescription = "Activity"
                                )
                            }
                        } else {
                            Icon(
                                imageVector = if (currentTab == 4) Icons.Default.ReceiptLong else Icons.Outlined.ReceiptLong,
                                contentDescription = "Activity"
                            )
                        }
                    },
                    label = { Text("Activity") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_orders")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> HomeScreen(
                    favorites = favorites,
                    onNavigateToMenu = { cat ->
                        if (cat != null) viewModel.selectCategory(cat)
                        viewModel.selectTab(1)
                    },
                    onNavigateToReservations = { viewModel.selectTab(2) },
                    onDishClick = { viewModel.openDishDetails(it) },
                    onQuickAdd = { viewModel.quickAddToCart(it) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) }
                )

                1 -> MenuScreen(
                    items = filteredDishes,
                    favorites = favorites,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    selectedDietaryTag = selectedDietaryTag,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    onCategorySelect = { viewModel.selectCategory(it) },
                    onDietarySelect = { viewModel.toggleDietaryTag(it) },
                    onDishClick = { viewModel.openDishDetails(it) },
                    onQuickAdd = { viewModel.quickAddToCart(it) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) }
                )

                2 -> ReservationsScreen(
                    reservations = reservations,
                    onMakeReservation = { name, phone, date, time, partySize, seating, occ, special, onSuccess ->
                        viewModel.makeReservation(name, phone, date, time, partySize, seating, occ, special, onSuccess)
                    },
                    onCancelReservation = { viewModel.cancelReservation(it) }
                )

                3 -> CartScreen(
                    cartItems = cartItems,
                    diningMode = diningMode,
                    destinationInfo = destinationInfo,
                    couponCode = couponCode,
                    discountPercent = discountPercent,
                    couponMessage = couponMessage,
                    selectedTipPercent = selectedTipPercent,
                    onDiningModeChange = { viewModel.setDiningMode(it) },
                    onDestinationInfoChange = { viewModel.setDestinationInfo(it) },
                    onCouponCodeChange = { viewModel.setCouponCode(it) },
                    onApplyCoupon = { viewModel.applyCoupon() },
                    onTipChange = { viewModel.setTipPercent(it) },
                    onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
                    onRemoveItem = { viewModel.removeCartItem(it) },
                    onClearCart = { viewModel.clearCart() },
                    onPlaceOrder = { viewModel.placeOrder() },
                    onExploreMenu = { viewModel.selectTab(1) }
                )

                4 -> OrdersAndFavoritesScreen(
                    orders = orders,
                    favorites = favorites,
                    onTrackOrder = { viewModel.openOrderTracker(it) },
                    onDishClick = { viewModel.openDishDetails(it) },
                    onQuickAdd = { viewModel.quickAddToCart(it) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onExploreMenu = { viewModel.selectTab(1) }
                )
            }

            // Dish Customization BottomSheet
            if (selectedDish != null) {
                val dish = selectedDish!!
                DishDetailBottomSheet(
                    dish = dish,
                    isFavorite = favorites.contains(dish.id),
                    onToggleFavorite = { viewModel.toggleFavorite(dish.id) },
                    onDismiss = { viewModel.closeDishDetails() },
                    onAddToCart = { mItem, qty, portion, spice, addOns, inst ->
                        viewModel.addToCart(mItem, qty, portion, spice, addOns, inst)
                    }
                )
            }

            // Live Order Tracker Dialog
            if (activeTrackedOrder != null) {
                OrderTrackerDialog(
                    order = activeTrackedOrder!!,
                    onDismiss = { viewModel.closeOrderTracker() }
                )
            }
        }
    }
}
