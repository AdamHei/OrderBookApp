package com.example.adam.orderbook;

import java.math.BigDecimal;

class Order {

    String type;
    BigDecimal amount, price;

    public Order(String type, BigDecimal amount, BigDecimal price) {
        this.type = type;
        this.amount = amount;
        this.price = price;
    }
}
