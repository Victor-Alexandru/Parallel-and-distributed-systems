import java.util.Random;
import java.util.concurrent.Semaphore;

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
       static Semaphore semaphore = new Semaphore(1, true);
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
       public void run() {
              // TODO Auto-generated method stub
              try {
                     semaphore.acquire();
                     System.out.println("Thread " + this.threadName + ": Operation "
                                   + this.transferOperation.getUniqueId() + " on accounts " + this.b1.getAccountName()+ ":" + this.b2.getAccountName() + " with the sum " + this.transferSum);
                     this.transferOperation.transfer(this.b1, this.b2, this.transferSum);
                     semaphore.release();
              } catch (InterruptedException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }

              // Random r = new Random();
              // // if (r.nextInt(1000) % 2 == 0) {
              // // try {
              // // System.out.println("Thread "+this.threadName+" is stopped for 2 seconds
              // --");
              // // Thread.sleep(2000);
              // // } catch (InterruptedException e) {
              // // // TODO Auto-generated catch block
              // // e.printStackTrace();
              // // }
              // // }

              // without mutex
              // System.out.println("Thread " + this.threadName + ": Operation " +
              // this.transferOperation.getUniqueId()
              // + " on accounts " + this.b1.getAccountName() + ":" + this.b2.getAccountName()
              // + " with the sum " + this.transferSum);
              // this.transferOperation.transfer(this.b1, this.b2, this.transferSum);
       }

}