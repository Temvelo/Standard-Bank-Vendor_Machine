package za.co.standardbank.vendingmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.co.standardbank.vendingmachine.service.VendingMachineService;

@SpringBootApplication
public class VendingMachineApplication {

	private VendingMachineService vendingMachineService;
	public static void main(String[] args) {
		SpringApplication.run(VendingMachineApplication.class, args);


	}

}
