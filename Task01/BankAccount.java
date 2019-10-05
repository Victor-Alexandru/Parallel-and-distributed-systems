
/**
 * BankAccount
 */
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.FileNotFoundException;

public class BankAccount {

       public static void main(String[] args) throws FileNotFoundException,UnsupportedEncodingException{
              BankAccountInstance b1 = new BankAccountInstance("A", 1000);
              BankAccountInstance b2 = new BankAccountInstance("B", 1000);
              Operation op = new Operation(534523);
              try {

                     op.transfer(b1, b2, 100);
                     op.transfer(b1, b2, 100);
                     op.transfer(b1, b2, 100);
                     op.transfer(b1, b2, 100);
                     op.transfer(b2, b1, 50);
                     op.transfer(b2, b1, 50);
                     op.transfer(b2, b1, 50);
                     op.transfer(b2, b1, 50);
                     op.transfer(b2, b1, 50);
              }

              catch (Exception exception) {
                     System.out.println(exception.getMessage());

              } finally {
                     PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
                     writer.println(b1.getLogs());
                     writer.println(b2.getLogs());
                     writer.close();

              }

       }
}