import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains the list of the terms of the polynomial.
 * The first terms in the list corresponds to x^0 and the last corresponds to the biggest power .
 */
public class Polinom {
    private List<Integer> terms;

    public Polinom(List<Integer> coefficients) {
        this.terms = coefficients;
    }

    /**
     * Generates a polynomial with random terms, with a given degree.
     *
     * @param degree - Integer
     */
    public Polinom(int degree) {
        terms = new ArrayList<>(degree + 1);
        Random randomGenerator = new Random();
        for (int i = 0; i < degree; i++) {
            terms.add(randomGenerator.nextInt(15));
        }
        terms.add(randomGenerator.nextInt(15) + 1);
    }

    public int getDegree() {
        return this.terms.size() - 1;
    }

    public int getLength() {
        return this.terms.size();
    }

    public List<Integer> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int power = getDegree();
        for (int i = getDegree(); i >= 0; i--) {
            if ( terms.get(i) == 0)
                continue;
            str.append(" ").append(terms.get(i)).append("x^").append(power).append(" +");
            power--;
        }
        str.deleteCharAt(str.length() - 1); //delete last +
        return str.toString();
    }
}
