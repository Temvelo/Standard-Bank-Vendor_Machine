package za.co.standardbank.vendingmachine.model;

import lombok.Data;

@Data
public class Product {
    private String code;
    private double price;
    private int quantity;

    public Product(String code, double price, int quantity) {
        this.code = code;
        this.price = price;
        this.quantity = quantity;
    }

    public void reduceQuantity(int count) {
        if (this.quantity > 0) {
            setQuantity(this.quantity - count);
        }
    }
}
