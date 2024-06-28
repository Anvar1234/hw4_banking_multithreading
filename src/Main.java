import creator.AccountCreator;
import model.Account;

public class Main {
    public static void main(String[] args) {
        AccountCreator accountCreator = new AccountCreator("parsingfile.txt");
        for(Account account : accountCreator.getAccounts())
        System.out.println(account);
    }
}