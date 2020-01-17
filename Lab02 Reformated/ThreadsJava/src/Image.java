import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Image {
    private int height;
    private int width;
    private String filePath;

    private int[][] redValuesMatrix;
    private int[][] greenValuesMatrix;
    private int[][] blueValuesMatrix;
    private int[][] grayScale;
    private int[][] whiteFilter;

    private int nrThreads = 10;


    public Image(String filePath) {
        this.filePath = filePath;
        readImageFromFile();
    }

    private void readImageFromFile() {
        // given the file path we read the image and complete the rgb matrixes and initialize the  grayscale matrix
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filePath));
            height = img.getHeight();
            width = img.getWidth();
            redValuesMatrix = new int[height][width];
            greenValuesMatrix = new int[height][width];
            blueValuesMatrix = new int[height][width];
            grayScale = new int[height][width];
            whiteFilter = new int[height][width];

            int[] aux;
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    aux = img.getRaster().getPixel(j, i, new int[3]);
                    redValuesMatrix[i][j] = aux[0];
                    greenValuesMatrix[i][j] = aux[1];
                    blueValuesMatrix[i][j] = aux[2];
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][][] makeRGBMatrix(int[][] r, int[][] g, int[][] b) {
        int[][][] pixelMatrix = new int[height][width][3];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int[] pixel = new int[3];
                pixel[0] = r[i][j];
                pixel[1] = g[i][j];
                pixel[2] = b[i][j];
                pixelMatrix[i][j] = pixel;
            }

        return pixelMatrix;
    }


    private void saveConstrucedImageToDisc(String fileName, int[][][] pixelMatrix) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                img.getRaster().setPixel(j, i, pixelMatrix[i][j]);
            }
        fileName = fileName + ".jpg";
        File outputfile = new File(fileName);
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //here we are using the threads
    private void grayScaleMatrixConstructor() {
        ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);

        for (int i = 0; i < height; i++) {
            executorService.execute(new GreyScaleRunnable(i, width, redValuesMatrix, greenValuesMatrix, blueValuesMatrix, grayScale));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void whiteFilterMatrixConstructor() {
        ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);

        for (int i = 0; i < height; i++) {
            executorService.execute(new WhiteFilterRunnable(i, width, redValuesMatrix, greenValuesMatrix, blueValuesMatrix, whiteFilter));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public float saveGrayscaleToFile(String fileName) {
        float start = System.nanoTime() / 1000000;
        grayScaleMatrixConstructor();
        saveConstrucedImageToDisc(fileName, makeRGBMatrix(grayScale, grayScale, grayScale));
        System.out.println(makeRGBMatrix(grayScale, grayScale, grayScale).toString());
        float end = System.nanoTime() / 1000000;
        return (end - start) / 1000;
    }

    public float whiteFilterImageToFile(String fileName) {
        float start = System.nanoTime() / 1000000;
        whiteFilterMatrixConstructor();
        saveConstrucedImageToDisc(fileName, makeRGBMatrix(whiteFilter, whiteFilter, whiteFilter));
        System.out.println(makeRGBMatrix(whiteFilter, whiteFilter, whiteFilter).toString());
        float end = System.nanoTime() / 1000000;

        return (end - start) / 1000;
    }


}
