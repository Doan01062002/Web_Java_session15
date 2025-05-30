package com.example.session15.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long idCart;
    private String idUser;
    private String idProduct;
    private int quantity;
}