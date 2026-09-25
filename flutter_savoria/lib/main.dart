import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/restaurant_provider.dart';
import 'screens/home_screen.dart';
import 'screens/menu_screen.dart';
import 'screens/cart_screen.dart';
import 'screens/reservation_screen.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => RestaurantProvider()),
      ],
      child: const SavoriaBistroApp(),
    ),
  );
}

class SavoriaBistroApp extends StatelessWidget {
  const SavoriaBistroApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Savoria Bistro',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFC5A059),
          primary: const Color(0xFF8C262E),
          secondary: const Color(0xFFC5A059),
          surface: const Color(0xFFFAF8F5),
          brightness: Brightness.light,
        ),
      ),
      home: const MainNavigationShell(),
    );
  }
}

class MainNavigationShell extends StatefulWidget {
  const MainNavigationShell({super.key});

  @override
  State<MainNavigationShell> createState() => _MainNavigationShellState();
}

class _MainNavigationShellState extends State<MainNavigationShell> {
  int _currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    final cartCount = context.watch<RestaurantProvider>().cartCount;

    final pages = [
      HomeScreen(
        onNavigateToMenu: () => setState(() => _currentIndex = 1),
        onNavigateToReserve: () => setState(() => _currentIndex = 3),
      ),
      const MenuScreen(),
      const CartScreen(),
      const ReservationScreen(),
    ];

    return Scaffold(
      body: pages[_currentIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _currentIndex,
        onDestinationSelected: (index) => setState(() => _currentIndex = index),
        destinations: [
          const NavigationDestination(
            icon: Icon(Icons.home_outlined),
            selectedIcon: Icon(Icons.home),
            label: 'Home',
          ),
          const NavigationDestination(
            icon: Icon(Icons.restaurant_menu_outlined),
            selectedIcon: Icon(Icons.restaurant_menu),
            label: 'Menu',
          ),
          NavigationDestination(
            icon: Badge(
              isLabelVisible: cartCount > 0,
              label: Text('$cartCount'),
              child: const Icon(Icons.shopping_bag_outlined),
            ),
            selectedIcon: Badge(
              isLabelVisible: cartCount > 0,
              label: Text('$cartCount'),
              child: const Icon(Icons.shopping_bag),
            ),
            label: 'Cart',
          ),
          const NavigationDestination(
            icon: Icon(Icons.event_seat_outlined),
            selectedIcon: Icon(Icons.event_seat),
            label: 'Reserve',
          ),
        ],
      ),
    );
  }
}
