package banking.account;

import banking.card.Card;
import lombok.*;
import java.io.Serializable;

@ToString
public class Account implements Serializable {
    @Getter @Setter
    private int balance;
    @Getter @Setter
    private Card currentCard;

    public Account(int balance, Card currentCard) {
        this.balance = balance;
        this.currentCard = currentCard;
    }

    public Account() {
        this.currentCard = new Card();
        this.balance = 0;
    }
}