import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/restaurant_models.dart';
import '../providers/restaurant_provider.dart';

class CartScreen extends StatelessWidget {
  const CartScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<RestaurantProvider>();
    final items = provider.cartItems;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Your Order'),
        centerTitle: true,
        actions: [
          if (items.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.delete_outline),
              onPressed: () => provider.clearCart(),
            ),
        ],
      ),
      body: items.isEmpty
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.shopping_bag_outlined, size: 70, color: Colors.grey.shade400),
                  const SizedBox(height: 12),
                  const Text('Your cart is empty', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 6),
                  const Text('Explore our menu and add fine delicacies', style: TextStyle(color: Colors.grey)),
                ],
              ),
            )
          : Column(
              children: [
                // Dining mode selector
                Padding(
                  padding: const EdgeInsets.all(16),
                  child: SegmentedButton<DiningMode>(
                    segments: const [
                      ButtonSegment(value: DiningMode.dineIn, label: Text('Dine-In'), icon: Icon(Icons.table_bar)),
                      ButtonSegment(value: DiningMode.takeaway, label: Text('Takeaway'), icon: Icon(Icons.takeout_dining)),
                      ButtonSegment(value: DiningMode.delivery, label: Text('Delivery'), icon: Icon(Icons.delivery_dining)),
                    ],
                    selected: {provider.diningMode},
                    onSelectionChanged: (set) => provider.setDiningMode(set.first),
                  ),
                ),

                // Cart items list
                Expanded(
                  child: ListView.separated(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    itemCount: items.length,
                    separatorBuilder: (_, __) => const Divider(height: 1),
                    itemBuilder: (context, index) {
                      final item = items[index];
                      return Padding(
                        padding: const EdgeInsets.symmetric(vertical: 10),
                        child: Row(
                          children: [
                            ClipRRect(
                              borderRadius: BorderRadius.circular(8),
                              child: Image.network(item.dish.imageUrl, width: 60, height: 60, fit: BoxFit.cover),
                            ),
                            const SizedBox(width: 12),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(item.dish.name, style: const TextStyle(fontWeight: FontWeight.bold)),
                                  Text('\$${item.dish.price.toStringAsFixed(2)} each', style: const TextStyle(color: Colors.grey, fontSize: 13)),
                                ],
                              ),
                            ),
                            Row(
                              children: [
                                IconButton(
                                  icon: const Icon(Icons.remove_circle_outline, size: 20),
                                  onPressed: () => provider.updateQuantity(item.dish.id, -1),
                                ),
                                Text('${item.quantity}', style: const TextStyle(fontWeight: FontWeight.bold)),
                                IconButton(
                                  icon: const Icon(Icons.add_circle_outline, size: 20),
                                  onPressed: () => provider.updateQuantity(item.dish.id, 1),
                                ),
                              ],
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                ),

                // Checkout Card
                Container(
                  padding: const EdgeInsets.all(20),
                  decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.surfaceContainerHighest.withOpacity(0.5),
                    borderRadius: const BorderRadius.vertical(top: Radius.circular(24)),
                  ),
                  child: SafeArea(
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Text('Subtotal'),
                            Text('\$${provider.subtotal.toStringAsFixed(2)}'),
                          ],
                        ),
                        const SizedBox(height: 6),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Text('Taxes & Hospitality (8.25%)'),
                            Text('\$${provider.tax.toStringAsFixed(2)}'),
                          ],
                        ),
                        const Divider(height: 20),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Text('Total Amount', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                            Text('\$${provider.total.toStringAsFixed(2)}',
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 20, color: Color(0xFF8C262E))),
                          ],
                        ),
                        const SizedBox(height: 16),
                        SizedBox(
                          width: double.infinity,
                          child: FilledButton(
                            style: FilledButton.styleFrom(
                              backgroundColor: const Color(0xFFC5A059),
                              padding: const EdgeInsets.symmetric(vertical: 14),
                            ),
                            onPressed: () {
                              showDialog(
                                context: context,
                                builder: (_) => AlertDialog(
                                  title: const Text('Order Placed Successfully!'),
                                  content: Text('Your ${provider.diningMode.name} order has been sent to the chef.'),
                                  actions: [
                                    TextButton(
                                      onPressed: () {
                                        provider.clearCart();
                                        Navigator.pop(context);
                                      },
                                      child: const Text('OK'),
                                    )
                                  ],
                                ),
                              );
                            },
                            child: const Text('Place Order', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                          ),
                        )
                      ],
                    ),
                  ),
                )
              ],
            ),
    );
  }
}
