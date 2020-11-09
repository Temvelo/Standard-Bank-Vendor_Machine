package za.co.standardbank.vendingmachine.model;

import lombok.Data;
import za.co.standardbank.vendingmachine.enums.ECoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Cart {
    private Product selectedProduct;
    private List<ECoin> coins = new ArrayList<>();
    private List<ECoin> change;
    private double balance;

    public Cart(Product product, List<ECoin> coins) {
        this.selectedProduct = product;
        this.coins = coins;
    }

    public void addCredit(ECoin coin) {
        if (Objects.isNull(coin)) return;
        this.coins.add(coin);
        this.setBalance(this.balance + coin.getDenomination());
    }

    public void deductProductPriceFromBalance() {
        if (balance <= 0 && Objects.isNull(selectedProduct)) return;
        this.setBalance(this.balance - selectedProduct.getPrice());
    }
}
