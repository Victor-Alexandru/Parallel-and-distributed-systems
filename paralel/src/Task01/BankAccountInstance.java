package Task01;
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
              this.logs.add(this.accountName + "\n");
       }

       public void appendOperationToLogTransfer(String operationName, BankAccountInstance b2, Integer sum) {
              this.logs.add("Operation" + operationName + " has transferd from " + b2.getAccountName() + " to "
                            + this.getAccountName() + " the sum of " + sum +  " ----remaining money "+this.getBalance()+'\n');

       }

       public void appendOperationToLogDecrement(String operationName, BankAccountInstance b2, Integer sum) {
              this.logs.add("Operation" + operationName + " has taken from account " + this.getAccountName()
                            + " the sum of " + sum + " and put to account " + b2.getAccountName() +  " ----remaining money "+this.getBalance()+ '\n');

       }

       public void appendOperationToLogInssufficentFunds(String operationName, BankAccountInstance b2, Integer sum) {
              this.logs.add("Insufficent money " + sum + " to transfer from " + this.getAccountName() + " to "
                            + b2.getAccountName() + " ----remaining money "+this.getBalance()+ '\n');

       }

       public void addBalance(Integer sum) {
              this.balance += sum;
       }

       public void substractBalance(Integer sum) {
              this.balance -= sum;
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