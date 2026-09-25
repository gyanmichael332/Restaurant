package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MenuDataSource
import com.example.model.CartItem
import com.example.model.Category
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Savoria", appName)
    }

    @Test
    fun `menu data source contains chef specials and items`() {
        val items = MenuDataSource.items
        assertTrue("Menu should have items", items.isNotEmpty())
        val specials = items.filter { it.isChefSpecial }
        assertTrue("Chef specials should be present", specials.isNotEmpty())
    }

    @Test
    fun `cart item calculation includes add-ons and quantity`() {
        val cartItem = CartItem(
            menuItemId = "wagyu_steak",
            name = "Pan-Seared Prime Wagyu",
            unitPrice = 48.0,
            quantity = 2,
            portionSize = "Regular",
            spiceLevel = "Standard",
            selectedAddOns = listOf("Truffle"),
            addOnsCost = 12.0
        )
        // (48 + 12) * 2 = 120.0
        assertEquals(120.0, cartItem.totalPrice, 0.001)
    }
}
