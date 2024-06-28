package creator;

import model.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AccountCreator {
    private final String fileName;

    public AccountCreator(String fileName) {
        this.fileName = fileName;
    }

    public List<Account> getAccounts() {
        return createAccounts();
    }

    private List<Account> createAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (String readableId : getReadableIds()) {
            accounts.add(new Account(UUID.randomUUID(), readableId));
        }
        return accounts;
    }

    private List<String> getReadableIds() {
        List<String> readableIds = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                readableIds.add(lineParser(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readableIds;
    }

    private String lineParser(String line) {
        String[] strings = line.split(";"); //убирать последнюю ";" не нужно, так как метод split в Java по умолчанию удаляет пустые строки в конце.
        System.out.println(Arrays.toString(strings));
        return strings[1]
                .concat(strings[0])
                .concat(strings[strings.length - 1].substring(8));
    }

}
