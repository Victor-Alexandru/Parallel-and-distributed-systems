import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * ThreadOperation
 */
public class ThreadOperation implements Runnable {
        
       BankAccountInstance b1;
       BankAccountInstance b2;
       Integer transferSum;
       // our Semaphore is same as a Mutex.
       // official doc :
       // Semaphores in java are like Monitors and locks that provide mutual
       // exclusivity to multiple threads and make threads wait on certain condition to
       // be true.
       // static Semaphore semaphore = new Semaphore(1, true);
       Operation transferOperation;
       String threadName;

       public ThreadOperation(String threadName, BankAccountInstance b1, BankAccountInstance b2, Integer transferSum,
                     Integer operationId) {
              this.threadName = threadName;
              this.b1 = b1;
              this.b2 = b2;
              this.transferSum = transferSum;
              this.transferOperation = new Operation(operationId);
       }

       @Override
       public void run()  {
              // TODO Auto-generated method stub
              // TODO Auto-generated method stub
              // try {  
              //        semaphore.acquire();
              //        System.out.println("--with mutexex--Thread " + this.threadName + ": Operation "
              //                      + this.transferOperation.getUniqueId() + " on accounts " + this.b1.getAccountName()+ ":" + this.b2.getAccountName() + " with the sum " + this.transferSum);
              //        this.transferOperation.transfer(this.b1, this.b2, this.transferSum);
              //        semaphore.release();
              // } catch (InterruptedException e) {
              //        // TODO Auto-generated catch block
              //        e.printStackTrace();
              // }

              // without mutex
       

              
              System.out.println("--no mutexex--Thread " + this.threadName + ": Operation " +
              this.transferOperation.getUniqueId()
              + " on accounts " + this.b1.getAccountName() + ":" + this.b2.getAccountName()
              + " with the sum " + this.transferSum );
              
              Integer sum = this.transferSum;

              if (b1.getBalance() < sum) {
                     b1.appendOperationToLogInssufficentFunds(this.transferOperation.getUniqueId().toString(), b2, sum);
              } else {
                     b1.substractBalance(sum);
                     b2.addBalance(sum);
                     b2.appendOperationToLogTransfer(this.transferOperation.getUniqueId().toString(), b1, sum);
                     b1.appendOperationToLogDecrement(this.transferOperation.getUniqueId().toString(), b2, sum);
                     this.transferOperation.incrementUniqueId();

              }

       }

}