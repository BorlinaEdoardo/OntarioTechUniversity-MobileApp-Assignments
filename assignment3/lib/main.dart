import 'package:flutter/material.dart';

import 'db_helper.dart';
import 'models/order.dart';
import 'models/product.dart';

void main() {
  runApp(const OrderApp());
}

class OrderApp extends StatelessWidget {
  const OrderApp({super.key});

  @override
  Widget build(BuildContext Context){
    return MaterialApp(
      title: 'Order Management',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF4ECDC4)),
        useMaterial3: true,
      ),
      home: const HomeScreen(),
    );
  }

}

// ------------------------------------------------------------
// HOME SCREEN
// ------------------------------------------------------------
class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final DbHelper _db = DbHelper.instance;
  List<Order> _orders = [];
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _load();
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _load() async {
    _orders = await _db.getAllOrders();
    setState(() {});
  }

  Future<void> _searchByDate() async {
    final dateStr = _searchController.text.trim();
    if (dateStr.isEmpty) {
      _load();
      return;
    }
    try {
      final date = DateTime.parse(dateStr);
      _orders = await _db.getOrdersByDate(date);
      setState(() {});
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Invalid format. Use: YYYY-MM-DD')),
      );
    }
  }

  // ------------------------------------------------------------
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Food Orders'),
        backgroundColor: const Color(0xFF4ECDC4),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _load,
        backgroundColor: const Color(0xFF4ECDC4),
        child: const Icon(Icons.add_shopping_cart),
      ),
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFE9FDF7), Color(0xFFC8F4EF)],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
        ),
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: TextField(
                controller: _searchController,
                decoration: InputDecoration(
                  hintText: 'Search by date (YYYY-MM-DD)',
                  prefixIcon: const Icon(Icons.search),
                  suffixIcon: IconButton(
                    icon: const Icon(Icons.clear),
                    onPressed: () {
                      _searchController.clear();
                      _load();
                    },
                  ),
                  filled: true,
                  fillColor: Colors.white,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: BorderSide.none,
                  ),
                ),
                onSubmitted: (_) => _searchByDate(),
              ),
            ),
            Expanded(
              child: _orders.isEmpty
                  ? const Center(
                      child: Text(
                        'No orders found.',
                        style: TextStyle(fontSize: 16, color: Colors.grey),
                      ),
                    )
                  : ListView.builder(
                      padding: const EdgeInsets.symmetric(horizontal: 16),
                      itemCount: _orders.length,
                      itemBuilder: (context, i) {
                        final order = _orders[i];
                        return Card(
                          elevation: 3,
                          margin: const EdgeInsets.only(bottom: 12),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: ListTile(
                            leading: CircleAvatar(
                              backgroundColor: const Color(0xFF4ECDC4),
                              child: Text(
                                '#${order.id}',
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            title: Text(
                              'Budget: \$${order.maxPrice.toStringAsFixed(2)}',
                              style: const TextStyle(fontWeight: FontWeight.w600),
                            ),
                            subtitle: Text(
                              order.date.toLocal().toString().split(' ')[0],
                              style: TextStyle(color: Colors.grey[600]),
                            ),
                            trailing: const Icon(Icons.chevron_right),
                            onTap: () {},
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

// ------------------------------------------------------------
// ORDER PAGE
// ------------------------------------------------------------
class OrderPage extends StatefulWidget {
  const OrderPage({super.key});

  @override
  State<OrderPage> createState() => _OrderPageState();
}

class _OrderPageState extends State<OrderPage>{
  final DbHelper _db = DbHelper.instance;
  List<Product> _products = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    _products = await _db.getAllProducts();
    setState(() {});
  }



}


