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

       public void transfer(BankAccountInstance b1, BankAccountInstance b2, Integer sum) throws Exception {
              if (b1.getBalance() < sum) {
                     throw new Exception("The account " + b1.getAccountName() + " has less money than " + sum);
              } else {
                     b2.appendOperationToLog(this.uniqueId.toString(), b1, sum);
                     b1.appendOperationToLogGiven(this.uniqueId.toString(),b2, sum);
                     b1.substractBalance(sum);
                     b2.addBalance(sum);
                     this.incrementUniqueId();
              }

       }
}