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
    private static List<ThreadOperation> threadOperations = new ArrayList<>();

    private static List<String> getThreadsName(String fileName) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));

        List<String> items = Arrays.asList(data.split("\\r?\\n"));
        return items;
    }

    private static void init(Integer numberOfOperations, Boolean useMutexex) throws Exception {

//              List<String> threadsName = BankAccount
//                            .getThreadsName("C:\\Users\\VictorViena\\IdeaProjects\\paralel\\src\\Task01\\threadNames.txt");

        List<String> threadsName = BankAccount
                .getThreadsName("D:\\Paralel and Distributed Systems\\paralel\\src\\Task01\\threadNames.txt");


        for (char alphabet = 'A'; alphabet <= 'B'; alphabet++) {
            BankAccount.bankAccounts.add(new BankAccountInstance(String.valueOf(alphabet), 100000));
        }
        for (int i = 1; i <= numberOfOperations; i++) {
            Random rand = new Random();
            BankAccountInstance b1 = BankAccount.bankAccounts
                    .get(rand.nextInt(BankAccount.bankAccounts.size()));
            BankAccountInstance b2 = BankAccount.bankAccounts
                    .get(rand.nextInt(BankAccount.bankAccounts.size()));
            if (b1 != b2) {
                int randInt = rand.nextInt(100000000);
//                            int transferSum = rand.nextInt(5500);
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
            Integer cmd;
            Integer nrOfOperations;
            boolean flag = true;

            while (true) {
                BankAccount.showMenu();
                cmd = in.nextInt();
                if (cmd != 0) {
                    System.out.println("The number of operations on accounts?");
                    nrOfOperations = in.nextInt();
                }
                switch (cmd) {
                    case 1: {
                        BankAccount.init(nrOfOperations, true);
                        ExecutorService executorService = Executors.newFixedThreadPool(25);
                        BankAccount.threadOperations.forEach(executorService::execute);
                        executorService.shutdown();
                        break;
                    }
                    case 2: {
                        BankAccount.init(nrOfOperations, false);
                        ExecutorService executorService = Executors.newFixedThreadPool(25);
                        BankAccount.threadOperations.forEach(executorService::execute);
                        executorService.shutdown();
                        break;

                    }
                    case 3: {
                        BankAccount.init(nrOfOperations, false);
                        long startTime = System.nanoTime();
                        BankAccount.threadOperations.forEach(ThreadOperation::run);
                        long endTime = System.nanoTime();
                        long durationNC = (endTime - startTime);
                        System.out.println("Total execution time not concurrent " + durationNC);
                        break;

                    }

                    case 0:
                        break;
                }

                System.out.println("Programul o sa adoarma");
                TimeUnit.SECONDS.sleep(15);
                System.out.println("Programul s-a trezit");

                PrintWriter writer = new PrintWriter("result.txt", StandardCharsets.UTF_8);
                BankAccount.bankAccounts.forEach((b) -> {
                    writer.println(b.getLogs());
                });
                writer.close();
                BankAccount.clearInit();

            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}