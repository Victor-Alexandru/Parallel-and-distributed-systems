package Task01;

/**
 * Operation
 */
class Operation {
    //this class takes to bankAccounts and
    // makes a lock transfer if we need to use mutexes,between two accounts
    //makes  a transfer withouth a lock if we didnt need to use mutexex between two accounts
    private Integer uniqueId;

    Operation(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    private void incrementUniqueId() {
        this.uniqueId += 1;
    }

    Integer getUniqueId() {
        return this.uniqueId;
    }

    boolean transfer(BankAccountInstance b1, BankAccountInstance b2, Integer sum) {
        if (b1.getBalance() < sum) {
            b1.appendOperationToLogInssufficentFunds(this.uniqueId.toString(), b2.getAccountName(), sum);
            return false;
        } else {
            b1.substractBalance(sum);
            b2.addBalance(sum);
            b2.appendOperationToLogTransfer(this.uniqueId.toString(), b1.getAccountName(), sum);
            b1.appendOperationToLogDecrement(this.uniqueId.toString(), b2.getAccountName(), sum);
            this.incrementUniqueId();
            return true;

        }

    }

    boolean lockTransfer(BankAccountInstance b1, BankAccountInstance b2, Integer sum) throws InterruptedException {

        if (b1.getId() > b2.getId()) {
            if (b1.lockBankAccountTransaction(this.uniqueId.toString(), b2.getAccountName(), sum, "-")) {
                b2.lockBankAccountTransaction(this.uniqueId.toString(), b1.getAccountName(), sum, "+");

            } else {
                return false;
            }

        } else {
            if (b1.lockHasBalanceEnough(sum)) {
                b2.lockBankAccountTransaction(this.uniqueId.toString(), b1.getAccountName(), sum, "+");
                b1.lockBankAccountTransaction(this.uniqueId.toString(), b2.getAccountName(), sum, "-");

            } else {
                return false;
            }
        }

        return true;

    }
}