import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AddOperation implements Runnable {
    //firstPipe for the first number coming from the queue
    private ArrayBlockingQueue<BigInteger> firstPipe;
    //the result number
    private ArrayBlockingQueue<BigInteger> resultPipe;
    //the number in question
    private BigInteger myNumber;
    //the count that will hold the number of zeros that we want
    private BigInteger indexPos = BigInteger.ONE;
    //the transport from adding digit with digit
    private BigInteger transport = BigInteger.ZERO;
    //the result number
    private BigInteger sum = BigInteger.ZERO;

    private void transportNumber(){
        //setting the transport
        if (sum.compareTo(BigInteger.TEN) >= 0) {
            transport = sum.divide(BigInteger.TEN);
            sum = sum.mod(BigInteger.TEN);
        } else {
            transport = BigInteger.ZERO;
        }
    }


    AddOperation(BigInteger number, BigInteger secondNumber, ArrayBlockingQueue<BigInteger> queueOUT) {
        //consturctor for the first operation
        this.firstPipe = new ArrayBlockingQueue<>(secondNumber.toString().length() + 1);
        this.resultPipe = queueOUT;
        this.myNumber = number;
        this.splitNumberInQueueIN(secondNumber);
    }

    AddOperation(BigInteger number, ArrayBlockingQueue<BigInteger> queueIN, ArrayBlockingQueue<BigInteger> queueOUT) {
        //consturctor for the rest of operations
        this.firstPipe = queueIN;
        this.resultPipe = queueOUT;
        this.myNumber = number;
    }


    @Override
    public void run() {
        try {
            add();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void add() throws InterruptedException {
        // adding the myNumber with the firstPipe and setting the result in the queueOut
        BigInteger digit = this.firstPipe.poll(5, TimeUnit.SECONDS);

        while (Objects.requireNonNull(digit).compareTo(new BigInteger("-1")) != 0) {
            this.indexPos = this.indexPos.multiply(BigInteger.TEN);

            sum = digit
                    .add(this.myNumber.mod(this.indexPos).divide(this.indexPos.divide(BigInteger.TEN)))
                    .add(this.transport);

            this.transportNumber();
            resultPipe.offer(sum);

            digit = this.firstPipe.poll(5, TimeUnit.SECONDS);
        }
        //here we check if we have a transport (from the final operations ) and add it to the final myNumber
        while (this.indexPos.compareTo(this.myNumber) <= 0 || this.transport.compareTo(BigInteger.ZERO) != 0){
            if (this.indexPos.compareTo(this.myNumber) <= 0) {
                this.indexPos = this.indexPos.multiply(BigInteger.TEN);

                sum = digit
                        .add(this.myNumber.mod(this.indexPos).divide(this.indexPos.divide(BigInteger.TEN)))
                        .add(this.transport);

                this.transportNumber();
                resultPipe.offer(sum);

            } else {
                resultPipe.offer(this.transport);
                this.transport = BigInteger.ZERO;
            }

        }

        resultPipe.offer(new BigInteger("-1"));
    }

    private void splitNumberInQueueIN(BigInteger number) {
        this.firstPipe.clear();

        while (number.compareTo(BigInteger.ZERO) != 0) {
            this.firstPipe.add(number.mod(BigInteger.TEN));
            number = number.divide(BigInteger.TEN);
        }

        this.firstPipe.add(new BigInteger("-1"));
    }
}