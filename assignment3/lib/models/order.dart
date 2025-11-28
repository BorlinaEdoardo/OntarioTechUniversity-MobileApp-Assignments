class Order {
  final int id;
  final double maxPrice;
  final DateTime date;

  Order({
    required this.id,
    required this.maxPrice,
    required this.date,
  });

  Order copyWith({
    int? id,
    double? maxPrice,
    DateTime? date,
  }) {
    return Order(
      id: id ?? this.id,
      maxPrice: maxPrice ?? this.maxPrice,
      date: date ?? this.date,
    );
  }

  factory Order.fromMap(Map<String, dynamic> map) {
    return Order(
      id: map['id'] as int,
      maxPrice: (map['maxPrice'] as num).toDouble(),
      date: DateTime.parse(map['date'] as String),
    );
  }

  Map<String, dynamic> toMap({bool includeId = false}) {
    final data = <String, dynamic>{
      'maxPrice': maxPrice,
      'date': date.toIso8601String(),
    };
    if (includeId) {
      data['id'] = id;
    }
    return data;
  }
}

