package banking;

import banking.account.Bank;
import banking.carddao.CardDAO;

import com.beust.jcommander.*;
import serialization.SerializationUtils;
import java.io.IOException;


public class Main {
    @Parameter(names = {"-fileName", "-f"}, description = "Specifies the file name for the database")
    private String file;
    public static void main(String[] args) {
        Main main = new Main();
        JCommander
                .newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        Bank bank;

            CardDAO.setDbStringURL(main.getFile());
            CardDAO.createTableCard();

        try {
            bank =(Bank) SerializationUtils.deserialize("BankData.txt");
        } catch (IOException | ClassNotFoundException e) {
            bank = new Bank();
        }

        bank.showActions();
        try {
            SerializationUtils.serialize(bank,"BankData.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getFile() {
        return file;
    }
}
