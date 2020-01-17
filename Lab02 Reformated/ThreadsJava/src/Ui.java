import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ui {
    public void run() {
        while (true) {
            printMenu();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                int input = Integer.parseInt(br.readLine());
                switch (input) {
                    case 0:
                        System.exit(0);
                    case 1:
                        whiteFilter();
                        break;
                    case 2:
                        grayScale();
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void grayScale() {
        Image img100x133 = new Image("../data/100x133.jpg");
        System.out.println("Time for 100x133 is: " + img100x133.saveGrayscaleToFile("./data/gray_img100x133"));

        Image img1200x56 = new Image("../data/1200x56.jpg");
        System.out.println("Time for 1200x56 is: " + img1200x56.saveGrayscaleToFile("./data/gray_img1200x56"));

        Image img1920x1080 = new Image("../data/1920x1080.jpg");
        System.out.println("Time for 1920x1080 is: " + img1920x1080.saveGrayscaleToFile("./data/gray_img1920x1080"));
    }

    private void whiteFilter() {

        Image img100x133 = new Image("../data/100x133.jpg");
        System.out.println("Time for 100x133 is: " + img100x133.whiteFilterImageToFile("./data/white_img100x133"));

        Image img1200x56 = new Image("../data/1200x56.jpg");
        System.out.println("Time for 1200x56 is: " + img1200x56.whiteFilterImageToFile("./data/white_img1200x56"));

        Image img1920x1080 = new Image("../data/1920x1080.jpg");
        System.out.println("Time for 1920x1080 is: " + img1920x1080.whiteFilterImageToFile("./data/white_img1920x1080"));


    }


    private void printMenu() {
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("1. White filter");
        System.out.println("2. Gray scale");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
        System.out.println();
    }
}
