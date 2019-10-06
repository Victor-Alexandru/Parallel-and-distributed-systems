import java.util.concurrent.Semaphore;

/**
 * ThreadOperation
 */
public class ThreadOperation implements Runnable {

       BankAccountInstance b1;
       BankAccountInstance b2;
       Integer transferSum;
       //our Semaphore is same as a Mutex.
       //official doc : 
       //Semaphores in java are like Monitors and locks that provide mutual 
       //exclusivity to multiple threads and make threads wait on certain condition to be true.
       static Semaphore semaphore = new Semaphore(1, true);
       Operation transferOperation;

       public ThreadOperation(BankAccountInstance b1, BankAccountInstance b2, Integer transferSum,
                     Integer operationId) {
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
                     System.out.println("---------------------------------------");
                     System.out.println("executting ------------- " + Thread.currentThread().getName()
                                   + "  : Operation " + this.transferOperation.getUniqueId());
                     
                     this.transferOperation.transfer(this.b1, this.b2, this.transferSum);

                     System.out.println("---------------------------------------");
                     semaphore.release();
              } catch (InterruptedException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }
       }

}