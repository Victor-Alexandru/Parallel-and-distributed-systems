package Task01;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * ThreadOperation
 */
public class ThreadOperation implements Runnable {

    private BankAccountInstance b1;
    private BankAccountInstance b2;
    private Integer transferSum;
    // our Semaphore is same as a Mutex.
    // official doc :
    // Semaphores in java are like Monitors and locks that provide mutual
    // exclusivity to multiple threads and make threads wait on certain condition to
    // be true.
    static Semaphore semaphore = new Semaphore(1, true);
    private Operation transferOperation;
    private String threadName;
    private Boolean useMutexes;

    ThreadOperation(String threadName, BankAccountInstance b1, BankAccountInstance b2, Integer transferSum,
                    Integer operationId, Boolean useMutexes) {
        this.threadName = threadName;
        this.b1 = b1;
        this.b2 = b2;
        this.transferSum = transferSum;
        this.transferOperation = new Operation(operationId);
        this.useMutexes = useMutexes;
    }

    @Override
    public void run() {
        this.tranferFromAccounts();
    }

    private void tranferFromAccounts() {
        if (useMutexes) {
            try {
                this.transferOperation.lockTransfer(b1, b2, transferSum);
                System.out.println("--with mutexex--Thread " + this.threadName + ": Operation " +
                        this.transferOperation.getUniqueId()
                        + " on accounts " + this.b1.getAccountName() + ":" + this.b2.getAccountName()
                        + " with the sum " + this.transferSum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } else {

            System.out.println("--no mutexex--Thread " + this.threadName + ": Operation " +
                    this.transferOperation.getUniqueId()
                    + " on accounts " + this.b1.getAccountName() + ":" + this.b2.getAccountName()
                    + " with the sum " + this.transferSum);

            Integer sum = this.transferSum;

            if (b1.getBalance() < sum) {
                b1.appendOperationToLogInssufficentFunds(this.transferOperation.getUniqueId().toString(), b2.getAccountName(), sum);
            } else {
                this.transferOperation.transfer(this.b1, this.b2, this.transferSum);
            }
        }
    }

}