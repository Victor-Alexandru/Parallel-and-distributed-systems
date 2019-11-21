/**
 * This class is the task for the simple multiplication, with time complexity O(n^2)
 */
public class MultiplicationTask implements Runnable {
    private int start;
    private int end;
    private Polinom p1, p2, result;

    public MultiplicationTask(int start, int end, Polinom p1, Polinom p2, Polinom result) {
        this.start = start;
        this.end = end;
        this.p1 = p1;
        this.p2 = p2;
        this.result = result;
    }

    /**
     * Calculam termenii de la start pana la end
     */
    @Override
    public void run() {
        for (int index = start; index < end; index++) {
            //case - no more elements to calculate
            if (index > result.getLength()) {
                return;
            }
            //find all the pairs that we add to obtain the value of a result coefficient
            for (int j = 0; j <= index; j++) {
                if (j < p1.getLength() && (index - j) < p2.getLength()) {
                    int value = p1.getTerms().get(j) * p2.getTerms().get(index - j);
                    result.getTerms().set(index, result.getTerms().get(index) + value);
                }
            }
        }
    }
}
