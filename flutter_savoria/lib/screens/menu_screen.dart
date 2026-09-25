import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/restaurant_provider.dart';

class MenuScreen extends StatefulWidget {
  const MenuScreen({super.key});

  @override
  State<MenuScreen> createState() => _MenuScreenState();
}

class _MenuScreenState extends State<MenuScreen> {
  String _selectedCategory = 'All';
  String _searchQuery = '';
  final List<String> _categories = ['All', 'Starters', 'Mains', 'Desserts', 'Drinks'];

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<RestaurantProvider>();
    final dishes = provider.menu.where((d) {
      final matchesCategory = _selectedCategory == 'All' || d.category == _selectedCategory;
      final matchesQuery = _searchQuery.isEmpty ||
          d.name.toLowerCase().contains(_searchQuery.toLowerCase()) ||
          d.description.toLowerCase().contains(_searchQuery.toLowerCase());
      return matchesCategory && matchesQuery;
    }).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Culinary Menu'),
        centerTitle: true,
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: SearchBar(
              hintText: 'Search dishes, ingredients...',
              leading: const Icon(Icons.search),
              onChanged: (val) => setState(() => _searchQuery = val),
            ),
          ),
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
            child: Row(
              children: _categories.map((cat) {
                final isSelected = _selectedCategory == cat;
                return Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: FilterChip(
                    label: Text(cat),
                    selected: isSelected,
                    onSelected: (_) => setState(() => _selectedCategory = cat),
                  ),
                );
              }).toList(),
            ),
          ),
          Expanded(
            child: dishes.isEmpty
                ? const Center(child: Text('No dishes found'))
                : ListView.builder(
                    padding: const EdgeInsets.all(16),
                    itemCount: dishes.length,
                    itemBuilder: (context, index) {
                      final dish = dishes[index];
                      final isFav = provider.isFavorite(dish.id);
                      return Card(
                        elevation: 1.5,
                        margin: const EdgeInsets.only(bottom: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                        child: Padding(
                          padding: const EdgeInsets.all(12),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              ClipRRect(
                                borderRadius: BorderRadius.circular(12),
                                child: Image.network(
                                  dish.imageUrl,
                                  width: 95,
                                  height: 95,
                                  fit: BoxFit.cover,
                                  errorBuilder: (_, __, ___) => Container(
                                    width: 95,
                                    height: 95,
                                    color: Colors.grey.shade300,
                                    child: const Icon(Icons.restaurant),
                                  ),
                                ),
                              ),
                              const SizedBox(width: 14),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                      children: [
                                        Expanded(
                                          child: Text(dish.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
                                        ),
                                        IconButton(
                                          visualDensity: VisualDensity.compact,
                                          icon: Icon(isFav ? Icons.favorite : Icons.favorite_border,
                                              color: isFav ? Colors.red : Colors.grey, size: 20),
                                          onPressed: () => provider.toggleFavorite(dish.id),
                                        )
                                      ],
                                    ),
                                    Text(dish.description, maxLines: 2, overflow: TextOverflow.ellipsis,
                                        style: TextStyle(fontSize: 12, color: Colors.grey.shade700)),
                                    const SizedBox(height: 8),
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                      children: [
                                        Text('\$${dish.price.toStringAsFixed(2)}',
                                            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                                        FilledButton.tonalIcon(
                                          onPressed: () {
                                            provider.addToCart(dish);
                                            ScaffoldMessenger.of(context).showSnackBar(
                                              SnackBar(content: Text('Added ${dish.name}'), duration: const Duration(milliseconds: 900)),
                                            );
                                          },
                                          icon: const Icon(Icons.add, size: 16),
                                          label: const Text('Add'),
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
    );
  }
}
