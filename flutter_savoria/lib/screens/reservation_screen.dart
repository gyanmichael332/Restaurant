import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/restaurant_models.dart';
import '../providers/restaurant_provider.dart';

class ReservationScreen extends StatefulWidget {
  const ReservationScreen({super.key});

  @override
  State<ReservationScreen> createState() => _ReservationScreenState();
}

class _ReservationScreenState extends State<ReservationScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _phoneController = TextEditingController();
  int _partySize = 2;
  String _selectedTime = '7:00 PM';
  String _seatingArea = 'Main Dining Hall';

  final List<String> _timeSlots = ['5:30 PM', '6:00 PM', '6:30 PM', '7:00 PM', '7:30 PM', '8:00 PM', '8:30 PM', '9:00 PM'];
  final List<String> _areas = ['Main Dining Hall', 'Wine Cellar Room', 'Terrace & Patio', 'Chef Table'];

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<RestaurantProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Table Reservation'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text('Party Size', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [1, 2, 4, 6, 8].map((size) {
                  final isSelected = _partySize == size;
                  return ChoiceChip(
                    label: Text('$size Guests'),
                    selected: isSelected,
                    onSelected: (_) => setState(() => _partySize = size),
                  );
                }).toList(),
              ),
              const SizedBox(height: 20),

              const Text('Preferred Time Slot', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: _timeSlots.map((time) {
                  final isSelected = _selectedTime == time;
                  return FilterChip(
                    label: Text(time),
                    selected: isSelected,
                    onSelected: (_) => setState(() => _selectedTime = time),
                  );
                }).toList(),
              ),
              const SizedBox(height: 20),

              const Text('Seating Area', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              const SizedBox(height: 8),
              DropdownButtonFormField<String>(
                value: _seatingArea,
                decoration: const InputDecoration(border: OutlineInputBorder()),
                items: _areas.map((a) => DropdownMenuItem(value: a, child: Text(a))).toList(),
                onChanged: (val) => setState(() => _seatingArea = val ?? _areas.first),
              ),
              const SizedBox(height: 20),

              const Text('Guest Contact Details', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
              const SizedBox(height: 12),
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: 'Full Name', prefixIcon: Icon(Icons.person), border: OutlineInputBorder()),
                validator: (val) => (val == null || val.trim().isEmpty) ? 'Please enter your name' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _emailController,
                decoration: const InputDecoration(labelText: 'Email Address', prefixIcon: Icon(Icons.email), border: OutlineInputBorder()),
                validator: (val) => (val == null || !val.contains('@')) ? 'Please enter a valid email' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _phoneController,
                decoration: const InputDecoration(labelText: 'Phone Number', prefixIcon: Icon(Icons.phone), border: OutlineInputBorder()),
                validator: (val) => (val == null || val.trim().isEmpty) ? 'Please enter your phone' : null,
              ),
              const SizedBox(height: 24),

              SizedBox(
                width: double.infinity,
                child: FilledButton(
                  style: FilledButton.styleFrom(
                    backgroundColor: const Color(0xFFC5A059),
                    padding: const EdgeInsets.symmetric(vertical: 14),
                  ),
                  onPressed: () {
                    if (_formKey.currentState?.validate() ?? false) {
                      final res = Reservation(
                        id: 'res_${DateTime.now().millisecondsSinceEpoch}',
                        guestName: _nameController.text.trim(),
                        guestEmail: _emailController.text.trim(),
                        guestPhone: _phoneController.text.trim(),
                        partySize: _partySize,
                        date: 'Today',
                        timeSlot: _selectedTime,
                        seatingArea: _seatingArea,
                      );
                      provider.addReservation(res);

                      showDialog(
                        context: context,
                        builder: (_) => AlertDialog(
                          title: const Text('Reservation Confirmed!'),
                          content: Text('Table for $_partySize at $_selectedTime reserved under ${_nameController.text}.'),
                          actions: [
                            TextButton(onPressed: () => Navigator.pop(context), child: const Text('Great'))
                          ],
                        ),
                      );
                    }
                  },
                  child: const Text('Confirm Table Reservation', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
