package za.co.standardbank.vendingmachine.model;

import lombok.Data;
import za.co.standardbank.vendingmachine.enums.ECoin;
import za.co.standardbank.vendingmachine.exception.NotFoundException;
import za.co.standardbank.vendingmachine.exception.NotSufficientChangeException;
import za.co.standardbank.vendingmachine.exception.SoldOutException;

import java.util.*;

@Data
public class VendingMachine {
    private List<Product> inventoryList = new ArrayList<>();
    private final EnumMap<ECoin, Integer> machineCoins = new EnumMap<>(ECoin.class);

    public Product getProductByCode(String productCode) {
        Optional<Product> productOptional = inventoryList.stream()
                .filter(inventory -> inventory.getCode().equalsIgnoreCase(productCode))
                .findFirst();
        if (!productOptional.isPresent())
            throw new NotFoundException(String.format("Invalid Product %s", productCode));

        Product product = productOptional.get();

        if (product.getQuantity() < 1)
            throw new SoldOutException(String.format("Product %s Sold Out.", productCode));
        return product;
    }

    public boolean hasInSufficientChange(double balance, double productPrice) {
        return !hasSufficientChangeForAmount(balance - productPrice);
    }

    public List<ECoin> collectChange(double balance) {
        List<ECoin> change = getChange(balance);
        updateMachineCoins(change);
        return change;
    }

    private void updateMachineCoins(List<ECoin> change) {
        change.forEach(coin -> {
            int count = machineCoins.get(coin);
            machineCoins.put(coin, count - 1);
            deductCoin(coin);
        });
    }

    public void reduceProductQuantity(Product product) {
        if (!hasProduct(product.getCode())) return;
        product.reduceQuantity(1);
    }

    public void deductCoin(ECoin coin) {
        if (hasCoin(coin)) {
            int count = machineCoins.get(coin);
            machineCoins.put(coin, count - 1);
        }
    }

    private List<ECoin> getChange(double amount) {
        if (amount <= 0) return Collections.emptyList();

        List<ECoin> changes = new ArrayList<>();
        double balance = amount;
        while (balance > 0) {
            if (balance >= ECoin.FIVE_RANDS.getDenomination() && hasCoin(ECoin.FIVE_RANDS)) {
                changes.add(ECoin.FIVE_RANDS);
                balance = balance - ECoin.FIVE_RANDS.getDenomination();
            } else if (balance >= ECoin.TWO_RANDS.getDenomination()
                    && hasCoin(ECoin.TWO_RANDS)) {
                changes.add(ECoin.TWO_RANDS);
                balance = balance - ECoin.TWO_RANDS.getDenomination();

            } else if (balance >= ECoin.ONE_RAND.getDenomination()
                    && hasCoin(ECoin.ONE_RAND)) {
                changes.add(ECoin.ONE_RAND);
                balance = balance - ECoin.ONE_RAND.getDenomination();
            } else if (balance >= ECoin.FIFTY_CENTS.getDenomination()
                    && hasCoin(ECoin.FIFTY_CENTS)) {
                changes.add(ECoin.FIFTY_CENTS);
                balance = balance - ECoin.FIFTY_CENTS.getDenomination();
            } else {
                throw new NotSufficientChangeException("NotSufficientChange,Please try another product");
            }
        }
        return changes;
    }

    private boolean hasSufficientChangeForAmount(double amount) {
        try {
            getChange(amount);
            return true;
        } catch (NotSufficientChangeException ex) {
            return false;
        }
    }

    private boolean hasCoin(ECoin coin) {
        Optional<Integer> productOptional = Optional.ofNullable(machineCoins.get(coin));
        if (!productOptional.isPresent())
            throw new NotFoundException(String.format("Invalid Coin %s", coin));
        return productOptional.get() > 0;
    }

    private boolean hasProduct(String productCode) {
        Optional<Product> productOptional = Optional.ofNullable(getProductByCode(productCode));
        return productOptional.filter(product -> product.getQuantity() > 0).isPresent();
    }


}
