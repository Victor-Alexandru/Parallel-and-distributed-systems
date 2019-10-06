/**
 * Operation
 */
public class Operation {

       private Integer uniqueId;

       public Operation(Integer uniqueId) {
              this.uniqueId = uniqueId;
       }

       public void incrementUniqueId() {
              this.uniqueId += 1;
       }

       public Integer getUniqueId() {
              return this.uniqueId;
       }

       public void transfer(BankAccountInstance b1, BankAccountInstance b2, Integer sum)  {
              if (b1.getBalance() < sum) {
                     System.out.println("The account " + b1.getAccountName() + " has less money than " + sum);
              } else {
                     b2.appendOperationToLogTransfer(this.uniqueId.toString(), b1, sum);
                     b1.appendOperationToLogDecrement(this.uniqueId.toString(), b2, sum);
                     b1.substractBalance(sum);
                     b2.addBalance(sum);
                     this.incrementUniqueId();
              }

       }
}