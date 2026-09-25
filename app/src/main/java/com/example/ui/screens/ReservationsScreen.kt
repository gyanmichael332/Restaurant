package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Reservation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReservationsScreen(
    reservations: List<Reservation>,
    onMakeReservation: (
        name: String,
        phone: String,
        date: String,
        time: String,
        partySize: Int,
        seating: String,
        occasion: String,
        special: String,
        onSuccess: () -> Unit
    ) -> Unit,
    onCancelReservation: (Long) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Book, 1: My Bookings

    // Form states
    val cal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.US)
    val availableDates = remember {
        (0..6).map { offset ->
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, offset)
            if (offset == 0) "Today (${dateFormat.format(c.time)})"
            else if (offset == 1) "Tomorrow (${dateFormat.format(c.time)})"
            else dateFormat.format(c.time)
        }
    }

    var selectedDate by remember { mutableStateOf(availableDates.first()) }
    var selectedTimeSlot by remember { mutableStateOf("7:00 PM") }
    var partySize by remember { mutableIntStateOf(2) }
    var seatingArea by remember { mutableStateOf("Indoor Main Dining") }
    var occasion by remember { mutableStateOf("Casual Dining") }
    var guestName by remember { mutableStateOf("") }
    var guestPhone by remember { mutableStateOf("") }
    var specialRequests by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val timeSlots = listOf(
        "12:00 PM", "1:15 PM", "2:30 PM",
        "5:30 PM", "6:45 PM", "7:30 PM", "8:15 PM", "9:00 PM"
    )

    val seatingOptions = listOf(
        "Indoor Main Dining",
        "Garden Terrace Patio",
        "Chef's Counter",
        "Private Wine Booth"
    )

    val occasions = listOf("Casual Dining", "Birthday", "Anniversary", "Business Dinner", "Romantic Date")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("reservations_screen")
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Book a Table", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_book_table")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("My Bookings", fontWeight = FontWeight.Bold)
                        if (reservations.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${reservations.size}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.testTag("tab_my_bookings")
            )
        }

        if (selectedTab == 0) {
            // Reservation Booking Form
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventSeat,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Fine Dining Table Reservations",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Instant table confirmation • No reservation fees",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Date Selection
                item {
                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableDates) { d ->
                            FilterChip(
                                selected = selectedDate == d,
                                onClick = { selectedDate = d },
                                label = { Text(d) },
                                shape = RoundedCornerShape(14.dp)
                            )
                        }
                    }
                }

                // Party Size
                item {
                    Text(
                        text = "Guests (${partySize} people)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items((1..10).toList()) { count ->
                            FilterChip(
                                selected = partySize == count,
                                onClick = { partySize = count },
                                label = { Text("$count ${if (count == 1) "Guest" else "Guests"}") },
                                shape = RoundedCornerShape(14.dp),
                                leadingIcon = if (partySize == count) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                    }
                }

                // Time Slots
                item {
                    Text(
                        text = "Available Dining Slots",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        timeSlots.forEach { slot ->
                            FilterChip(
                                selected = selectedTimeSlot == slot,
                                onClick = { selectedTimeSlot = slot },
                                label = { Text(slot) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                // Seating Atmosphere
                item {
                    Text(
                        text = "Seating Area Preference",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        seatingOptions.forEach { area ->
                            FilterChip(
                                selected = seatingArea == area,
                                onClick = { seatingArea = area },
                                label = { Text(area) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                // Occasion
                item {
                    Text(
                        text = "Dining Occasion",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(occasions) { occ ->
                            FilterChip(
                                selected = occasion == occ,
                                onClick = { occasion = occ },
                                label = { Text(occ) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                // Guest Contact Details
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Guest Contact Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = guestName,
                                onValueChange = {
                                    guestName = it
                                    errorMessage = null
                                },
                                label = { Text("Full Name *") },
                                placeholder = { Text("e.g. Eleanor Vance") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reservation_name_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = guestPhone,
                                onValueChange = {
                                    guestPhone = it
                                    errorMessage = null
                                },
                                label = { Text("Mobile Phone Number *") },
                                placeholder = { Text("e.g. (555) 234-5678") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reservation_phone_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = specialRequests,
                                onValueChange = { specialRequests = it },
                                label = { Text("Special Requests / Dietary Needs (Optional)") },
                                placeholder = { Text("e.g. Window seat, anniversary champagne, high chair") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reservation_requests_input"),
                                shape = RoundedCornerShape(12.dp),
                                maxLines = 3
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    item {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Submit Button
                item {
                    Button(
                        onClick = {
                            if (guestName.isBlank()) {
                                errorMessage = "Please enter your name"
                                return@Button
                            }
                            if (guestPhone.isBlank()) {
                                errorMessage = "Please enter a contact phone number"
                                return@Button
                            }
                            onMakeReservation(
                                guestName.trim(),
                                guestPhone.trim(),
                                selectedDate,
                                selectedTimeSlot,
                                partySize,
                                seatingArea,
                                occasion,
                                specialRequests.trim()
                            ) {
                                selectedTab = 1
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_reservation_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Confirm Reservation ($partySize Guests)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // My Reservations List
            if (reservations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Reservations Yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Book your table in advance for lunch, dinner, or special occasions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { selectedTab = 0 }) {
                            Text("Book a Table Now")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reservations, key = { it.id }) { res ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ref: #RES-${res.id + 1000}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (res.status == "Confirmed") Color(0xFF2E7D32) else MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                    ) {
                                        Text(
                                            text = res.status,
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "${res.date} • ${res.timeSlot}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${res.partySize} Guests • ${res.seatingArea}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = "Reserved for: ${res.guestName} (${res.guestPhone})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (res.specialRequests.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Requests: \"${res.specialRequests}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                if (res.status == "Confirmed") {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedButton(
                                        onClick = { onCancelReservation(res.id) },
                                        modifier = Modifier.align(Alignment.End),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("Cancel Reservation")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
