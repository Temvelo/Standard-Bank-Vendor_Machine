package za.co.standardbank.vendingmachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.co.standardbank.vendingmachine.enums.ECoin;
import za.co.standardbank.vendingmachine.exception.NotFoundException;
import za.co.standardbank.vendingmachine.exception.SoldOutException;
import za.co.standardbank.vendingmachine.model.Product;
import za.co.standardbank.vendingmachine.service.VendingMachineService;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VendingMachineServiceTests {

    @Autowired
    private VendingMachineService vendingMachineService;

    @Test
    void testWhenDisplayingProducts() {
        Map<String, String> response = vendingMachineService.displayProducts();
        String expectedMessage = "Products Available";
        String actualMessage = response.get("message");
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testWhenProductSelection() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> vendingMachineService.selectProduct("QWERT"));
        String expectedMessage = "Invalid Product";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testWhenSelectedProductIsSoldOut() {
        Optional<Product> productOptional = vendingMachineService.getInventoryList().stream().filter(product -> product.getQuantity() < 1)
                .findAny();

        if (!productOptional.isPresent()) {
            Assertions.assertTrue(true);
            return;
        }

        String code = productOptional.get().getCode();
        SoldOutException exception = assertThrows(SoldOutException.class, () -> vendingMachineService.selectProduct(code));
        String expectedMessage = "Sold Out";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testWhenSelectedProduct() {
        Optional<Product> productOptional = vendingMachineService.getInventoryList().stream().filter(product -> product.getQuantity() > 0)
                .findAny();

        if (!productOptional.isPresent()) {
            Assertions.assertTrue(true);
            return;
        }

        String code = productOptional.get().getCode();
        Map<String, String> response = vendingMachineService.selectProduct(code);
        if (Objects.isNull(vendingMachineService.getCurrentCart())) {
            String expectedMessage = "Selected Product is";
            String actualMessage = response.get("message");
            assertTrue(actualMessage.contains(expectedMessage));
            return;
        }
        assertTrue(response.get("message").contains("Balance is"));
        assertNotNull(vendingMachineService.getCurrentCart().getSelectedProduct());
    }

    @Test
    void testWhenSelectedProductIsCheckingOut() {
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);

        Optional<Product> productOptional = vendingMachineService.getInventoryList().stream()
                .filter(product -> product.getQuantity() > 0)
                .filter(product -> product.getPrice() < 20)
                .findAny();

        if (!productOptional.isPresent()) {
            Assertions.assertTrue(true);
            return;
        }

        String code = productOptional.get().getCode();
        Map<String, String> response = vendingMachineService.selectProduct(code);
        if (Objects.isNull(vendingMachineService.getCurrentCart())) {
            String expectedMessage = "Thank you, your change is";
            String actualMessage = response.get("message");
            assertTrue(actualMessage.contains(expectedMessage));
            return;
        }
        assertTrue(response.get("message").contains("Balance is"));
        assertNotNull(vendingMachineService.getCurrentCart().getSelectedProduct());
    }

    @Test
    void testWhenCancellingWithNoChange() {
        Optional<Product> productOptional = vendingMachineService.getInventoryList().stream().filter(product -> product.getQuantity() > 0)
                .findAny();

        if (!productOptional.isPresent()) {
            Assertions.assertTrue(true);
            return;
        }
        String code = productOptional.get().getCode();
        vendingMachineService.selectProduct(code);
        Map<String, String> response = vendingMachineService.cancel();
        assertTrue(response.get("message").contains("Thank you. Cancel Successful"));
    }

    @Test
    void testWhenCancellingWithChange() {
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        Map<String, String> response = vendingMachineService.cancel();
        String value = response.get("message");
        assertTrue(value.contains("Thank you, your refund is"));
        assertTrue(value.contains(Collections.singletonList(ECoin.FIVE_RANDS).toString()));
    }


    @Test
    void testWhenEnoughMoneyIsInsertedAndNoProductSelected() {
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);
        vendingMachineService.insertCoin(ECoin.FIVE_RANDS);

        Optional<Product> productOptional = vendingMachineService.getInventoryList().stream()
                .filter(product -> product.getQuantity() > 0)
                .filter(product -> product.getPrice() < 20)
                .findAny();

        if (!productOptional.isPresent()) {
            Assertions.assertTrue(true);
            return;
        }

        String code = productOptional.get().getCode();
        Map<String, String> response = vendingMachineService.selectProduct(code);
        if (Objects.isNull(vendingMachineService.getCurrentCart())) {
            String expectedMessage = "Thank you, your change is";
            String actualMessage = response.get("message");
            assertTrue(actualMessage.contains(expectedMessage));
            return;
        }
        assertTrue(response.get("message").contains("Balance is"));
        assertNotNull(vendingMachineService.getCurrentCart().getSelectedProduct());
    }

    @BeforeEach
    void testWhenMachineIsReset() {
        Map<String, String> response = vendingMachineService.resetMachine();
        String expectedMessage = "Machine Reset Successful";
        String actualMessage = response.get("message");
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
