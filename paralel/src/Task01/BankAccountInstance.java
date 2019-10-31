package Task01;
/**
 * BankAccountInstance
 */

import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class BankAccountInstance {

    // banck account model

    private static Integer classId = 1;
    private Semaphore semaphore = new Semaphore(1, true);
    //semaphore is userful for locking the functions locking every instance of bank account
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
    }

    boolean lockBankAccountTransaction(String operationName, String destinationAccountName, Integer sum, String sign) throws InterruptedException {
        // having some prints in the console
        //locking the function
        System.out.println("try to lock  on :" + this.accountName);
        semaphore.acquire();
        System.out.println("lock aquired on :" + this.accountName);
        //checking if we have money
        if (sign.equals("-")) {
            if (this.balance < sum) {
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
        semaphore.release();
        System.out.println("lock released on : " + this.accountName);
        return true;
    }

    boolean lockHasBalanceEnough(Integer sum) throws InterruptedException {
        //lock and check if the account has enough money
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
        this.logs.add(operationName + "," + destAccountName + ","
                + this.getAccountName() + "," + sum + "," + this.balance + '\n');

    }

    void appendOperationToLogDecrement(String operationName, String destAccountName, Integer sum) {
        this.logs.add(operationName + "," + this.getAccountName()
                + "," + destAccountName + "," + sum + "," + this.balance + '\n');

    }

    void appendOperationToLogInssufficentFunds(String operationName, String destAccountName, Integer sum) {
        this.logs.add("Insufficent money " + sum + " to transfer from " + this.getAccountName() + " to "
                + destAccountName + " ----remaining money " + this.balance + '\n');

    }


    public String getLockAudit()  {
        //seeing on the log o f each account if there has been ocurring errors
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.getMessage();
        }
        StringBuilder pString = new StringBuilder("For account " + this.getAccountName() + " thees are not consistent: \n");
        int startingSum = 100000;
        for (int i=0;i<this.getLogs().size();i++) {
            //spliting every log and check if the sum is equal with the startingSum,for which we have applied some operation
            String operationLog = this.getLogs().get(i);
            String[] arrOfStr = operationLog.split(",");
            String operationName  = arrOfStr[0];
            String sourceAccount  = arrOfStr[1];
            String destAccount  = arrOfStr[2];
            String sum  = arrOfStr[3];
            String remainingMoney  = arrOfStr[4].replace("\n","");


            if(sourceAccount.equals(this.getAccountName()))
            { // we are sending money
                startingSum -= Integer.parseInt(sum);
            }else{
                //we are receving money
                startingSum += Integer.parseInt(sum);
            }

            if(!String.valueOf(startingSum).equals(remainingMoney))
            {
                pString.append(operationName.toString()).append("\n");
            }
        }

        pString.append("-------------------------- END------------------------");
        semaphore.release();
        return pString.toString();
    }

    //adding and substrating the balance if it is the case
    void addBalance(Integer sum) {
        this.balance += sum;
    }

    void substractBalance(Integer sum) {
        this.balance -= sum;
    }

    //getting the logs
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