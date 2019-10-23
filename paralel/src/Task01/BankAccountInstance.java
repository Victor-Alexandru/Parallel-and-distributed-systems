package Task01;
/**
 * BankAccountInstance
 */

import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class BankAccountInstance {

    private static Integer classId = 1;
    private Semaphore semaphore = new Semaphore(1, true);
    private List<String> logs;
    private int id;
    private String accountName;
    private Integer balance;

    BankAccountInstance(String name, Integer balance) {
        this.id = BankAccountInstance.classId;
        BankAccountInstance.classId = BankAccountInstance.classId + 1;
        this.logs = new ArrayList<>();
        this.accountName = name;
        this.balance = balance;
        this.logs.add(this.accountName + "\n");
    }

    boolean lockBankAccountTransaction(String operationName, String destinationAccountName, Integer sum, String sign) throws InterruptedException {

        System.out.println("try to lock  on :" + this.accountName);
        semaphore.acquire();
        System.out.println("lock aquired on :" + this.accountName);
        if (sign.equals("-")) {
            if (this.balance < sum) {
                this.appendOperationToLogInssufficentFunds(operationName, destinationAccountName, sum);
                semaphore.release();
                return false;
            } else {
                this.balance -= sum;
                this.appendOperationToLogDecrement(operationName, destinationAccountName, sum);
            }
        } else {
            this.balance += sum;
            this.appendOperationToLogTransfer(operationName, destinationAccountName, sum);

        }
//        TimeUnit.SECONDS.sleep(3);
        semaphore.release();
        System.out.println("lock released on : " + this.accountName);
        return true;
    }

    boolean lockHasBalanceEnough(Integer sum) throws InterruptedException {
        semaphore.acquire();
        if (this.balance < sum) {
            semaphore.release();
            return false;
        } else {
            semaphore.release();
            return true;
        }
    }

    void appendOperationToLogTransfer(String operationName, String destAccountName, Integer sum) {
        this.logs.add(operationName + ": [" + destAccountName + "] -> ["
                + this.getAccountName() + "] :" + sum + " ----remaining money " + this.balance + '\n');

    }

    void appendOperationToLogDecrement(String operationName, String destAccountName, Integer sum) {
        this.logs.add(operationName + ": [" + this.getAccountName()
                + "] -> [" + destAccountName + "] :" + sum + "----remaining money " + this.balance + '\n');

    }

    void appendOperationToLogInssufficentFunds(String operationName, String destAccountName, Integer sum) {
        this.logs.add("Insufficent money " + sum + " to transfer from " + this.getAccountName() + " to "
                + destAccountName + " ----remaining money " + this.balance + '\n');

    }

    void addBalance(Integer sum) {
        this.balance += sum;
    }

    void substractBalance(Integer sum) {
        this.balance -= sum;
    }

    List<String> getLogs() {
        return this.logs;
    }

    String getAccountName() {
        return this.accountName;
    }

    Integer getBalance() {
        return this.balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}