package banking.carddao;

import banking.account.Account;
import banking.card.Card;
import org.sqlite.SQLiteDataSource;

import java.io.Serializable;
import java.sql.*;
import java.util.Objects;

/**
 * Based on the Data Access Object Design pattern where encapsulates all the methods of
 * interacting with the database in an object
 */
public class CardDAO implements Serializable {
    private static String dbStringURL;
    private static final SQLiteDataSource dataSource;


    public static void setDbStringURL(String fileName) {
        CardDAO.dbStringURL = "jdbc:sqlite:" + fileName;
    }

    static {
        dbStringURL = "jdbc:sqlite:C:\\Users\\Moham\\OneDrive\\Desktop\\Simple Banking System" +
                "\\Simple Banking System\\task\\bank.db";
        dataSource = new SQLiteDataSource();
    }

    /**
     * @return Returns a Valid connection with the database
     */
    public static Connection connect() {
        dataSource.setUrl(dbStringURL);
        try {
            return dataSource.getConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a table called card with
     */
    public static void createTableCard() {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                String query = " CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER NOT NULL PRIMARY KEY," +
                        "number TEXT(16), " +
                        "pin TEXT(4), " +
                        "balance INTEGER DEFAULT 0); ";
                statement.executeUpdate(query);
            }
        } catch (SQLException throwables) {
            System.out.println("Table Already exists");
        }
    }

    /**
     * @param account Takes an account object and inserts an entry to the database from its
     *                fields (Current Card and Balance)
     * @return returns 1 in case the card was added and zero in case it's not
     */
    public static int addCard(Account account) {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                String query = " INSERT INTO card (number,pin,balance) VALUES " +
                        "(" + account.getCurrentCard().getCardNumber() + "," +
                        account.getCurrentCard().getPin() + "," +
                        account.getBalance() + ")";
                return statement.executeUpdate(query);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    /**
     * @return returns an account object from the database or null if the account isn't found
     */
    public static Account logIn(String cardNumber, String pin) {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                Account account = new Account();
                String query = "SELECT * FROM card " +
                        "WHERE number = '" + cardNumber + "' " +
                        "AND pin = '" + pin + "';";
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                String cardNumberResult = resultSet.getString(2);
                String cardPinResult = resultSet.getString(3);
                int balance = resultSet.getInt(4);
                account.setBalance(balance);
                account.getCurrentCard().setCardNumber(cardNumberResult);
                account.getCurrentCard().setPin(cardPinResult);
                return account;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * @param account Used to get the card number and execute it in the query
     */
    public static void addIncome(Account account, String incomeToAdd) {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            String query = "UPDATE card SET balance = (?) WHERE number = (?)";
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, account.getBalance() + Integer.parseInt(incomeToAdd));
                statement.setString(2, account.getCurrentCard().getCardNumber());
                statement.executeUpdate();
                connection.commit();
                System.out.println("Income was added!\n");
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getBalance(Account account) {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            String query = "SELECT balance FROM card WHERE number = (?) AND pin = (?)";
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, account.getCurrentCard().getCardNumber());
                statement.setString(2, account.getCurrentCard().getPin());
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return Integer.toString(resultSet.getInt(1));
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static Account checkAccountToTransfer(String cardNumber) {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            String query = "SELECT * FROM card WHERE number = (?)";
            connection.setAutoCommit(false);
            Account account = new Account();
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cardNumber);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                String cardNumberResult = resultSet.getString(2);
                String cardPinResult = resultSet.getString(3);
                int balance = resultSet.getInt(4);
                account.setBalance(balance);
                account.getCurrentCard().setCardNumber(cardNumberResult);
                account.getCurrentCard().setPin(cardPinResult);
                return account;
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void transferAmounts(Account from, Account accountToTransferTo, int amountToTransfer) {

        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            String updateAccount = "UPDATE card SET balance = (?) WHERE number = (?)";
            connection.setAutoCommit(false);
            try (PreparedStatement updateStatement = connection.prepareStatement(updateAccount)) {
                String balanceAccount1 = CardDAO.getBalance(from);
                assert balanceAccount1 != null;
                if (Integer.parseInt(balanceAccount1) < amountToTransfer) {
                    System.out.println("Not enough money!\n");
                } else {
                    Savepoint savepoint = connection.setSavepoint();
                    updateStatement.setInt(1, Integer.parseInt(Objects.requireNonNull(CardDAO.getBalance(from))) - amountToTransfer);
                    updateStatement.setString(2, from.getCurrentCard().getCardNumber());
                    int affectedRows = updateStatement.executeUpdate();
                    if (affectedRows == 1) {
                        updateStatement.setInt(1, Integer.parseInt(Objects.requireNonNull(CardDAO.getBalance(accountToTransferTo))) + amountToTransfer);
                        updateStatement.setString(2, accountToTransferTo.getCurrentCard().getCardNumber());
                        int affectedRowsAddition = updateStatement.executeUpdate();
                        if (affectedRowsAddition == 1) {
                            connection.commit();
                            System.out.println("Success!\n");
                        } else {
                            connection.rollback(savepoint);
                        }
                    } else {
                        connection.rollback(savepoint);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void closeAccount(Account account) {
        try (Connection connection = CardDAO.connect()) {
            assert connection != null;
            String query = "DELETE FROM card WHERE number = (?)";
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, account.getCurrentCard().getCardNumber());
                int i = statement.executeUpdate();
                if (i == 1) {
                    connection.commit();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
