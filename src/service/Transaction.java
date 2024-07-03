package service;

import model.Account;
import model.NotEnoughMoneyException;
import util.BankingLogger;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class Transaction implements Runnable {

    private final CountDownLatch countDownLatch;
    private final Account withdrawingAccount;
    private final Account depositingAccount;
    private final Random random = new Random();
    private int amount;
    private static final Logger logger = BankingLogger.log(Transaction.class.getName());

    public Transaction(Account from, Account to, CountDownLatch countDownLatch) {
        this.withdrawingAccount = from;
        this.depositingAccount = to;
        this.countDownLatch = countDownLatch;
    }

    private synchronized void doTransaction() {
        amount = random.nextInt(10000);
        withdrawingAccount.withdraw(amount);
        depositingAccount.deposit(amount);
    }

    @Override
    public void run() {
        try {
            doTransaction();
            logger.info(String.format("Списание со счета : " + withdrawingAccount.getReadableID()
                    + ", на счет : " + depositingAccount.getReadableID()
                    + ", суммы : " + amount
                    + ", прошло успешно!%n"));
        } catch (NotEnoughMoneyException e) {
            logger.warning(String.format("Списание со счета : " + withdrawingAccount.getReadableID()
                    + ", на счет : " + depositingAccount.getReadableID()
                    + ", суммы : " + amount
                    + ", невозможно! %s", e.getMessage()));
        } finally {
            countDownLatch.countDown();
        }
        try {
            Thread.sleep(random.nextInt(1000, 2001));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
