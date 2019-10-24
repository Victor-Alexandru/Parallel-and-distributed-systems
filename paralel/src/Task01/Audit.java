package Task01;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Audit implements Runnable {


    private List<BankAccountInstance> bankAccounts;
    private PrintWriter writer;

    Audit(List<BankAccountInstance> bankAccounts, String filename) {
        try {
            this.writer = new PrintWriter(filename, StandardCharsets.UTF_8);
            this.bankAccounts = bankAccounts;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        System.out.println("Audit start");

        for (BankAccountInstance b1 : this.bankAccounts) {
            String a = b1.getLockAudit();
            writer.write(a);
        }
        writer.close();
        System.out.println("Audit end");

    }


}
