import 'package:flutter/material.dart';

import 'db_helper.dart';
import 'models/order.dart';
import 'models/order_item.dart';
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

  Future<void> _showOrderDetailsDialog(Order order) async {
    final orderData = await _db.getOrderWithItems(order.id);
    final items = orderData['items'] as List<Map<String, dynamic>>? ?? [];
    final totalPrice = orderData['totalPrice'] as double? ?? 0.0;

    if (!mounted) return;

    final maxPriceController = TextEditingController(text: order.maxPrice.toString());

    await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Order #${order.id}'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Date: ${order.date.toLocal().toString().split(' ')[0]}',
                style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: maxPriceController,
                keyboardType: const TextInputType.numberWithOptions(decimal: true),
                decoration: const InputDecoration(
                  labelText: 'Max Price',
                  border: OutlineInputBorder(),
                  prefixText: '\$',
                ),
              ),
              const SizedBox(height: 16),
              const Text(
                'Items:',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              if (items.isEmpty)
                const Text('No items in this order')
              else
                ...items.map((item) {
                  final product = item['product'] as Product;
                  final orderItem = item['orderItem'] as OrderItem;
                  final itemTotal = item['itemTotal'] as double;
                  return Padding(
                    padding: const EdgeInsets.only(bottom: 8),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Expanded(
                          child: Text(
                            '${product.name} x${orderItem.quantity}',
                            style: const TextStyle(fontSize: 14),
                          ),
                        ),
                        Text(
                          '\$${itemTotal.toStringAsFixed(2)}',
                          style: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  );
                }).toList(),
              const Divider(),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'Total:',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    '\$${totalPrice.toStringAsFixed(2)}',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF4ECDC4),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () async {
              await _db.deleteOrder(order.id);
              await _load();
              if (context.mounted) {
                Navigator.pop(context);
              }
            },
            style: TextButton.styleFrom(foregroundColor: Colors.red),
            child: const Text('Delete'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () async {
              final newMaxPrice = double.tryParse(maxPriceController.text);
              if (newMaxPrice == null || newMaxPrice <= 0) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Invalid max price')),
                );
                return;
              }

              final updatedOrder = order.copyWith(maxPrice: newMaxPrice);
              await _db.updateOrder(updatedOrder);
              await _load();
              if (context.mounted) {
                Navigator.pop(context);
              }
            },
            child: const Text('Update'),
          ),
        ],
      ),
    );

    maxPriceController.dispose();
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
        onPressed: () async {
          await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => const ProductsPage()),
          );
          _load();
        },
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
                            onTap: () => _showOrderDetailsDialog(order),
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
// PRODUCTS PAGE
// ------------------------------------------------------------
class ProductsPage extends StatefulWidget {
  const ProductsPage({super.key});

  @override
  State<ProductsPage> createState() => _ProductsPageState();
}

class _ProductsPageState extends State<ProductsPage>{
  final DbHelper _db = DbHelper.instance;
  List<Product> _products = [];
  final TextEditingController _maxPriceController = TextEditingController();
  DateTime _selectedDate = DateTime.now();

  final Map<int, int> _cart = {};

  @override
  void initState() {
    super.initState();
    _load();
  }

  @override
  void dispose() {
    _maxPriceController.dispose();
    super.dispose();
  }

  Future<void> _load() async {
    _products = await _db.getAllProducts();
    setState(() {});
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate,
      firstDate: DateTime(2020),
      lastDate: DateTime(2030),
    );
    if (picked != null && picked != _selectedDate) {
      setState(() {
        _selectedDate = picked;
      });
    }
  }

  void _addToCart(Product product) {
    setState(() {
      _cart[product.id] = (_cart[product.id] ?? 0) + 1;
    });
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('${product.name} added to order'),
        duration: const Duration(milliseconds: 500),
      ),
    );
  }

  double _calculateTotal() {
    double total = 0;
    for (var entry in _cart.entries) {
      final product = _products.firstWhere((p) => p.id == entry.key);
      total += product.price * entry.value;
    }
    return total;
  }

  Future<void> _confirmOrder() async {
    if (_cart.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Cart is empty')),
      );
      return;
    }

    final maxPrice = double.tryParse(_maxPriceController.text);
    if (maxPrice == null || maxPrice <= 0) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter a valid max price')),
      );
      return;
    }

    // Check if total exceeds budget
    final totalPrice = _calculateTotal();
    if (totalPrice > maxPrice) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Total (\$${totalPrice.toStringAsFixed(2)}) exceeds budget (\$${maxPrice.toStringAsFixed(2)})'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    // Create the order
    final order = Order(
      id: 0,
      maxPrice: maxPrice,
      date: _selectedDate,
    );

    final orderId = await _db.insertOrder(order);

    // Add order items
    for (var entry in _cart.entries) {
      final orderItem = OrderItem(
        id: 0,
        orderId: orderId,
        productId: entry.key,
        quantity: entry.value,
      );
      await _db.insertOrderItem(orderItem);
    }

    if (mounted) {
      Navigator.pop(context);
    }
  }

  Future<void> _showAddProductDialog() async {
    final nameController = TextEditingController();
    final priceController = TextEditingController();

    await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Add Product'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: nameController,
              decoration: const InputDecoration(
                labelText: 'Product Name',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: priceController,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: const InputDecoration(
                labelText: 'Price',
                border: OutlineInputBorder(),
                prefixText: '\$',
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () async {
              final name = nameController.text.trim();
              final price = double.tryParse(priceController.text);

              if (name.isEmpty || price == null || price <= 0) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Invalid input')),
                );
                return;
              }

              final product = Product(id: 0, name: name, price: price);
              await _db.insertProduct(product);
              await _load();
              if (context.mounted) {
                Navigator.pop(context);
              }
            },
            child: const Text('Add'),
          ),
        ],
      ),
    );
  }

  Future<void> _showEditProductDialog(Product product) async {
    final nameController = TextEditingController(text: product.name);
    final priceController = TextEditingController(text: product.price.toString());

    await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Edit Product'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: nameController,
              decoration: const InputDecoration(
                labelText: 'Product Name',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: priceController,
              keyboardType: const TextInputType.numberWithOptions(decimal: true),
              decoration: const InputDecoration(
                labelText: 'Price',
                border: OutlineInputBorder(),
                prefixText: '\$',
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () async {
              await _db.deleteProduct(product.id);
              await _load();
              if (context.mounted) {
                Navigator.pop(context);
              }
            },
            style: TextButton.styleFrom(foregroundColor: Colors.red),
            child: const Text('Delete'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () async {
              final name = nameController.text.trim();
              final price = double.tryParse(priceController.text);

              if (name.isEmpty || price == null || price <= 0) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Invalid input')),
                );
                return;
              }

              final updatedProduct = product.copyWith(name: name, price: price);
              await _db.updateProduct(updatedProduct);
              await _load();
              if (context.mounted) {
                Navigator.pop(context);
              }
            },
            child: const Text('Update'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final totalPrice = _calculateTotal();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Select Products'),
        backgroundColor: const Color(0xFF4ECDC4),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _showAddProductDialog,
        backgroundColor: const Color(0xFF4ECDC4),
        child: const Icon(Icons.add),
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
              child: Column(
                children: [
                  InkWell(
                    onTap: () => _selectDate(context),
                    child: Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Row(
                        children: [
                          const Icon(Icons.calendar_today, color: Color(0xFF4ECDC4)),
                          const SizedBox(width: 12),
                          Text(
                            'Date: ${_selectedDate.toLocal().toString().split(' ')[0]}',
                            style: const TextStyle(fontSize: 16),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  TextField(
                    controller: _maxPriceController,
                    keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    decoration: InputDecoration(
                      hintText: 'Max Price',
                      prefixIcon: const Icon(Icons.attach_money),
                      filled: true,
                      fillColor: Colors.white,
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                  if (_cart.isNotEmpty) ...[
                    const SizedBox(height: 12),
                    Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Column(
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              const Text(
                                'Total:',
                                style: TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              Text(
                                '\$${totalPrice.toStringAsFixed(2)}',
                                style: const TextStyle(
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                  color: Color(0xFF4ECDC4),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 12),
                          SizedBox(
                            width: double.infinity,
                            child: ElevatedButton.icon(
                              onPressed: _confirmOrder,
                              icon: const Icon(Icons.check_circle),
                              label: Text('Confirm Order (${_cart.length} items)'),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFF4ECDC4),
                                foregroundColor: Colors.white,
                                padding: const EdgeInsets.symmetric(vertical: 12),
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(12),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ],
              ),
            ),
            Expanded(
              child: _products.isEmpty
                  ? const Center(
                      child: Text(
                        'No products available',
                        style: TextStyle(fontSize: 16, color: Colors.grey),
                      ),
                    )
                  : ListView.builder(
                      padding: const EdgeInsets.symmetric(horizontal: 16),
                      itemCount: _products.length,
                      itemBuilder: (context, i) {
                        final product = _products[i];
                        final quantity = _cart[product.id] ?? 0;

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
                                product.name[0].toUpperCase(),
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            title: Row(
                              children: [
                                Expanded(
                                  child: Text(
                                    product.name,
                                    style: const TextStyle(fontWeight: FontWeight.w600),
                                  ),
                                ),
                                if (quantity > 0)
                                  Container(
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 8,
                                      vertical: 4,
                                    ),
                                    decoration: BoxDecoration(
                                      color: const Color(0xFF4ECDC4),
                                      borderRadius: BorderRadius.circular(12),
                                    ),
                                    child: Text(
                                      'x$quantity',
                                      style: const TextStyle(
                                        color: Colors.white,
                                        fontWeight: FontWeight.bold,
                                        fontSize: 12,
                                      ),
                                    ),
                                  ),
                              ],
                            ),
                            subtitle: Text(
                              '\$${product.price.toStringAsFixed(2)}',
                              style: const TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                                color: Color(0xFF4ECDC4),
                              ),
                            ),
                            trailing: IconButton(
                              icon: const Icon(Icons.edit, color: Colors.grey),
                              onPressed: () => _showEditProductDialog(product),
                            ),
                            onTap: () => _addToCart(product),
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


