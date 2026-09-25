import 'package:flutter/foundation.dart';
import '../models/restaurant_models.dart';

class RestaurantProvider extends ChangeNotifier {
  final List<Dish> _menu = [
    const Dish(
      id: 'd1',
      name: 'Truffle Filet Mignon',
      description: 'Prime dry-aged beef tenderloin with black truffle demi-glace and roasted seasonal root vegetables.',
      price: 48.00,
      category: 'Mains',
      imageUrl: 'https://images.unsplash.com/photo-1544025162-d76694265947',
      dietaryBadges: ['Gluten-Free', 'Chef Special'],
      isSpecial: true,
      calories: 680,
    ),
    const Dish(
      id: 'd2',
      name: 'Wild Mushroom Risotto',
      description: 'Carnaroli rice slow-cooked with foraged porcini, Parmigiano-Reggiano, and white truffle oil.',
      price: 28.50,
      category: 'Mains',
      imageUrl: 'https://images.unsplash.com/photo-1633964913295-ceb43826e7c9',
      dietaryBadges: ['Vegetarian', 'Gluten-Free'],
      calories: 520,
    ),
    const Dish(
      id: 'd3',
      name: 'Artisan Burrata Caprese',
      description: 'Heirloom vine tomatoes, creamy Pugliese burrata, aged balsamic glaze, and fresh Genovese basil.',
      price: 19.50,
      category: 'Starters',
      imageUrl: 'https://images.unsplash.com/photo-1592417817098-8f3d691079d7',
      dietaryBadges: ['Vegetarian'],
      calories: 410,
    ),
    const Dish(
      id: 'd4',
      name: 'Pan-Seared Chilean Sea Bass',
      description: 'Miso-glazed sea bass over baby bok choy and ginger-infused lemongrass dashi broth.',
      price: 44.00,
      category: 'Mains',
      imageUrl: 'https://images.unsplash.com/photo-1534422298391-e4f8c172dddb',
      dietaryBadges: ['Gluten-Free', 'Chef Special'],
      isSpecial: true,
      calories: 590,
    ),
    const Dish(
      id: 'd5',
      name: 'Valrhona Chocolate Fondant',
      description: 'Warm molten 70% dark chocolate cake served with house-spun Tahitian vanilla gelato.',
      price: 15.00,
      category: 'Desserts',
      imageUrl: 'https://images.unsplash.com/photo-1606313564200-e75d5e30476c',
      dietaryBadges: ['Vegetarian'],
      calories: 490,
    ),
    const Dish(
      id: 'd6',
      name: 'Signature Smoked Old Fashioned',
      description: 'Single barrel Kentucky bourbon, Angostura bitters, Luxardo cherry, infused with aromatic hickory smoke.',
      price: 18.00,
      category: 'Drinks',
      imageUrl: 'https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b',
      calories: 220,
    ),
  ];

  final Map<String, CartItem> _cart = {};
  final Set<String> _favorites = {'d1', 'd3'};
  final List<Reservation> _reservations = [];
  DiningMode _diningMode = DiningMode.dineIn;

  List<Dish> get menu => _menu;
  List<CartItem> get cartItems => _cart.values.toList();
  List<Reservation> get reservations => _reservations;
  DiningMode get diningMode => _diningMode;
  int get cartCount => _cart.values.fold(0, (sum, item) => sum + item.quantity);
  double get subtotal => _cart.values.fold(0.0, (sum, item) => sum + item.totalPrice);
  double get tax => subtotal * 0.0825;
  double get total => subtotal + tax;

  bool isFavorite(String dishId) => _favorites.contains(dishId);

  void toggleFavorite(String dishId) {
    if (_favorites.contains(dishId)) {
      _favorites.remove(dishId);
    } else {
      _favorites.add(dishId);
    }
    notifyListeners();
  }

  void addToCart(Dish dish) {
    if (_cart.containsKey(dish.id)) {
      _cart[dish.id]!.quantity += 1;
    } else {
      _cart[dish.id] = CartItem(dish: dish);
    }
    notifyListeners();
  }

  void updateQuantity(String dishId, int delta) {
    if (!_cart.containsKey(dishId)) return;
    _cart[dishId]!.quantity += delta;
    if (_cart[dishId]!.quantity <= 0) {
      _cart.remove(dishId);
    }
    notifyListeners();
  }

  void clearCart() {
    _cart.clear();
    notifyListeners();
  }

  void setDiningMode(DiningMode mode) {
    _diningMode = mode;
    notifyListeners();
  }

  void addReservation(Reservation reservation) {
    _reservations.insert(0, reservation);
    notifyListeners();
  }
}
