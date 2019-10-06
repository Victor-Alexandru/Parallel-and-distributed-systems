
/**
 * BankAccount
 */
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.io.FileNotFoundException;

public class BankAccount {

       public static List<BankAccountInstance> bankAccounts = new ArrayList<BankAccountInstance>();
       public static List<ThreadOperation> threadOperations = new ArrayList<ThreadOperation>();

       public static List<String> getThreadsName(String fileName) throws Exception {
              String data = "";
              data = new String(Files.readAllBytes(Paths.get(fileName)));

              List<String> items = Arrays.asList(data.split("\\r?\\n"));
              return items;
       }

       public static void init(Integer numberOfOperations) throws Exception {

              List<String> threadsName = BankAccount
                            .getThreadsName("D:\\Paralel and Distributed Systems\\Task01\\threadNames.txt");
              for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                     BankAccount.bankAccounts.add(new BankAccountInstance(String.valueOf(alphabet), 1000));
              }
              for (int i = 1; i <= numberOfOperations; i++) {
                     Random rand = new Random();
                     BankAccountInstance b1 = BankAccount.bankAccounts
                                   .get(rand.nextInt(BankAccount.bankAccounts.size()));
                     BankAccountInstance b2 = BankAccount.bankAccounts
                                   .get(rand.nextInt(BankAccount.bankAccounts.size()));
                     if (b1 != b2) {
                            int randInt = rand.nextInt(100000000);
                            int transferSum = rand.nextInt(5500);
                            BankAccount.threadOperations
                                          .add(new ThreadOperation(threadsName.get(i), b1, b2, transferSum, randInt));

                     }

              }

       }

       public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
              Operation op = new Operation(534523);
              try {
                     BankAccount.init(100);

                     ExecutorService te = Executors.newCachedThreadPool();
                     long startTimeConcurrentExecution = System.nanoTime();
                     BankAccount.threadOperations.forEach(r -> te.execute(r));
                     te.shutdown();
                     long endTimeConcurrentExecution = System.nanoTime();
                     long duration = (endTimeConcurrentExecution - startTimeConcurrentExecution);

                     System.out.println("------------------------------");


                     // long startTime = System.nanoTime();
                     // BankAccount.threadOperations.forEach(r -> r.run());
                     // long endTime = System.nanoTime();
                     // long durationNC = (endTime - startTime);

                     PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
                     BankAccount.bankAccounts.forEach((b) -> {
                            writer.println(b.getLogs());
                     });
                     writer.close();

                     System.out.println("Total execution time concurrent " + duration);
                     // System.out.println("Total execution time not concurrent " + durationNC);

              }

              catch (Exception exception) {
                     System.out.println(exception.getMessage());

              }

       }
}