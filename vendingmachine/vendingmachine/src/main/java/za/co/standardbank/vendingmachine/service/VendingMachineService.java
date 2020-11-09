package za.co.standardbank.vendingmachine.service;

import za.co.standardbank.vendingmachine.enums.ECoin;
import za.co.standardbank.vendingmachine.model.Cart;
import za.co.standardbank.vendingmachine.model.Product;

import java.util.List;
import java.util.Map;

public interface VendingMachineService {

    Map<String, String> displayProducts();

    Map<String, String> selectProduct(String productCode);

    Map<String, String> resetMachine();

    Map<String, String> insertCoin(ECoin coin);

    Map<String, String> cancel();

    List<Product> getInventoryList();

    Cart getCurrentCart();

}
