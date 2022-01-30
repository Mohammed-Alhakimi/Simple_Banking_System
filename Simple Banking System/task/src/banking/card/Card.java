package banking.card;

import lombok.*;

import java.io.Serializable;
import java.util.Random;

@ToString
public class Card implements Serializable {

    @Getter @Setter
    private String cardNumber;
    @Getter @Setter
    private int id;
    private static final int idCounter = 0;
    @Getter @Setter
    private String pin;
    private final Random random = new Random();

    public Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    /**
     * When constructing a new Card without specifying the number or the pin,
     * A new Card Number and PINs are generated and immediately assigned to the card number
     * and the PIN code fields, but they are not final in case later they might get changed;
     */
    public Card() {
        this.cardNumber = generateCardNumber();
        this.pin = generatePin();
    }

    /**
     * Starts with a pin starter to avoid the problem of SQl lite that
     * automatically omits the zero in the beginning of the pin code when saving it to the database
     *
     * @return a string of the pin that doesn't start with 0
     */
    private String generatePin() {
        long pinStarter = 1000;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }
        return Long.toString(pinStarter + Long.parseLong(sb.toString()));
    }

    /**
     * This method internally generates a number og 15 digits and then passes it to the
     * validator that calculates the checksum value, and appends it to the end of the credit cards number to
     * make the full 16 digit card for the sake of simplicity now I still didn't implement a method to check
     * if the card number has been already taken or not yet
     *
     * @return returns A luhan valid card number
     */
    public String generateCardNumber() {
        long initialNumber = 400000000000000L;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        initialNumber += Long.parseLong(sb.toString());
        Validator validator = new Validator(Long.toString(initialNumber));
        return validator.getValidCard().toString();
    }
}
