class OrderItem {
  final int id;
  final int orderId;
  final int productId;
  final int quantity;

  OrderItem({
    required this.id,
    required this.orderId,
    required this.productId,
    required this.quantity,
  });

  OrderItem copyWith({
    int? id,
    int? orderId,
    int? productId,
    int? quantity,
  }) {
    return OrderItem(
      id: id ?? this.id,
      orderId: orderId ?? this.orderId,
      productId: productId ?? this.productId,
      quantity: quantity ?? this.quantity,
    );
  }

  factory OrderItem.fromMap(Map<String, dynamic> map) {
    return OrderItem(
      id: map['id'] as int,
      orderId: map['orderId'] as int,
      productId: map['productId'] as int,
      quantity: map['quantity'] as int,
    );
  }

  Map<String, dynamic> toMap({bool includeId = false}) {
    final data = <String, dynamic>{
      'orderId': orderId,
      'productId': productId,
      'quantity': quantity,
    };
    if (includeId) {
      data['id'] = id;
    }
    return data;
  }
}
