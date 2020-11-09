package za.co.standardbank.vendingmachine.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.standardbank.vendingmachine.enums.ECoin;
import za.co.standardbank.vendingmachine.service.VendingMachineService;

import java.util.Map;

@RestController
@RequestMapping("v1/machine")
public class VendingMachineController {
    private final VendingMachineService vendingMachineService;

    public VendingMachineController(VendingMachineService vendingMachineService) {
        this.vendingMachineService = vendingMachineService;
    }

    @GetMapping(
            value = "/products",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> displayProducts() {
        return constructOkResponse(vendingMachineService.displayProducts());
    }

    @PostMapping(
            value = "/products/{productCode}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> selectProduct(@PathVariable String productCode) {
        return constructOkResponse(vendingMachineService.selectProduct(productCode));
    }

    @PostMapping(
            value = "/coins/{coin}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> insertCoin(@PathVariable ECoin coin) {
        return constructOkResponse(vendingMachineService.insertCoin(coin));
    }

    @PostMapping(
            value = "cancel",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> cancel() {
        return constructOkResponse(vendingMachineService.cancel());
    }

    @PostMapping(
            value = "reset",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> resetMachine() {
        return constructOkResponse(vendingMachineService.resetMachine());
    }

    public <T> ResponseEntity<T> constructOkResponse(T entity) {
        return ResponseEntity.ok(entity);
    }


}
