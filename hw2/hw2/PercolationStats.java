package hw2;
import edu.princeton.cs.introcs.*;

import java.util.ArrayList;

import static edu.princeton.cs.introcs.StdRandom.uniform;

public class PercolationStats {
    /** The mean of percolation threshold */
    private double mean;
    /** sample mean of percolation threshold */
    private double stddev;
    /** low endpoint of 95% confidence interval */
    private double confidenceLow;
    /** high endpoint of 95% confidence interval */
    private double confidenceHigh;

    /** Perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        double[] results = new double[T];
        double total = 0;
        for (int i = 0; i < T; i++) {
            Percolation current = pf.make(N);
            while (!current.percolates()) {
                int row = uniform(N);
                int col = uniform(N);
                current.open(row, col);
            }
            results[i] = ((double) current.numberOfOpenSites() / N);
        }
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLow =  mean - (1.96 * Math.sqrt(stddev) / Math.sqrt(T));
        confidenceHigh =  mean + (1.96 * Math.sqrt(stddev) / Math.sqrt(T));
    }

    /** @return sample mean of percolation threshold */
    public double mean() {
        return mean;
    }

    /** @return sample standard deviation of percolation threshold */
    public double stddev() {
        return stddev;
    }

    /** @return low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return confidenceLow;
    }

    /** @return high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return confidenceHigh;
    }
}
