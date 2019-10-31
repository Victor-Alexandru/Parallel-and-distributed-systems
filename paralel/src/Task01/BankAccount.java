package Task01;
/**
 * BankAccount
 */

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class BankAccount {

    private static List<BankAccountInstance> bankAccounts = new ArrayList<>();
    private static List<Runnable> threadOperations = new ArrayList<>();


    private static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private static List<String> getThreadsName(String fileName) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));

        List<String> items = Arrays.asList(data.split("\\r?\\n"));
        return items;
    }

    private static void init(Integer numberOfOperations, Boolean useMutexex) throws Exception {

              List<String> threadsName = BankAccount
                            .getThreadsName("C:\\paralel and distributed\\Parallel-and-distributed-systems\\paralel\\src\\Task01\\threadNames.txt");
//
//        List<String> threadsName = BankAccount
//                .getThreadsName("D:\\paralele\\Parallel-and-distributed-systems\\paralel\\src\\Task01\\threadNames.txt");
//

        for (char alphabet = 'A'; alphabet <= 'E'; alphabet++) {
            BankAccount.bankAccounts.add(new BankAccountInstance(String.valueOf(alphabet), 100000));
        }
        int contoraudit = 0;
        for (int i = 1; i <= numberOfOperations; i++) {
            Random rand = new Random();
            BankAccountInstance b1 = BankAccount.bankAccounts
                    .get(rand.nextInt(BankAccount.bankAccounts.size()));
            BankAccountInstance b2 = BankAccount.bankAccounts
                    .get(rand.nextInt(BankAccount.bankAccounts.size()));

            if (i > numberOfOperations / 2 && contoraudit == 0) {
                BankAccount.threadOperations.add(new Audit(BankAccount.bankAccounts, "auditIntermediar.txt"));
                contoraudit++;
            }

            if (b1 != b2) {
                int randInt = rand.nextInt(100000000);
//              int transferSum = rand.nextInt(5500);
                int transferSum = 200;

                BankAccount.threadOperations
                        .add(new ThreadOperation(threadsName.get(i), b1, b2, transferSum, randInt, useMutexex));

            }
        }


    }

    private static void clearInit() {
        BankAccount.bankAccounts.clear();
        BankAccount.threadOperations.clear();
    }

    private static void showMenu() {
        System.out.println("---------------------------------------------");
        System.out.println("1.Concurent with mutexes");
        System.out.println("2.Concurent with no  mutexes");
        System.out.println("3.Not Concurent");
        System.out.println("0.Exit ");
        System.out.println("---------------------------------------------");
        System.out.println("Your command ");
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        try {
            Scanner in = new Scanner(System.in);
            int cmd;
            Integer nrOfOperations;
            boolean flag = true;


            BankAccount.showMenu();
            cmd = in.nextInt();


            System.out.println("The number of operations on accounts?");
            nrOfOperations = in.nextInt();

            switch (cmd) {
                case 1: {
                    BankAccount.init(nrOfOperations, true);
                    ExecutorService executorService = Executors.newFixedThreadPool(16);
                    BankAccount.threadOperations.forEach(executorService::execute);
                    executorService.shutdown();
                    BankAccount.awaitTerminationAfterShutdown(executorService);
                    break;
                }
                case 2: {
                    BankAccount.init(nrOfOperations, false);
                    ExecutorService executorService = Executors.newFixedThreadPool(16);
                    BankAccount.threadOperations.forEach(executorService::execute);
                    executorService.shutdown();
                    BankAccount.awaitTerminationAfterShutdown(executorService);
                    break;
                }
                case 3: {
                    BankAccount.init(nrOfOperations, false);
                    long startTime = System.nanoTime();
                    BankAccount.threadOperations.forEach(Runnable::run);
                    long endTime = System.nanoTime();
                    long durationNC = (endTime - startTime);
                    System.out.println("Total execution time not concurrent " + durationNC);
                    break;

                }

            }


            PrintWriter writer = new PrintWriter("result.txt", StandardCharsets.UTF_8);
            BankAccount.bankAccounts.forEach((b) -> {
                writer.println(b.getLogs());
            });
            ExecutorService executorService = Executors.newFixedThreadPool(16);
            executorService.execute(new Audit(BankAccount.bankAccounts, "auditFinal.txt"));
            executorService.shutdown();
            BankAccount.awaitTerminationAfterShutdown(executorService);
            writer.close();
            BankAccount.clearInit();


        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}