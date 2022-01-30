package banking.card;

import java.io.Serializable;

public class Validator implements Serializable {

    private final String cardNumber;

    /**
     * @param cardNumber takes a 15 digit card number and then uses the method of getValidCard
     *                   to calculate the checksum needed to make the card valid according to Luhn
     *                   Algorithm
     */
    public Validator(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return a valid card number according to Luhn algorithm
     */
    public Long getValidCard() {
        StringBuilder sb = new StringBuilder();
        String[] split = cardNumber.split("");
        int[] numbers = new int[15];

        for (int i = 0; i < split.length; i++) {
            numbers[i] = Integer.parseInt(split[i]);
        }
        for (int i = 0; i < numbers.length; i++) {
            if ((i + 1) % 2 != 0) {
                numbers[i] *= 2;
            }
        }
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 9) {
                numbers[i] -= 9;
            }
        }
        int sumAllNumbers = 0;
        for (int digit : numbers
        ) {
            sumAllNumbers += digit;
        }
        int modulo = sumAllNumbers % 10;
        int checkSum;
        if (modulo == 0) {
            checkSum = 0;
        } else {
            checkSum = 10 - modulo;
        }
        sb.append(cardNumber).append(checkSum);
        return Long.parseLong(sb.toString());
    }
    public static boolean validateCardNumber(String  cardNumber){
        String checkSum = cardNumber.substring(15);
        String first15Numbers = cardNumber.substring(0,15);
        String[] split = first15Numbers.split("");
        int[] numbers = new int[15];

        for (int i = 0; i < split.length; i++) {
            numbers[i] = Integer.parseInt(split[i]);
        }
        for (int i = 0; i < numbers.length; i++) {
            if ((i + 1) % 2 != 0) {
                numbers[i] *= 2;
            }
        }
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 9) {
                numbers[i] -= 9;
            }
        }
        int sumAllNumbers = 0;
        for (int digit : numbers
        ) {
            sumAllNumbers += digit;
        }
        return (sumAllNumbers + Integer.parseInt(checkSum)) % 10 == 0;
    }
}
