
/**
 * BankAccountInstance
 */
import java.util.*;
import java.util.Arrays;
import java.util.List;

public class BankAccountInstance {

       private List<String> logs;
       String accountName;
       Integer balance;

       public BankAccountInstance(String name, Integer balance) {
              this.logs = new ArrayList<String>();
              this.accountName = name;
              this.balance = balance;
              this.logs.add("----------------------"+"\n"+this.accountName+"\n"+"----------------------");
       }

       public void appendOperationToLogTransfer(String operationName, BankAccountInstance b2, Integer sum) {
              this.logs.add("Operation" + operationName + " has transferd from " + b2.getAccountName() + " to "
                            + this.getAccountName() + " the sum of " + sum + '\n');

       }

       public void appendOperationToLogDecrement(String operationName, BankAccountInstance b2, Integer sum) {
              this.logs.add("Operation" + operationName + " has taken from account " + this.getAccountName()
                            + " the sum of " + sum + " and put to account " + b2.getAccountName() + '\n');

       }

       public void addBalance(Integer sum) {
              this.balance += sum;
       }

       public void substractBalance(Integer sum) {
              this.balance += sum;
       }

       public List<String> getLogs() {
              return this.logs;
       }

       public String getAccountName() {
              return this.accountName;
       }

       public Integer getBalance() {
              return this.balance;
       }
}