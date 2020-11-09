package za.co.standardbank.vendingmachine.enums;

import lombok.Getter;

public enum ECoin {
    FIFTY_CENTS(0.5),
    ONE_RAND(1),
    TWO_RANDS(2),
    FIVE_RANDS(5);

    @Getter
    private final double denomination;

    ECoin(double denomination) {
        this.denomination = denomination;
    }
}
