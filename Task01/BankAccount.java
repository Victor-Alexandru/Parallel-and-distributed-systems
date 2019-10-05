/**
 * BankAccount
 */
public class BankAccount {

       public static void main(String[] args) {
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
                     System.out.println(b1.getLogs());
                     System.out.println(b2.getLogs());

              }

       }
}