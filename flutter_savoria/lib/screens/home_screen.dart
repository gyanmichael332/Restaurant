import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/restaurant_provider.dart';

class HomeScreen extends StatelessWidget {
  final VoidCallback onNavigateToMenu;
  final VoidCallback onNavigateToReserve;

  const HomeScreen({
    super.key,
    required this.onNavigateToMenu,
    required this.onNavigateToReserve,
  });

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<RestaurantProvider>();
    final specials = provider.menu.where((d) => d.isSpecial).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Column(
          children: [
            Text('SAVORIA BISTRO', style: TextStyle(fontWeight: FontWeight.bold, letterSpacing: 2, fontSize: 16)),
            Text('Fine Culinary Artistry', style: TextStyle(fontSize: 11, color: Colors.grey)),
          ],
        ),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Hero Banner
            Stack(
              children: [
                Image.network(
                  'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4',
                  height: 220,
                  width: double.infinity,
                  fit: BoxFit.cover,
                ),
                Container(
                  height: 220,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        Colors.black.withOpacity(0.2),
                        Colors.black.withOpacity(0.7),
                      ],
                    ),
                  ),
                ),
                Positioned(
                  bottom: 16,
                  left: 16,
                  right: 16,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFFC5A059),
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: const Text('MICHELIN GUIDED', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                      ),
                      const SizedBox(height: 6),
                      const Text(
                        'An Unforgettable Evening of Taste',
                        style: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 10),
                      Row(
                        children: [
                          FilledButton.icon(
                            style: FilledButton.styleFrom(backgroundColor: const Color(0xFFC5A059)),
                            onPressed: onNavigateToReserve,
                            icon: const Icon(Icons.table_restaurant, size: 16),
                            label: const Text('Book Table'),
                          ),
                          const SizedBox(width: 8),
                          OutlinedButton.icon(
                            style: OutlinedButton.styleFrom(foregroundColor: Colors.white, side: const BorderSide(color: Colors.white)),
                            onPressed: onNavigateToMenu,
                            icon: const Icon(Icons.restaurant_menu, size: 16),
                            label: const Text('View Menu'),
                          ),
                        ],
                      )
                    ],
                  ),
                )
              ],
            ),

            const SizedBox(height: 20),

            // Chef Specials Section
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text("Chef's Recommendations", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  TextButton(onPressed: onNavigateToMenu, child: const Text('See All')),
                ],
              ),
            ),

            SizedBox(
              height: 240,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                padding: const EdgeInsets.symmetric(horizontal: 16),
                itemCount: specials.length,
                itemBuilder: (context, index) {
                  final dish = specials[index];
                  return Container(
                    width: 180,
                    margin: const EdgeInsets.only(right: 14),
                    child: Card(
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                      clipBehavior: Clip.antiAlias,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Image.network(dish.imageUrl, height: 110, width: 180, fit: BoxFit.cover),
                          Padding(
                            padding: const EdgeInsets.all(10),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(dish.name, maxLines: 1, overflow: TextOverflow.ellipsis, style: const TextStyle(fontWeight: FontWeight.bold)),
                                const SizedBox(height: 4),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text('\$${dish.price.toStringAsFixed(2)}', style: const TextStyle(fontWeight: FontWeight.bold, color: Color(0xFF8C262E))),
                                    IconButton(
                                      visualDensity: VisualDensity.compact,
                                      icon: const Icon(Icons.add_circle, color: Color(0xFFC5A059)),
                                      onPressed: () {
                                        provider.addToCart(dish);
                                        ScaffoldMessenger.of(context).showSnackBar(
                                          SnackBar(content: Text('Added ${dish.name}'), duration: const Duration(seconds: 1)),
                                        );
                                      },
                                    )
                                  ],
                                )
                              ],
                            ),
                          )
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
