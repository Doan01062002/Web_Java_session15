package com.example.session15.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private Long id;
    private String orderId;
    private String productId;
    private int quantity;
    private double currentPrice;
}