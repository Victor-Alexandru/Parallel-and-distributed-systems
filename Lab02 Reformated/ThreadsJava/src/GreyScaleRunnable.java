public class GreyScaleRunnable implements Runnable {
    private int width;
    private int i;

    private int[][] redValues;
    private int[][] greenValues;
    private int[][] blueValues;
    private int[][] grayScale;

    public GreyScaleRunnable(int i, int width, int[][] r, int[][] g, int[][] b, int[][] grayScale) {
        this.i = i;
        this.width = width;
        this.redValues = r;
        this.greenValues = g;
        this.blueValues = b;
        this.grayScale = grayScale;

    }

    @Override
    public void run() {
        for (int j = 0; j < width; j++) {
            int sum = (int) (redValues[i][j] * 0.299 + greenValues[i][j] * 0.587 + blueValues[i][j] * 0.144);
            if (sum > 255)
                grayScale[i][j] = 255;
            else
                grayScale[i][j] = sum;
        }
    }
}
