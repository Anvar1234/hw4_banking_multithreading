package model;

import util.BankingLogger;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {
    private static int countAcconts = 0;
    private final int simpleID;
    private UUID UUID;
    private String readableID;
    private int money;
    private static final Logger logger = BankingLogger.log(Account.class.getName());
    private final Random random = new Random();
    private final Object LOCK1 = new Object();
    private final Object LOCK2 = new Object();

    public Account(java.util.UUID UUID, String readableID) {
        this.simpleID = ++countAcconts;
        this.UUID = UUID;
        this.readableID = readableID;
        this.money = 10000;
    }

    public java.util.UUID getUUID() {
        return UUID;
    }

    public void setUUID(java.util.UUID UUID) {
        this.UUID = UUID;
    }

    public String getReadableID() {
        return readableID;
    }

    public void setReadableID(String readableID) {
        this.readableID = readableID;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) { //не должно быть возможности устанавливать отрицательное значение.
        this.money = money;
    }

    public void deposit(int depositAmount) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Зачисление на счет " + readableID + " " + depositAmount + " денег\n"));
        }
//        synchronized (LOCK1) {
        money += depositAmount;
//        }
    }

    public void withdraw(int withdrawAmount) {
//        synchronized (LOCK2) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Попытка списания со счета " + readableID + " " + withdrawAmount + " денег\n"));
        }
        if (withdrawAmount > money) {
            throw new NotEnoughMoneyException(String.format("Недостаточный баланс на счете %s!%n", readableID));
        }
        money -= withdrawAmount;
//        }
    }


    @Override
    public String toString() {
        return "SimpleID=" + simpleID + ", UUID=" + UUID + ", readableID=" + readableID + ", money=" + money;
    }
}
