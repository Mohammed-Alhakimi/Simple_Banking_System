package banking.carddao;

import banking.account.Account;
import banking.card.Card;
import org.junit.*;
import java.sql.*;


import static org.junit.Assert.*;

public class CardDAOTest {

    Connection connection;

    @Before
    public void setUp() {
        connection = CardDAO.connect();
    }

    @Test
    public void connect() {
        boolean connected = false;
        try {
            connected = connection.isValid(5);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assertTrue(connected);
    }

    @After
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createTableTest() {
        try (Statement statement = connection.createStatement()) {
            String query = " CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER  NOT NULL PRIMARY KEY," +
                    "number TEXT, " +
                    "pin TEXT, " +
                    "balance INTEGER DEFAULT 0); ";
            int i = statement.executeUpdate(query);
            System.out.println(i + " Affected rows");
        } catch (SQLException throwables) {
            System.out.println("Table already exists");
        }
    }

    @Test
    public void addEntryToDataBase() {
        Account account = new Account(1000,
                new Card("4000003429795087", "1234"));
        int i = CardDAO.addCard(account);
        assertEquals(1, i);
    }

    @Test
    public void getEntryFromTable() {
        String cardNumber = "4000003429795087";
        String pin = "1234";
        Account account = CardDAO.logIn(cardNumber, pin);
        assertNotNull(account);
    }

}