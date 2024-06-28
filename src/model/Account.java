package model;

import java.util.UUID;

public class Account {
    //где-то создать аккаунтКреатер, чтобы парсить и сразу создавать аккаунты.
    private UUID UUID;
    private String readableID;
    private long money; //формат должен быть типа BigDecimal что ли, для работы с точными и большими числами.

    public Account(java.util.UUID UUID, String readableID) {
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

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) { //не должно быть возможности устанавливать отрицательное значение.
        this.money = money;
    }

    @Override
    public String toString() {
        return  "UUID=" + UUID +
                ", readableID=" + readableID +
                ", money=" + money;
    }
}
