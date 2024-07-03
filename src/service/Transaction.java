package service;

import model.Account;
import model.NotEnoughMoneyException;
import util.BankingLogger;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
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
        logger.info(String.format("Баланс счета списания " + withdrawingAccount.getReadableID()
                + "=" + withdrawingAccount.getMoney() + "\n"
                + "Баланс счета пополнения " + depositingAccount.getReadableID()
                + "=" + depositingAccount.getMoney() + "\n" + "Сумма списания : " + amount + "\n" + "Поток : %s%n", Thread.currentThread().getName()));
        try {
            withdrawingAccount.withdraw(amount);
            depositingAccount.deposit(amount);
        } catch (NotEnoughMoneyException e) {
            throw new NotEnoughMoneyException(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            doTransaction();
            if(logger.isLoggable(Level.FINE)) {
                logger.fine(String.format("Списание со счета " + withdrawingAccount.getReadableID()
                        + " на счет " + depositingAccount.getReadableID()
                        + " завершено в потоке %s%n", Thread.currentThread().getName()));
            }
        } catch (NotEnoughMoneyException e) {
            logger.warning("метод run: " + e.getMessage());
        } finally {
            countDownLatch.countDown();
        }
        try {
            Thread.sleep(random.nextInt(100, 501));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
