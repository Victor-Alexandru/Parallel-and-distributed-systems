package Task01;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Audit implements Runnable {

    static Semaphore semaphore = new Semaphore(1, true);

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
        try {
            semaphore.acquire();
            System.out.println("Audit start");
            for (BankAccountInstance b : this.bankAccounts) {
                writer.write(b.getLogs().toString());
            }
            writer.close();
            System.out.println("Audit end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }


    public void auditABankAccount(BankAccountInstance b) {
//        int start = 100000;
//        for(String s : b.getLogs())
//        {
//            if(s.contains("[A]->"))
//        }

    }
}
