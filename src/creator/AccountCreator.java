package creator;

import model.Account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountCreator {
    private final String fileName;

    public AccountCreator(String fileName) {
        this.fileName = fileName;
    }

    public List<Account> getAccounts() {
        return createAccounts();
    }

    private List<Account> createAccounts() {
        return getReadableIds().stream()
                .map(readableId -> new Account(UUID.randomUUID(), readableId))
                .collect(Collectors.toList());
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
        String[] strings = line.split(";");
        String lastString = strings[strings.length - 1];
        return strings[1]
                .concat(strings[0])
                .concat(lastString.substring(lastString.length() - 2));
    }

}
