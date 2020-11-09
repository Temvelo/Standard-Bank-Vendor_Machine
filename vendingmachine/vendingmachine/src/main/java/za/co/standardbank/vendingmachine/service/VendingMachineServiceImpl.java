package za.co.standardbank.vendingmachine.service;

import org.springframework.stereotype.Service;
import za.co.standardbank.vendingmachine.enums.ECoin;
import za.co.standardbank.vendingmachine.exception.NotSufficientChangeException;
import za.co.standardbank.vendingmachine.model.Cart;
import za.co.standardbank.vendingmachine.model.Product;
import za.co.standardbank.vendingmachine.model.VendingMachine;

import javax.annotation.PostConstruct;
import java.util.*;

import static za.co.standardbank.vendingmachine.util.CommonUtils.*;

@Service
public class VendingMachineServiceImpl implements VendingMachineService {
    private VendingMachine vendingMachine;
    private Cart cart;
    private Product selectedProduct;

    @PostConstruct
    public void init() {
        initialize();
    }

    @Override
    public Map<String, String> displayProducts() {
        return constructResponse(String.format("Products Available %s", vendingMachine.getInventoryList()));
    }

    @Override
    public Map<String, String> selectProduct(String productCode) {
        this.selectedProduct = vendingMachine.getProductByCode(productCode);

        if (Objects.isNull(cart)) {
            return constructResponse(String.format("Selected Product is %s Costs %s", this.selectedProduct.getCode(), this.selectedProduct.getPrice()));
        }
        cart.setSelectedProduct(this.selectedProduct);
        return checkoutIfPayedInFull();
    }

    @Override
    public Map<String, String> insertCoin(ECoin coin) {
        if (Objects.isNull(cart)) {
            this.cart = new Cart(this.selectedProduct, new ArrayList<>());
        }
        cart.addCredit(coin);
        if (Objects.isNull(selectedProduct)) {
            return constructResponse(String.format("Balance is %s, Please Select Product", cart.getBalance()));
        }
        return checkoutIfPayedInFull();
    }

    private Map<String, String> checkoutIfPayedInFull() {
        double remainingBalance = cart.getSelectedProduct().getPrice() - cart.getBalance();
        if (remainingBalance > 0) {
            return constructResponse(String.format("Balance is %s. Remaining Balance %s", cart.getBalance(), remainingBalance));
        }
        return checkOutCartAndGetChange();
    }

    private Map<String, String> checkOutCartAndGetChange() {
        collectProduct();
        cart.setChange(getChange());
        Map<String, String> response = constructResponse(String.format("Thank you, your change is  %s Coins %s", cart.getBalance(), cart.getChange()));
        clear();
        return response;
    }

    private List<ECoin> getChange() {
        return vendingMachine.collectChange(cart.getBalance());
    }

    @Override
    public Map<String, String> resetMachine() {
        initialize();
        return constructResponse("Machine Reset Successful");
    }

    @Override
    public Map<String, String> cancel() {
        if (Objects.isNull(cart)) return constructResponse("Thank you. Cancel Successful");
        Map<String, String> response = constructResponse(String.format("Thank you, your refund is  %s Coins %s", cart.getBalance(), cart.getCoins()));
        clear();
        return response;
    }

    @Override
    public List<Product> getInventoryList() {
        return vendingMachine.getInventoryList();
    }

    @Override
    public Cart getCurrentCart() {
        return cart;
    }

    private void clear() {
        this.selectedProduct = null;
        this.cart = null;
    }

    private void collectProduct() {
        if (vendingMachine.hasInSufficientChange(cart.getBalance(), cart.getSelectedProduct().getPrice())) {
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");
        }
        vendingMachine.reduceProductQuantity(cart.getSelectedProduct());
        cart.deductProductPriceFromBalance();
    }

    private void initialize() {
        vendingMachine = new VendingMachine();
        for (ECoin coin : ECoin.values()) {
            vendingMachine.getMachineCoins().put(coin, 4);
        }

        for (int i = 0; i < 5; i++) {
            vendingMachine.getInventoryList().add(new Product(getRandomCode(), getRandomPrice(), getQuantity()));
        }
    }

    private Map<String, String> constructResponse(String value) {
        Map<String, String> data = new HashMap<>();
        data.put("message", value);
        return data;
    }

}
