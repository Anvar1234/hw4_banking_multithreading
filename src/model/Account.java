package model;

import util.BankingLogger;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {
    private static int countAcconts = 0;
    private static final Logger logger = BankingLogger.log(Account.class.getName());
    private final int simpleID;
    private final UUID UUID;
    private final String readableID;
    private int money;


    public Account(java.util.UUID UUID, String readableID) {
        this.simpleID = ++countAcconts;
        this.UUID = UUID;
        this.readableID = readableID;
        this.money = 10000;
    }

    public String getReadableID() {
        return readableID;
    }

    public int getMoney() {
        return money;
    }

    public void deposit(int depositAmount) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Зачисление на счет : " + readableID
                    + ", суммы : " + depositAmount
                    + ", остаток на счете : %s%n", money));
        }
        money += depositAmount;
    }

    public void withdraw(int withdrawAmount) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Попытка списания со счета : " + readableID
                    + ", суммы : " + withdrawAmount
                    + ", остаток на счете : %s%n", money));
        }
        if (withdrawAmount > money) {
            throw new NotEnoughMoneyException(String.format("Недостаточный баланс на счете %s!%n", readableID));
        }
        money -= withdrawAmount;
    }

    @Override
    public String toString() {
        return "SimpleID=" + simpleID + ", UUID=" + UUID + ", readableID=" + readableID + ", money=" + money;
    }
}
