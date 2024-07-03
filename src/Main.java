import creator.AccountCreator;
import model.Account;
import service.Transaction;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        AccountCreator accountCreator = new AccountCreator("parsingfile.txt");
        List<Account> accounts = accountCreator.getAccounts();

        int howManyLatches = 5;
        CountDownLatch countDownLatch = new CountDownLatch(howManyLatches);
        for (Account account : accounts) {
            System.out.println(account);
        }
        //********************************
//        long startTime0 = System.currentTimeMillis();
//        Transaction moneyTransfer1 = new Transaction(1, accounts.get(0), accounts.get(1));
//        Transaction moneyTransfer2 = new Transaction(2, accounts.get(1), accounts.get(0));
//        System.out.println("acc0 money : " + accounts.get(0).getMoney());
//        System.out.println("acc1 money : " + accounts.get(1).getMoney());
//        for (int i = 0; i < 5; i++) {
//            moneyTransfer1.run();
//            moneyTransfer2.run();
//        }
//        long finishTime0 = System.currentTimeMillis();
//        System.out.println("money0 : " + accounts.get(0).getMoney());
//        System.out.println("money1 : " + accounts.get(1).getMoney());
//        System.out.println("sum of money : " + (accounts.get(0).getMoney() + accounts.get(1).getMoney()));
//        System.out.println("time : " + (finishTime0 - startTime0));
//
//        //-----------------------------
//        System.out.println("acc2 money : " + accounts.get(2).getMoney());
//        System.out.println("acc3 money : " + accounts.get(3).getMoney());
//        long startTime1 = System.currentTimeMillis();
//        for (int i = 0; i < 5; i++) {
//            Thread thread1 = new Thread(new Transaction(3, accounts.get(2), accounts.get(3)));
//            Thread thread2 = new Thread(new Transaction(4, accounts.get(3), accounts.get(2)));
//            thread1.start();
//            thread2.start();
//            try {
//                thread1.join();
//                thread2.join();
//            } catch (InterruptedException e) {
//                System.out.printf("%s has been interrupted", Thread.currentThread().getName());
//            }
//        }
//
//        long finishTime1 = System.currentTimeMillis();
//        System.out.println("money2 : " + accounts.get(2).getMoney());
//        System.out.println("money3 : " + accounts.get(3).getMoney());
//        System.out.println("sum of money : " + (accounts.get(2).getMoney() + accounts.get(3).getMoney()));
//        System.out.println("time : " + (finishTime1 - startTime1));


        //+++++++++++++++++++++++++++++++
        long startTime2 = System.currentTimeMillis();
        Transaction transaction3 = new Transaction(accounts.get(0), accounts.get(3), countDownLatch);
        Transaction transaction4 = new Transaction(accounts.get(1), accounts.get(2), countDownLatch);
//        System.out.println("acc0 money : " + accounts.get(0).getMoney());
//        System.out.println("acc3 money : " + accounts.get(3).getMoney());


        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < howManyLatches; i++) {
            service.submit(transaction3);
            service.submit(transaction4);
        }
        service.shutdown();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            if (service.awaitTermination(20, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }

        long finishTime2 = System.currentTimeMillis();
//        System.out.println("money0 : " + accounts.get(0).getMoney());
//        System.out.println("money3 : " + accounts.get(3).getMoney());
//        System.out.println("sum of money : " + (accounts.get(0).getMoney() + accounts.get(3).getMoney()));
        System.out.println("time : " + (finishTime2 - startTime2));

        for (Account account : accounts) {
            System.out.println(account);
        }
        int result = 0;
        for (Account account : accounts) {
            result += account.getMoney();
        }
        System.out.println("Сумма денег на всех аккаунтах = " + result);

    }
}