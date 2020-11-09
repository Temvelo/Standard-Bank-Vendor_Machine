package za.co.standardbank.vendingmachine.util;

import java.util.Random;

public class CommonUtils {

    private CommonUtils() {

    }

    public static String getRandomCode() {
        Random r = new Random();
        return String.valueOf((char) (r.nextInt(26) + 'A'));
    }

    public static char getRandomPrice() {
        Random r = new Random();
        return (char) (r.nextInt(20));
    }

    public static char getQuantity() {
        Random r = new Random();
        return (char) (r.nextInt(4));
    }
}
