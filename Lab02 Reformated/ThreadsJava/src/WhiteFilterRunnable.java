public class WhiteFilterRunnable implements Runnable {
    private int width;
    private int i;

    private int[][] redValues;
    private int[][] greenValues;
    private int[][] blueValues;
    private int[][] whiteFilter;

    public WhiteFilterRunnable(int i, int width, int[][] r, int[][] g, int[][] b, int[][] wf) {
        this.i = i;
        this.width = width;
        this.redValues = r;
        this.greenValues = g;
        this.blueValues = b;
        this.whiteFilter = wf;

    }

    @Override
    public void run() {
        for (int j = 0; j < width; j++) {
            int sum = redValues[i][j] + greenValues[i][j] + (int) (blueValues[i][j] / 3);
            if (sum > 255)
                sum = 255;
            whiteFilter[i][j] = sum;
        }
    }
}
