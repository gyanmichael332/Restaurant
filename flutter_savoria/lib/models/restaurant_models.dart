enum DiningMode { dineIn, takeaway, delivery }

class Dish {
  final String id;
  final String name;
  final String description;
  final double price;
  final String category;
  final String imageUrl;
  final List<String> dietaryBadges;
  final bool isSpecial;
  final int calories;
  final double rating;

  const Dish({
    required this.id,
    required this.name,
    required this.description,
    required this.price,
    required this.category,
    required this.imageUrl,
    this.dietaryBadges = const [],
    this.isSpecial = false,
    this.calories = 450,
    this.rating = 4.8,
  });
}

class CartItem {
  final Dish dish;
  int quantity;
  String specialInstructions;

  CartItem({
    required this.dish,
    this.quantity = 1,
    this.specialInstructions = '',
  });

  double get totalPrice => dish.price * quantity;
}

class Reservation {
  final String id;
  final String guestName;
  final String guestEmail;
  final String guestPhone;
  final int partySize;
  final String date;
  final String timeSlot;
  final String seatingArea;
  final String specialOccasion;

  Reservation({
    required this.id,
    required this.guestName,
    required this.guestEmail,
    required this.guestPhone,
    required this.partySize,
    required this.date,
    required this.timeSlot,
    this.seatingArea = 'Main Dining Room',
    this.specialOccasion = 'None',
  });
}
