package runner;

import creator.AccountCreator;
import model.Account;
import service.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ApplicationRunner {
    public static void run() {
        //Создадим список доступных аккаунтов:
        AccountCreator accountCreator = new AccountCreator("parsingfile.txt");
        List<Account> accounts = accountCreator.getAccounts();
        for (Account account : accounts) {
            System.out.println(account);
        }
        //Сколько пар аккаунтов (транзакций) хотим гонять:
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите количество пар счетов, с которыми хотите работать : ");
        int countOfTransactions = scanner.nextInt();

        //Создадим список транзакций.
        List<Transaction> transactions = new ArrayList<>();

        //Сколько циклов будет крутиться каждая транзакция:
        System.out.print("Введите количество циклов переводов (по условию 30) : ");
        int countOfLatches = scanner.nextInt();
        CountDownLatch countDownLatch = new CountDownLatch(countOfLatches);

        //Сколько потоков примем для работы:
        System.out.print("Введите количество потоков для работы : ");
        int howManyTreads = scanner.nextInt();

        //Заполним список транзакциями:
        for (int i = 0; i < countOfTransactions; i++) {
            System.out.println("Введите SimpleID счета, с которого будете переводить : ");
            int withdrawingAccountID = scanner.nextInt();
            System.out.println("Введите SimpleID счета, на который будете переводить : ");
            int depositingAccountID = scanner.nextInt();
            transactions.add(new Transaction(accounts.get(withdrawingAccountID - 1), accounts.get(depositingAccountID - 1), countDownLatch));
        }

        //Засечем время начала:
        long startTime2 = System.currentTimeMillis();

        //Создадим пул потоков и распределим задачи:
        ExecutorService service = Executors.newFixedThreadPool(howManyTreads);
        for (int i = 0; i < countOfLatches; i++) {
            for (Transaction transaction : transactions) {
                service.submit(transaction); //Нарезаем потокам задачи.
            }
        }
        service.shutdown(); //Отправляем потоки в работу.


        //Ждем, пока отсчитаются защелки (наше количество циклов):
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Ждем завершения работы пула потоков.
        try {
            if (service.awaitTermination(20, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }

        long finishTime2 = System.currentTimeMillis();
        //Количество времени работы потоков:
        System.out.println("Времени заняло : " + (finishTime2 - startTime2) + "\n");

        //Вывод всех аккаунтов для наглядности и суммы денег на всех счетах:
        int result = 0;
        for (Account account : accounts) {
            System.out.println(account);
            result += account.getMoney();
        }
        System.out.println("\nСумма денег на всех аккаунтах = " + result);
    }
}
