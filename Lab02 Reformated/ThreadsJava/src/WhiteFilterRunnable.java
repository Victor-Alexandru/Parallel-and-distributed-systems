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
            int sum = (int) (redValues[i][j] * 0.200 + greenValues[i][j] * 0.700 + blueValues[i][j] * 0.100);
            if (sum > 255)
                whiteFilter[i][j] = 255;
            else
                whiteFilter[i][j] = sum;
        }
    }
}
