public class Main {

    public static void main(String[] args)  throws InterruptedException{
        Polinom p = new Polinom(80);
        Polinom q = new Polinom(80);
//		Polinom p = new Polinom(new ArrayList<Integer>(Arrays.asList(5, 4, 2, 4)));
//		Polinom q = new Polinom(new ArrayList<Integer>(Arrays.asList(6, 3, 7)));

        System.out.println("pol p:" + p);
        System.out.println("pol q" + q);
        System.out.println("\n");
        //1a
        System.out.println(base_seq(p, q).toString() + "\n");
        //1b
        System.out.println(parallel_base_seq(p, q).toString() + "\n");
    }

    private static Polinom parallel_base_seq(Polinom p, Polinom q) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Polinom result2 = Operations.baseMultiplicationParallel(p, q, 5);
        long endTime = System.currentTimeMillis();
        System.out.println("Seq parallel base op: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result2;
    }

    private static Polinom base_seq(Polinom p, Polinom q) {
        long startTime = System.currentTimeMillis();
        Polinom result1 = Operations.baseMultiplication(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Seq base op: ");
        System.out.println("Time elapsed: " + (endTime - startTime) + " ms");
        return result1;
    }

    private static Polinom multiplication3(Polinom p, Polinom q) {
        long startTime = System.currentTimeMillis();
        Polinom result3 = Operations.multiplicationKaratsubaSequentialForm(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Karatsuba sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result3;
    }
}
