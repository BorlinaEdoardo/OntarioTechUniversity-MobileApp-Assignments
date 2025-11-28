import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import 'models/order_item.dart';
import 'models/order.dart';
import 'models/product.dart';

class DbHelper {
  DbHelper._internal();

  static final DbHelper instance = DbHelper._internal();

  static const _dbName = 'food_orders.db';
  static const _dbVersion = 2;
  static const _tableOrders = 'orders';
  static const _tableProducts = 'products';
  static const _tableOrderItems = 'order_items';

  Database? _database;

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDb();
    return _database!;
  }

  Future<Database> _initDb() async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, _dbName);

    return openDatabase(
      path,
      version: _dbVersion,
      onCreate: (db, version) async {
        await db.execute('''
          CREATE TABLE $_tableOrders (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            maxPrice REAL NOT NULL,
            date TEXT NOT NULL
          )
        ''');
        await db.execute('''
          CREATE TABLE $_tableProducts (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            price REAL NOT NULL
          )
        ''');
        await db.execute('''
          CREATE TABLE $_tableOrderItems (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            orderId INTEGER NOT NULL,
            productId INTEGER NOT NULL,
            quantity INTEGER NOT NULL,
            FOREIGN KEY (orderId) REFERENCES $_tableOrders (id) ON DELETE CASCADE,
            FOREIGN KEY (productId) REFERENCES $_tableProducts (id)
          )
        ''');

        await _insertInitialProducts(db);
      },
    );
  }

  Future<void> _insertInitialProducts(Database db) async {
    final products = [
      {'name': 'Pizza Margherita', 'price': 8.50},
      {'name': 'Hamburger', 'price': 6.00},
      {'name': 'Spaghetti Carbonara', 'price': 9.00},
      {'name': 'Caesar Salad', 'price': 7.50},
      {'name': 'Chicken Wings', 'price': 5.50},
      {'name': 'French Fries', 'price': 3.00},
      {'name': 'Sushi Roll', 'price': 12.00},
      {'name': 'Tacos', 'price': 7.00},
      {'name': 'Pad Thai', 'price': 10.00},
      {'name': 'Fish and Chips', 'price': 11.00},
      {'name': 'Lasagna', 'price': 9.50},
      {'name': 'Ramen', 'price': 10.50},
      {'name': 'Burrito', 'price': 8.00},
      {'name': 'Chicken Sandwich', 'price': 6.50},
      {'name': 'Greek Salad', 'price': 7.00},
      {'name': 'Poke Bowl', 'price': 11.50},
      {'name': 'Beef Steak', 'price': 15.00},
      {'name': 'Grilled Salmon', 'price': 13.50},
      {'name': 'Veggie Burger', 'price': 7.50},
      {'name': 'Club Sandwich', 'price': 8.50},
      {'name': 'Pho', 'price': 9.50},
      {'name': 'Fried Rice', 'price': 7.00},
      {'name': 'BBQ Ribs', 'price': 14.00},
      {'name': 'Falafel Wrap', 'price': 6.50},
      {'name': 'Margherita Pizza', 'price': 10.00},
    ];

    for (var product in products) {
      await db.insert(_tableProducts, product);
    }
  }

  Future<int> insertOrder(Order order) async {
    final db = await database;
    return await db.insert(_tableOrders, order.toMap());
  }

  Future<int> updateOrder(Order order) async {
    final db = await database;
    return await db.update(
      _tableOrders,
      order.toMap(includeId: true),
      where: 'id = ?',
      whereArgs: [order.id],
    );
  }

  Future<int> deleteOrder(int orderId) async {
    final db = await database;
    await db.delete(
      _tableOrderItems,
      where: 'orderId = ?',
      whereArgs: [orderId],
    );
    return await db.delete(
      _tableOrders,
      where: 'id = ?',
      whereArgs: [orderId],
    );
  }

  Future<List<Order>> getAllOrders() async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      _tableOrders,
      orderBy: 'date DESC',
    );
    return maps.map((map) => Order.fromMap(map)).toList();
  }

  Future<Order?> getOrderById(int id) async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      _tableOrders,
      where: 'id = ?',
      whereArgs: [id],
    );
    if (maps.isEmpty) return null;
    return Order.fromMap(maps.first);
  }

  Future<List<Order>> getOrdersByDate(DateTime date) async {
    final db = await database;
    final dateStr = date.toIso8601String().split('T')[0];
    final List<Map<String, dynamic>> maps = await db.query(
      _tableOrders,
      where: 'date LIKE ?',
      whereArgs: ['$dateStr%'],
    );
    return maps.map((map) => Order.fromMap(map)).toList();
  }

  Future<int> insertProduct(Product product) async {
    final db = await database;
    return await db.insert(_tableProducts, product.toMap());
  }

  Future<int> updateProduct(Product product) async {
    final db = await database;
    return await db.update(
      _tableProducts,
      product.toMap(includeId: true),
      where: 'id = ?',
      whereArgs: [product.id],
    );
  }

  Future<int> deleteProduct(int productId) async {
    final db = await database;
    return await db.delete(
      _tableProducts,
      where: 'id = ?',
      whereArgs: [productId],
    );
  }

  Future<List<Product>> getAllProducts() async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      _tableProducts,
      orderBy: 'name ASC',
    );
    return maps.map((map) => Product.fromMap(map)).toList();
  }

  Future<Product?> getProductById(int id) async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      _tableProducts,
      where: 'id = ?',
      whereArgs: [id],
    );
    if (maps.isEmpty) return null;
    return Product.fromMap(maps.first);
  }

  Future<int> insertOrderItem(OrderItem orderItem) async {
    final db = await database;
    return await db.insert(_tableOrderItems, orderItem.toMap());
  }

  Future<int> updateOrderItem(OrderItem orderItem) async {
    final db = await database;
    return await db.update(
      _tableOrderItems,
      orderItem.toMap(includeId: true),
      where: 'id = ?',
      whereArgs: [orderItem.id],
    );
  }

  Future<int> deleteOrderItem(int orderItemId) async {
    final db = await database;
    return await db.delete(
      _tableOrderItems,
      where: 'id = ?',
      whereArgs: [orderItemId],
    );
  }

  Future<List<OrderItem>> getOrderItemsByOrderId(int orderId) async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      _tableOrderItems,
      where: 'orderId = ?',
      whereArgs: [orderId],
    );
    return maps.map((map) => OrderItem.fromMap(map)).toList();
  }

  Future<Map<String, dynamic>> getOrderWithItems(int orderId) async {
    final order = await getOrderById(orderId);
    if (order == null) return {};

    final orderItems = await getOrderItemsByOrderId(orderId);
    final List<Map<String, dynamic>> itemsWithProducts = [];
    double totalPrice = 0;

    for (var orderItem in orderItems) {
      final product = await getProductById(orderItem.productId);
      if (product != null) {
        final itemTotal = product.price * orderItem.quantity;
        totalPrice += itemTotal;
        itemsWithProducts.add({
          'orderItem': orderItem,
          'product': product,
          'itemTotal': itemTotal,
        });
      }
    }

    return {
      'order': order,
      'items': itemsWithProducts,
      'totalPrice': totalPrice,
    };
  }

  Future<void> deleteDatabase() async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, _dbName);
    await databaseFactory.deleteDatabase(path);
    _database = null;
  }
}
