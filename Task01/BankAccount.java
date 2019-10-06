
/**
 * BankAccount
 */
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;

public class BankAccount {

       public static List<BankAccountInstance> bankAccounts = new ArrayList<BankAccountInstance>();
       public static List<ThreadOperation> threadOperations = new ArrayList<ThreadOperation>();

       public static void init() {
              for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                     BankAccount.bankAccounts.add(new BankAccountInstance(String.valueOf(alphabet), 1000));
              }
              for (int i = 1; i <= 10000; i++) {
                     Random rand = new Random();
                     BankAccountInstance b1 = BankAccount.bankAccounts
                                   .get(rand.nextInt(BankAccount.bankAccounts.size()));
                     BankAccountInstance b2 = BankAccount.bankAccounts
                                   .get(rand.nextInt(BankAccount.bankAccounts.size()));
                     if (b1 != b2) {
                            int randInt = rand.nextInt(100000000);
                            int transferSum = rand.nextInt(5500);
                            BankAccount.threadOperations.add(new ThreadOperation(b1, b2, transferSum, randInt));

                     }

              }

       }

       public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
              Operation op = new Operation(534523);
              try {
                     BankAccount.init();
                     BankAccount.threadOperations.forEach(r -> r.run());
              }

              catch (Exception exception) {
                     System.out.println(exception.getMessage());

              } finally {
                     PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
                     BankAccount.bankAccounts.forEach((b) -> {
                            writer.println(b.getLogs());
                     });
                     writer.close();

              }

       }
}