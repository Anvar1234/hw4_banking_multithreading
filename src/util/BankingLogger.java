package util;

import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BankingLogger {
    static {
        try(FileInputStream in = new FileInputStream("log.properties")){
            LogManager.getLogManager().readConfiguration(in);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static Logger log(String className) {
        return Logger.getLogger(className);
    }
}
