package com.example.data

import com.example.R
import com.example.model.Category
import com.example.model.DietaryTag
import com.example.model.MenuItem

object MenuDataSource {
    val items: List<MenuItem> = listOf(
        MenuItem(
            id = "wagyu_steak",
            name = "Pan-Seared Prime Wagyu",
            category = Category.CHEF_SPECIALS,
            description = "A5 Grade Wagyu tenderloin with rosemary-thyme butter, glazed baby asparagus, roasted bone marrow jus, and black garlic potato purée.",
            price = 48.00,
            calories = 780,
            prepTimeMin = 22,
            rating = 4.9,
            reviewCount = 184,
            imageResId = R.drawable.img_dish_wagyu,
            dietaryTags = listOf(DietaryTag.CHEF_PICK, DietaryTag.GLUTEN_FREE),
            allergens = listOf("Dairy"),
            isPopular = true,
            isChefSpecial = true,
            ingredients = listOf("A5 Wagyu Beef", "Rosemary", "Baby Asparagus", "Shallots", "Red Wine Reduction"),
            availableAddOns = listOf(
                "Shaved Fresh Black Truffle" to 12.00,
                "Seared Foie Gras Medallion" to 14.00,
                "Extra Roasted Bone Marrow" to 8.00
            )
        ),
        MenuItem(
            id = "truffle_pasta",
            name = "Artisanal Black Truffle Fettuccine",
            category = Category.PASTA_PIZZA,
            description = "House-made egg fettuccine rolled daily, tossed in 24-month aged Parmigiano-Reggiano emulsion, French cultured butter, and shaved Norcia black winter truffles.",
            price = 32.50,
            calories = 640,
            prepTimeMin = 16,
            rating = 4.8,
            reviewCount = 215,
            imageResId = R.drawable.img_dish_pasta,
            dietaryTags = listOf(DietaryTag.VEGETARIAN, DietaryTag.CHEF_PICK),
            allergens = listOf("Dairy", "Gluten", "Egg"),
            isPopular = true,
            isChefSpecial = true,
            ingredients = listOf("Semolina Pasta", "Aged Parmigiano", "Norcia Truffles", "Cultured Butter", "Cracked Pepper"),
            availableAddOns = listOf(
                "Extra Shaved Truffles (5g)" to 10.00,
                "Toasted Garlic Herb Focaccia" to 5.00,
                "Burrata Pugliese Center" to 7.00
            )
        ),
        MenuItem(
            id = "molten_lava",
            name = "Decadent Valrhona Molten Cake",
            category = Category.DESSERTS,
            description = "Warm 72% dark chocolate fondant with flowing cocoa center, Madagascar bourbon vanilla bean gelato, candied hazelnuts, and tart raspberry reduction.",
            price = 14.50,
            calories = 520,
            prepTimeMin = 14,
            rating = 4.9,
            reviewCount = 340,
            imageResId = R.drawable.img_dish_dessert,
            dietaryTags = listOf(DietaryTag.VEGETARIAN, DietaryTag.CHEF_PICK),
            allergens = listOf("Dairy", "Gluten", "Egg", "Tree Nuts"),
            isPopular = true,
            isChefSpecial = false,
            ingredients = listOf("72% Valrhona Cocoa", "Bourbon Vanilla Gelato", "Fresh Raspberries", "Piedmont Hazelnuts"),
            availableAddOns = listOf(
                "Extra Vanilla Gelato Scoop" to 3.50,
                "Warm Salted Caramel Drizzle" to 2.00
            )
        ),
        MenuItem(
            id = "burrata_tartine",
            name = "Heirloom Burrata Caprese",
            category = Category.STARTERS,
            description = "Handmade creamy burrata cheese, rainbow heirloom tomatoes, basil oil infusion, 15-year aged balsamic caviar, and charred sourdough crisps.",
            price = 18.00,
            calories = 410,
            prepTimeMin = 10,
            rating = 4.7,
            reviewCount = 128,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.VEGETARIAN),
            allergens = listOf("Dairy", "Gluten"),
            isPopular = true,
            isChefSpecial = false,
            ingredients = listOf("Burrata Cheese", "Heirloom Tomatoes", "Basil Chiffonade", "Aged Balsamic", "Sourdough"),
            availableAddOns = listOf(
                "Prosciutto di Parma Crisps" to 4.50,
                "Pine Nut Pesto Extra" to 3.00
            )
        ),
        MenuItem(
            id = "crispy_calamari",
            name = "Amalfi Crispy Calamari & Gambas",
            category = Category.STARTERS,
            description = "Golden semolina-crusted baby squid and wild shrimp, charred lemon wheels, caperberry emulsion, and spicy smoked paprika aioli.",
            price = 19.50,
            calories = 490,
            prepTimeMin = 12,
            rating = 4.6,
            reviewCount = 95,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.SPICY),
            allergens = listOf("Shellfish", "Gluten"),
            isPopular = false,
            isChefSpecial = false,
            ingredients = listOf("Wild Squid", "Tiger Shrimp", "Meyer Lemon", "Smoked Paprika", "Garlic Aioli"),
            availableAddOns = listOf(
                "Extra Truffle Aioli" to 2.50,
                "Spicy Pepperoncinis" to 1.50
            )
        ),
        MenuItem(
            id = "wild_salmon",
            name = "Pan-Roasted Ora King Salmon",
            category = Category.MAINS,
            description = "Crispy-skinned New Zealand Ora King salmon fillet, saffron cauliflower velvet puree, braised leeks, and Meyer lemon dill butter sauce.",
            price = 36.00,
            calories = 610,
            prepTimeMin = 18,
            rating = 4.8,
            reviewCount = 142,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.GLUTEN_FREE),
            allergens = listOf("Fish", "Dairy"),
            isPopular = true,
            isChefSpecial = true,
            ingredients = listOf("King Salmon", "Saffron Purée", "Baby Leeks", "Dill Butter", "Sea Salt"),
            availableAddOns = listOf(
                "Grilled Jumbo Asparagus" to 6.00,
                "Seared Sea Scallops (2pcs)" to 9.00
            )
        ),
        MenuItem(
            id = "mushroom_risotto",
            name = "Wild Forest Morel Risotto",
            category = Category.PASTA_PIZZA,
            description = "Slow-simmered Acquerello carnaroli rice, chanterelle and morel mushrooms, fresh thyme, aged Taleggio cheese, and white truffle essence.",
            price = 28.00,
            calories = 570,
            prepTimeMin = 20,
            rating = 4.7,
            reviewCount = 112,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.VEGETARIAN, DietaryTag.GLUTEN_FREE),
            allergens = listOf("Dairy"),
            isPopular = false,
            isChefSpecial = false,
            ingredients = listOf("Carnaroli Rice", "Morel Mushrooms", "Taleggio", "White Wine", "Vegetable Broth"),
            availableAddOns = listOf(
                "Crispy Pancetta Bits" to 4.00,
                "Extra Shaved Parmigiano" to 2.50
            )
        ),
        MenuItem(
            id = "margherita_pizza",
            name = "Wood-Fired Margherita DOP",
            category = Category.PASTA_PIZZA,
            description = "48-hour fermented Neapolitan dough baked at 900°F, San Marzano DOP tomato sauce, fresh buffalo mozzarella, fragrant Genovese basil, and EVOO.",
            price = 22.00,
            calories = 710,
            prepTimeMin = 15,
            rating = 4.9,
            reviewCount = 280,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.VEGETARIAN),
            allergens = listOf("Dairy", "Gluten"),
            isPopular = true,
            isChefSpecial = false,
            ingredients = listOf("00 Flour Dough", "San Marzano DOP", "Bufala Mozzarella", "Fresh Basil", "EVOO"),
            availableAddOns = listOf(
                "Spicy Calabrian 'Nduja" to 4.00,
                "Wild Arugula & Parmigiano" to 3.50,
                "White Truffle Drizzle" to 4.50
            )
        ),
        MenuItem(
            id = "bistro_tiramisu",
            name = "Classic Venetian Tiramisù",
            category = Category.DESSERTS,
            description = "Espresso-soaked Savoiardi ladyfingers, velvety mascarpone sabayon cream, dark Dutch cocoa dusting, and amaretto perfume.",
            price = 13.00,
            calories = 430,
            prepTimeMin = 8,
            rating = 4.8,
            reviewCount = 175,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.VEGETARIAN),
            allergens = listOf("Dairy", "Gluten", "Egg"),
            isPopular = false,
            isChefSpecial = false,
            ingredients = listOf("Mascarpone", "Illy Espresso", "Ladyfingers", "Valrhona Cocoa", "Amaretto"),
            availableAddOns = listOf(
                "Extra Espresso Shot Pairing" to 3.00
            )
        ),
        MenuItem(
            id = "cocktail_smoked_old_fashioned",
            name = "Smoked Rosemary Old Fashioned",
            category = Category.DRINKS,
            description = "Small-batch Kentucky bourbon, Angostura & orange bitters, demerara syrup, torched fresh rosemary sprig, expressed orange peel, smoked under glass cloche.",
            price = 16.50,
            calories = 190,
            prepTimeMin = 5,
            rating = 4.9,
            reviewCount = 160,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE),
            allergens = emptyList(),
            isPopular = true,
            isChefSpecial = true,
            ingredients = listOf("Kentucky Bourbon", "Demerara Sugar", "Angostura Bitters", "Smoked Rosemary", "Orange Zest"),
            availableAddOns = emptyList()
        ),
        MenuItem(
            id = "cocktail_hibiscus_spritz",
            name = "Elderflower & Hibiscus Spritz",
            category = Category.DRINKS,
            description = "Valdobbiadene Prosecco Superiore, St-Germain elderflower liqueur, artisanal hibiscus botanical infusion, sparkling club soda, and fresh mint.",
            price = 15.00,
            calories = 145,
            prepTimeMin = 4,
            rating = 4.7,
            reviewCount = 98,
            imageResId = null,
            dietaryTags = listOf(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE),
            allergens = emptyList(),
            isPopular = false,
            isChefSpecial = false,
            ingredients = listOf("Prosecco", "St-Germain", "Hibiscus Tea", "Soda Water", "Mint"),
            availableAddOns = emptyList()
        )
    )

    fun getById(id: String): MenuItem? = items.find { it.id == id }
}
