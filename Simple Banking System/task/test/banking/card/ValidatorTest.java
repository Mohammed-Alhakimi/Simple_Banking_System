package banking.card;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void validCardNumber() {
        assertTrue(Validator.validateCardNumber("4000009455296122"));
    }
    @Test
    public void invalidCardNumber(){
        assertFalse(Validator.validateCardNumber("4000003305160035"));
    }
}